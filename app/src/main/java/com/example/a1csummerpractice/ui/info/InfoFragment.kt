package com.example.a1csummerpractice.ui.info

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.a1csummerpractice.databinding.FragmentInfoBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

class InfoFragment : Fragment() {

    private var _binding: FragmentInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentInfoBinding.inflate(inflater, container, false)

        //Firebase
        val remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 216000
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.fetchAndActivate().addOnCompleteListener { task->
            if (task.isSuccessful){
                binding.tvInfoLatestVer.text = String.format("Последняя версия: %s", remoteConfig.getString("server_app_version"))
            }
            else binding.tvInfoLatestVer.visibility = View.GONE
        }

        binding.tvInfoCurrVer.text = String.format("Текущая версия: %s",requireContext().packageManager.getPackageInfo(
            requireContext().packageName, 0
        ).versionName)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}