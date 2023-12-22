package com.bignerdranch.android.bitebalance

import android.app.Application
import androidx.room.Room

class MyApplication : Application() {

    companion object {
        var db: AppDatabase? = null
    }

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "recipes-database"
        ).build()
    }
}
