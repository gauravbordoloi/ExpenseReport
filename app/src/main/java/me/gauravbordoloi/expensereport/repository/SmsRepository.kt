package me.gauravbordoloi.expensereport.repository

import android.content.Context
import android.provider.Telephony
import me.gauravbordoloi.expensereport.transaction.Transaction
import java.util.regex.Pattern

object SmsRepository {

    private val PATTERN =
        Pattern.compile("(?i)(?:(?:RS|INR|MRP)\\.?\\s?)(\\d+(:?,\\d+)?(,\\d+)?(\\.\\d{1,2})?)")

    private val credited = arrayOf("credited", "cr", "credit")

    private val debited = arrayOf("debit", "debited", "purchasing", "purchase", "dr", "withdrawn")

    // message type -> inbox = 1,
    fun getTransactions(context: Context): List<Transaction>? {
        val cursor = context.contentResolver.query(
            Telephony.Sms.CONTENT_URI,
            arrayOf("_id", "address", "date", "body"),
            "type=1",
            null,
            "date ASC"
        )
        return if (cursor != null) {
            val list = mutableListOf<Transaction>()
            while (cursor.moveToNext()) {
                val body = cursor.getString(cursor.getColumnIndex("body"))
                val matcher = PATTERN.matcher(body)
                if (check(body, credited) && matcher.find()) {
                    val amount = matcher.group(1)?.toDoubleOrNull() ?: continue
                    val sms =
                        Transaction(
                            cursor.getInt(cursor.getColumnIndex("_id")),
                            cursor.getString(cursor.getColumnIndex("address")),
                            cursor.getLong(cursor.getColumnIndex("date")),
                            amount,
                            "CREDITED"
                        )
                    list.add(sms)
                }
                if (check(body, debited) && matcher.find()) {
                    val amount = matcher.group(1)?.toDoubleOrNull() ?: continue
                    val sms =
                        Transaction(
                            cursor.getInt(cursor.getColumnIndex("_id")),
                            cursor.getString(cursor.getColumnIndex("address")),
                            cursor.getLong(cursor.getColumnIndex("date")),
                            amount,
                            "DEBITED"
                        )
                    list.add(sms)
                }
            }
            cursor.close()
            list
        } else {
            null
        }
    }

    private fun check(text: String, arr: Array<String>): Boolean {
        for (a in arr) {
            if (text.contains(a, true)) {
                return true
            }
        }
        return false
    }

}