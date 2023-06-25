package com.example.a1csummerpractice.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.a1csummerpractice.domain.NewsDao
import com.example.a1csummerpractice.domain.newsdt.NewsItemData

@Database(entities = [NewsItemData::class], version = 6)
abstract class NewsDatabase: RoomDatabase() {
    abstract fun dao(): NewsDao

    companion object{
        @Volatile
        private var INSTANCE: NewsDatabase? = null

        fun getDatabase(context: Context): NewsDatabase{
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NewsDatabase::class.java,
                    "news_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
