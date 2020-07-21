package me.gauravbordoloi.expensereport.tag

import android.content.Context
import android.graphics.Color
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import me.gauravbordoloi.expensereport.repository.db.TransactionDb
import me.gauravbordoloi.expensereport.transaction.Transaction
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import timber.log.Timber
import java.lang.Exception

object TagUtil {

    const val RED = ""

    const val PINK = "#F44336"

    const val PURPLE = "#F44336"

    const val BLUE = "#F44336"

    const val INDIGO = "#F44336"

    const val CYAN = "#F44336"

    const val TEAL = "#F44336"

    const val GREEN = "#F44336"

    const val YELLOW = "#F44336"

    const val ORANGE = "#F44336"

    const val BROWN = "#F44336"

    const val GRAY = "#F44336"

    val colors = arrayOf(
        "#F44336",
        "#E91E63",
        "#673AB7",
        "#673AB7",
        "#2196F3",
        "#00BCD4",
        "#009688",
        "#4CAF50",
        "#FFEB3B",
        "#FF9800",
        "#795548",
        "#607D8B"
    )

    fun getTags(context: Context, callback: (List<String>?) -> Unit) {
        val db = TransactionDb.getInstance(context).dao()
        doAsync {
            try {
                val tags = db.getAllTags()
                if (tags.isNullOrEmpty()) {
                    uiThread {
                        callback(null)
                    }
                } else {
                    val list = mutableListOf<String>()
                    for (tag in tags) {
                        if (!list.contains(tag)) {
                            list.add(tag)
                        }
                    }
                    uiThread {
                        callback(list)
                    }
                }
            } catch (e: Exception) {
                Timber.e(e)
                uiThread {
                    callback(null)
                }
            }
        }
    }

    fun getTransactions(context: Context, callback: (List<Transaction>?) -> Unit) {
        val db = TransactionDb.getInstance(context).dao()
        doAsync {
            try {
                val transactions = db.getTransactionsWithTag()
                uiThread {
                    callback(transactions)
                }
            } catch (e: Exception) {
                Timber.e(e)
                uiThread {
                    callback(null)
                }
            }
        }
    }

    fun groupTags(list: List<Transaction>): Pair<List<String>, BarData> {
        val creditEntry = mutableListOf<BarEntry>()
        val debitEntry = mutableListOf<BarEntry>()
        val creditHash = mutableMapOf<String, Double>()
        val debitHash = mutableMapOf<String, Double>()
        val tags = mutableListOf<String>()
        for (item in list) {
            if (!tags.contains(item.tag)) {
                tags.add(item.tag!!)
            }
            if (item.type == "CREDITED") {
                if (creditHash.containsKey(item.tag)) {
                    creditHash[item.tag!!] = creditHash[item.tag!!]!! + item.amount
                } else {
                    creditHash[item.tag!!] = item.amount
                }
            } else {
                if (debitHash.containsKey(item.tag!!)) {
                    debitHash[item.tag!!] = debitHash[item.tag!!]!! + item.amount
                } else {
                    debitHash[item.tag!!] = item.amount
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
        return Pair(tags, data)
    }

}