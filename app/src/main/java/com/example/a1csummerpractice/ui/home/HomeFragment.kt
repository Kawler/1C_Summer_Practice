package com.example.a1csummerpractice.ui.home

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a1csummerpractice.R
import com.example.a1csummerpractice.data.NewsDatabase
import com.example.a1csummerpractice.data.repository.NewsRepository
import com.example.a1csummerpractice.data.repository.NewsRoomRepository
import com.example.a1csummerpractice.databinding.FragmentHomeBinding
import com.example.a1csummerpractice.domain.adapters.NewsAdapter
import com.example.a1csummerpractice.domain.newsdt.NewsData
import com.example.a1csummerpractice.domain.newsdt.NewsItemData
import com.google.android.material.appbar.AppBarLayout
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt
import java.time.Instant
import java.time.ZoneId
import kotlin.math.abs


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var _newsAdapter: NewsAdapter
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val newsDao = NewsDatabase.getDatabase(requireContext()).dao()
        var roomData: List<NewsItemData> = listOf()
        lateinit var roomRepository: NewsRoomRepository

        //Берём данные из Бд в фоне
        lifecycleScope.launch(Dispatchers.IO) {
            roomRepository = NewsRoomRepository(newsDao)
            roomData = roomRepository.readAllData
        }

        //Firebase
        val remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 216000
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.fetchAndActivate().addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful) {
                binding.tbHome.post {
                    binding.tbHome.background.setTint(Color.parseColor(remoteConfig.getString("toolbar_color")))
                }
                binding.fabHome.post {
                    binding.fabHome.background.setTint(Color.parseColor(remoteConfig.getString("toolbar_color")))
                }
            }
        }

        //Управление кнопкой в зависимости от прокрутки тулбара
        binding.appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener(fun(
            appBarLayout: AppBarLayout, verticalOffset: Int
        ) {
            if (abs(verticalOffset) == appBarLayout.totalScrollRange) {
                //Collapsed
                binding.fabHome.visibility = View.VISIBLE
            } else if (verticalOffset == 0) {
                //Extended
                binding.fabHome.visibility = View.GONE
            } else {
                //Transition
                binding.fabHome.visibility = View.GONE
            }
        }))

        binding.fabHome.setOnClickListener {
            binding.appBarLayout.setExpanded(true)
        }

        //Получение новостей
        var newsData: NewsData?
        val newsRepository = NewsRepository()
        runBlocking {
            try {
                newsData = newsRepository.getNewsData()
                lifecycleScope.launch(Dispatchers.IO) {
                    roomRepository.deleteAllNews()
                    for (item in newsData!!.news!!) {
                        roomRepository.insertNews(item)
                    }
                }
            } catch (e: Exception) {
                newsData = null
                if (roomData == null) {
                    binding.tvHomeStatus.text = "Сервис новостей не отвечает"
                    binding.tvHomeStatus.visibility = View.VISIBLE
                } else {
                    //Если есть сохраннёные новости, то они отобразятся
                    binding.tvHomeStatus.visibility = View.GONE
                    newsData = NewsData(roomData, count = 0, error_msg = "")
                    Toast.makeText(context, "Загруженны сохранённые новости", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        if (newsData != null) {
            if (newsData!!.news?.isEmpty() == true) {
                Log.i("API Error mesage", newsData!!.error_msg)
                binding.tvHomeStatus.text = "Не удалось получить новости"
                binding.tvHomeStatus.visibility = View.VISIBLE
            }
        }

        //Спинер с месяцами
        val months: MutableList<Any> = mutableListOf(
            "  ",
            "Январь",
            "Февраль",
            "Март",
            "Апрель",
            "Май",
            "Июнь",
            "Июль",
            "Август",
            "Сентябрь",
            "Октябрь",
            "Ноябрь",
            "Декабрь"
        )
        val spinMAdapter = object :
            ArrayAdapter<Any>(requireContext(), android.R.layout.simple_spinner_item, months) {
            //Изменяем цвет выбранного элемента в списке
            override fun getDropDownView(
                position: Int, convertView: View?, parent: ViewGroup
            ): View {
                return super.getDropDownView(position, convertView, parent).also { view ->
                    if (position == binding.homeTbSpinnerMonth.selectedItemPosition) view.setBackgroundColor(
                        resources.getColor(R.color.light_cyan)
                    )
                }
            }
        }.also { adapter ->
            adapter.setDropDownViewResource(R.layout.home_dropdown_spinner_menu)
            binding.homeTbSpinnerMonth.adapter = adapter
        }

        //Заполняем массив с доступными для сортировки годами и создаём спинер с годами
        val years: MutableList<Any> = mutableListOf()
        years.add("")
        if (newsData != null) {
            if (newsData!!.news?.isEmpty() == false) {
                years.add(
                    Instant.ofEpochSecond(newsData!!.news!![0].news_date_uts.toLong())
                        .atZone(ZoneId.of("Europe/Moscow")).year
                )
                for (item in newsData!!.news!!) {
                    years.add(
                        Instant.ofEpochSecond(item.news_date_uts.toLong())
                            .atZone(ZoneId.of("Europe/Moscow")).year
                    )
                }
            }
        }
        val spinYAdapter = object : ArrayAdapter<Any>(
            requireContext(), android.R.layout.simple_spinner_item, years.distinct()
        ) {
            //Изменяем цвет выбранного элемента в списке
            override fun getDropDownView(
                position: Int, convertView: View?, parent: ViewGroup
            ): View {
                return super.getDropDownView(position, convertView, parent).also { view ->
                    if (position == binding.homeTbSpinnerYear.selectedItemPosition) view.setBackgroundColor(
                        resources.getColor(R.color.light_cyan)
                    )
                }
            }
        }.also { adapter ->
            adapter.setDropDownViewResource(R.layout.home_dropdown_spinner_menu)
            binding.homeTbSpinnerYear.adapter = adapter
        }


        //Изменить цвет выбранного итема в спиннере
        val listener: AdapterView.OnItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>, view: View?, position: Int, id: Long
                ) {
                    if (view != null) (parent.getChildAt(0) as TextView).setTextColor(
                        resources.getColor(
                            R.color.dark_cyan
                        )
                    )
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        binding.homeTbSpinnerMonth.onItemSelectedListener = listener
        binding.homeTbSpinnerYear.onItemSelectedListener = listener

        //Заполнение списка новостей
        val layManager = LinearLayoutManager(context)
        _newsAdapter = NewsAdapter()
        if (newsData != null) if (newsData!!.news != null) _newsAdapter.data = newsData!!.news!!


        binding.homeRv.layoutManager = layManager
        binding.homeRv.adapter = _newsAdapter
        //Логика работы кнопок в тулбаре - обновление данных
        binding.homeTbReloadBtn.setOnClickListener {
            binding.tvHomeStatus.visibility = View.INVISIBLE
            runBlocking {
                try {
                    newsData = newsRepository.getNewsData()
                    lifecycleScope.launch(Dispatchers.IO) {
                        roomRepository.deleteAllNews()
                        for (item in newsData!!.news!!) {
                            roomRepository.insertNews(item)
                        }
                    }
                    _newsAdapter.data = newsData!!.news!!
                    _newsAdapter.notifyDataSetChanged()
                } catch (e: Exception) {
                    newsData = null
                    if (roomData == null) {
                        binding.tvHomeStatus.text = "Сервис новостей не отвечает"
                        binding.tvHomeStatus.visibility = View.VISIBLE
                    } else {
                        //Если есть сохраннёные новости, то они отобразятся
                        binding.tvHomeStatus.visibility = View.GONE
                        newsData = NewsData(roomData, count = 0, error_msg = "")
                        Toast.makeText(context, "Нет ответа от сервера", Toast.LENGTH_SHORT).show()
                        _newsAdapter.data = newsData!!.news!!
                        _newsAdapter.notifyDataSetChanged()
                    }
                }
                binding.homeTbSpinnerMonth.setSelection(0)
                binding.homeTbSpinnerYear.setSelection(0)
            }
        }

        //Принятие сортировки данных
        binding.homeTbAcceptBtn.setOnClickListener {
            if (newsData != null && newsData!!.news != null) {
                val newData = setMonthYear(
                    spinnerM = binding.homeTbSpinnerMonth,
                    spinnerY = binding.homeTbSpinnerYear,
                    newsData!!.news!!
                )
                if (newData.isEmpty()) {
                    binding.tvHomeStatus.text = "Нет новостей за выбранный период"
                    binding.tvHomeStatus.visibility = View.VISIBLE
                } else {
                    binding.tvHomeStatus.visibility = View.GONE
                }
                _newsAdapter.data = newData
                _newsAdapter.notifyDataSetChanged()
            } else {
                binding.tvHomeStatus.text = "Сервис новостей не отвечает"
                binding.tvHomeStatus.visibility = View.VISIBLE
            }
        }

        //Получаем преференс для версии приложения и первого запуска
        val sharedPreferences =
            requireActivity().getSharedPreferences("app_version", Context.MODE_PRIVATE)
        val sharedEditor = sharedPreferences.edit()

        //Проверяем совпадение текущей версии приложения со старой и является ли это первым запуском
        if (sharedPreferences.getString(
                "app_version", "version"
            ) != context?.packageManager?.getPackageInfo(
                requireContext().packageName, 0
            )?.versionName || sharedPreferences.getBoolean("first_launch", true)
        ) {
            //Уточняем версию приложения
            sharedEditor.putString(
                "app_version", context?.packageManager?.getPackageInfo(
                    requireContext().packageName, 0
                )?.versionName
            ).apply()
            sharedEditor.putBoolean("first_launch", false).apply()

            binding.homeTbSpinnerMonth.post {
                MaterialTapTargetPrompt.Builder(this).setTarget(binding.homeTbSpinnerMonth)
                    .setPrimaryText("Поле выбора месяца")
                    .setSecondaryText("Используется для сортировки новостей по месяцам")
                    .setBackButtonDismissEnabled(true)
                    .setFocalColour(resources.getColor(R.color.light_cyan))
                    .setBackgroundColour(resources.getColor(R.color.dark_cyan))
                    .setPromptStateChangeListener { promt, state ->
                        if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED) {
                            MaterialTapTargetPrompt.Builder(this)
                                .setTarget(binding.homeTbSpinnerYear)
                                .setPrimaryText("Поле выбора года")
                                .setSecondaryText("Используется для сортировки новостей по месяцам годам")
                                .setBackButtonDismissEnabled(true)
                                .setBackgroundColour(resources.getColor(R.color.dark_cyan))
                                .setFocalColour(resources.getColor(R.color.light_cyan))
                                .setPromptStateChangeListener { promt, state ->
                                    if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED) {
                                        MaterialTapTargetPrompt.Builder(this)
                                            .setTarget(binding.homeTbAcceptBtn)
                                            .setPrimaryText("Кнопка принятия параметров сортировки")
                                            .setSecondaryText("Нажмите, чтобы отсортировать новости")
                                            .setBackButtonDismissEnabled(true)
                                            .setBackgroundColour(resources.getColor(R.color.dark_cyan))
                                            .setFocalColour(resources.getColor(R.color.light_cyan))
                                            .setPromptStateChangeListener { promt, state ->
                                                if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED) {
                                                    MaterialTapTargetPrompt.Builder(this)
                                                        .setTarget(binding.homeTbReloadBtn)
                                                        .setPrimaryText("Кнопка обновление новостей с сервера")
                                                        .setSecondaryText("Нажмите, чтобы обновить новости")
                                                        .setBackButtonDismissEnabled(true)
                                                        .setBackgroundColour(resources.getColor(R.color.dark_cyan))
                                                        .setFocalColour(resources.getColor(R.color.light_cyan))
                                                        .setPromptStateChangeListener { promt, state ->
                                                            if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED) {
                                                                binding.appBarLayout.setExpanded(
                                                                    false
                                                                )
                                                                MaterialTapTargetPrompt.Builder(this)
                                                                    .setTarget(binding.fabHome)
                                                                    .setPrimaryText("Кнопка для открытия поля сортировки")
                                                                    .setSecondaryText("Нажмите, чтобы открыть поле для сортировки")
                                                                    .setBackButtonDismissEnabled(
                                                                        true
                                                                    ).setFocalColour(
                                                                        resources.getColor(
                                                                            R.color.light_cyan
                                                                        )
                                                                    ).setBackgroundColour(
                                                                        resources.getColor(
                                                                            R.color.dark_cyan
                                                                        )
                                                                    ).show()
                                                            }
                                                        }.show()
                                                }
                                            }.show()
                                    }
                                }.show()
                        }
                    }.show()
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

fun setMonthYear(
    spinnerM: Spinner, spinnerY: Spinner, newsData: List<NewsItemData>
): List<NewsItemData> {
    var month: Int? = null
    when (spinnerM.selectedItemPosition) {
        1 -> month = 1
        2 -> month = 2
        3 -> month = 3
        4 -> month = 4
        5 -> month = 5
        6 -> month = 6
        7 -> month = 7
        8 -> month = 8
        9 -> month = 9
        10 -> month = 10
        11 -> month = 11
        12 -> month = 12
    }
    return if (spinnerY.selectedItemPosition != 0) {
        getData(newsData, month, spinnerY.selectedItem as Int?)
    } else {
        getData(newsData, month, null)
    }
}

//Проводится проверка на выбранные условия в тулбаре и конвертируется время новости, после этого, если
//новость соответсвует условиям, то она добовляется в отсортированный список новостей
fun getData(newsData: List<NewsItemData>, month: Int?, year: Int?): List<NewsItemData> {
    var newData = newsData
    val newsArray: MutableList<NewsItemData> = mutableListOf()
    if (month != null && year == null) {
        for (item in newsData) {
            val zdt = Instant.ofEpochSecond(item.news_date_uts.toLong())
                .atZone(ZoneId.of("Europe/Moscow"))
            if (zdt.monthValue == month) {
                Log.i("News", item.news_date_uts + "  ||   " + item.news_date)
                newsArray.add(item)
            }
        }
        newData = newsArray
    }
    if (month == null && year != null) {
        for (item in newsData) {
            val zdt = Instant.ofEpochSecond(item.news_date_uts.toLong())
                .atZone(ZoneId.of("Europe/Moscow"))
            if (zdt.year == year) {
                newsArray.add(item)
            }
        }
        newData = newsArray
    }
    if (month != null && year != null) {
        for (item in newsData) {
            val zdt = Instant.ofEpochSecond(item.news_date_uts.toLong())
                .atZone(ZoneId.of("Europe/Moscow"))
            if (zdt.monthValue == month && zdt.year == year) {
                newsArray.add(item)
            }
        }
        newData = newsArray
    }
    return newData
}
