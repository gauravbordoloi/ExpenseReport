package me.gauravbordoloi.expensereport

import android.app.Application
import me.gauravbordoloi.expensereport.util.SharedPref

class App : Application() {

    companion object {

        private lateinit var pref: SharedPref

        fun pref(): SharedPref = pref

    }

    override fun onCreate() {
        super.onCreate()
        pref = SharedPref(applicationContext)
    }

}