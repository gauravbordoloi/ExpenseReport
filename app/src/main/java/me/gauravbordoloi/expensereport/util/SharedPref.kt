package me.gauravbordoloi.expensereport.util

import android.content.Context

class SharedPref(context: Context) {

    private val LAST_UPDATE = "last_update"

    private val sharedPref =
        context.getSharedPreferences("me.gauravbordoloi.expensereport", Context.MODE_PRIVATE)
    private val editor = sharedPref.edit()

    fun clear() {
        editor.clear()
        editor.apply()
    }

    fun getLastUpdatedTime(): Long {
        return sharedPref.getLong(LAST_UPDATE, 0)
    }

    fun setLastUpdatedTime(value: Long) {
        editor.putLong(LAST_UPDATE, value)
        editor.commit()
    }

}