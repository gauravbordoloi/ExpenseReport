package me.gauravbordoloi.expensereport.transaction

import android.content.Context
import android.graphics.Color
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import me.gauravbordoloi.expensereport.App
import me.gauravbordoloi.expensereport.repository.SmsRepository
import me.gauravbordoloi.expensereport.repository.db.TransactionDb
import me.gauravbordoloi.expensereport.util.DateTimeUtil
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import timber.log.Timber
import java.lang.Exception

object TransactionUtil {

    fun getTransactions(context: Context, filter: String?, callback: (List<Transaction>?) -> Unit) {
        val db = TransactionDb.getInstance(context).dao()
        doAsync {
            try {
                val transactions = SmsRepository.getTransactions(context)
                db.setTransactions(transactions)
                App.pref().setLastUpdatedTime(System.currentTimeMillis())
                val ts = if (filter.isNullOrEmpty()) {
                    db.getTransactions()
                } else {
                    db.getFilteredTransactions(filter)
                }
                uiThread {
                    callback(ts)
                }
            } catch (e: Exception) {
                Timber.e(e)
                uiThread {
                    callback(null)
                }
            }
        }
    }

    fun groupMonthly(list: List<Transaction>): Pair<List<String>, BarData> {
        val creditEntry = mutableListOf<BarEntry>()
        val debitEntry = mutableListOf<BarEntry>()
        val creditHash = mutableMapOf<String, Double>()
        val debitHash = mutableMapOf<String, Double>()
        val months = mutableListOf<String>()
        for (item in list) {
            val month = DateTimeUtil.getMonthInString(item.date)
            if (months.isEmpty()) {
                months.add(month)
            } else {
                if (months[months.size - 1] != month) {
                    months.add(month)
                }
            }
            if (item.type == "CREDITED") {
                if (creditHash.containsKey(month)) {
                    creditHash[month] = creditHash[month]!! + item.amount
                } else {
                    creditHash[month] = item.amount
                }
            } else {
                if (debitHash.containsKey(month)) {
                    debitHash[month] = debitHash[month]!! + item.amount
                } else {
                    debitHash[month] = item.amount
                }
            }
        }
        var i = 0f
        for (key in creditHash.keys) {
            creditEntry.add(BarEntry(i, creditHash[key]!!.toFloat()))
            i += 1f
        }
        var j = 0f
        for (key in debitHash.keys) {
            debitEntry.add(BarEntry(j, debitHash[key]!!.toFloat()))
            j += 1f
        }
        val creditSet = BarDataSet(creditEntry, "Credit")
        creditSet.color = Color.parseColor("#5fa30f")
        val debitSet = BarDataSet(debitEntry, "Debit")
        debitSet.color = Color.parseColor("#D7263D")
        val dataSet = listOf(creditSet, debitSet)
        val barWidth = 0.45f // x2 dataset
        val data = BarData(dataSet)
        data.barWidth = barWidth
        return Pair(months, data)
    }

    fun groupDaily(list: List<Transaction>): Pair<List<String>, BarData> {
        val creditEntry = mutableListOf<BarEntry>()
        val debitEntry = mutableListOf<BarEntry>()
        val creditHash = mutableMapOf<String, Double>()
        val debitHash = mutableMapOf<String, Double>()
        val days = mutableListOf<String>()
        for (item in list) {
            val day = DateTimeUtil.getDayInString(item.date)
            if (days.isEmpty()) {
                days.add(day)
            } else {
                if (days[days.size - 1] != day) {
                    days.add(day)
                }
            }
            if (item.type == "CREDITED") {
                if (creditHash.containsKey(day)) {
                    creditHash[day] = creditHash[day]!! + item.amount
                } else {
                    creditHash[day] = item.amount
                }
            } else {
                if (debitHash.containsKey(day)) {
                    debitHash[day] = debitHash[day]!! + item.amount
                } else {
                    debitHash[day] = item.amount
                }
            }
        }
        var i = 0f
        for (key in creditHash.keys) {
            creditEntry.add(BarEntry(i, creditHash[key]!!.toFloat()))
            i += 1f
        }
        var j = 0f
        for (key in debitHash.keys) {
            debitEntry.add(BarEntry(j, debitHash[key]!!.toFloat()))
            j += 1f
        }
        val creditSet = BarDataSet(creditEntry, "Credit")
        creditSet.color = Color.parseColor("#5fa30f")
        val debitSet = BarDataSet(debitEntry, "Debit")
        debitSet.color = Color.parseColor("#D7263D")
        val dataSet = listOf(creditSet, debitSet)
        val barWidth = 0.45f
        val data = BarData(dataSet)
        data.barWidth = barWidth
        return Pair(days, data)
    }

}