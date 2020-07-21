package me.gauravbordoloi.expensereport.util

import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtil {

    fun getFormattedTime(date: Long?, pattern: String): String {
        if (date == null) {
            return ""
        }
        val formatter = SimpleDateFormat("dd MMM, hh:mm a", Locale.ENGLISH)
        return formatter.format(Date(date))
    }

    fun getMonth(date: Long?): Int {
        if (date == null) {
            return 0
        }
        val formatter = SimpleDateFormat("MM", Locale.ENGLISH)
        val month = formatter.format(Date(date))
        return month.toInt()
    }

    fun getMonthInString(date: Long?): String {
        if (date == null) {
            return ""
        }
        val formatter = SimpleDateFormat("MMM", Locale.ENGLISH)
        return formatter.format(Date(date)).toString()
    }

    fun getDayInString(date: Long?): String {
        if (date == null) {
            return ""
        }
        val formatter = SimpleDateFormat("dd", Locale.ENGLISH)
        return formatter.format(Date(date)).toString()
    }

}