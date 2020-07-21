package me.gauravbordoloi.expensereport.transaction

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.item_transaction.view.*
import me.gauravbordoloi.expensereport.R
import me.gauravbordoloi.expensereport.repository.db.TransactionDb
import me.gauravbordoloi.expensereport.tag.TagDialog
import me.gauravbordoloi.expensereport.util.DateTimeUtil
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import timber.log.Timber
import java.lang.Exception
import java.text.NumberFormat
import java.util.*

class TransactionAdapter : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    private val transactions = arrayListOf<Transaction>()
    private val currencyFormatter = NumberFormat.getCurrencyInstance(Locale.getDefault())

    fun setTransactions(transactions: List<Transaction>?) {
        if (transactions.isNullOrEmpty()) {
            this.transactions.clear()
            notifyDataSetChanged()
            return
        }
        this.transactions.clear()
        this.transactions.addAll(transactions)
        notifyDataSetChanged()
    }

    fun addTransaction(transaction: Transaction) {
        transactions.add(transaction)
        notifyItemInserted(transactions.size - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = transactions.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaction = transactions[position]

        holder.transactionAddress.text = transaction.address
        holder.transactionAmount.text = if (transaction.type == "CREDITED") {
            "+ ${currencyFormatter.format(transaction.amount)}"
        } else {
            "- ${currencyFormatter.format(transaction.amount)}"
        }
        if (transaction.tag.isNullOrEmpty()) {
            holder.transactionTag.visibility = View.GONE
        } else {
            holder.transactionTag.visibility = View.VISIBLE
            holder.transactionTag.text = transaction.tag
            holder.transactionTag.setBackgroundColor(Color.parseColor(transaction.tagColor))
        }
        holder.transactionDate.text = DateTimeUtil.getFormattedTime(transaction.date, "") //TODO pattern

        holder.itemView.setOnClickListener {
            TagDialog(it.context, transaction.tag) { tag ->
                transaction.tag = tag
                doAsync {
                    try {
                        TransactionDb.getInstance(it.context).dao().setTransaction(transaction)
                        uiThread {
                            notifyItemChanged(position)
                        }
                    } catch (e: Exception) {
                        Timber.e(e)
                        Snackbar.make(it, "Some error occurred", Snackbar.LENGTH_SHORT).show()
                    }
                }
            }.show()
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val transactionAddress = view.transactionAddress
        val transactionTag = view.transactionTag
        val transactionAmount = view.transactionAmount
        val transactionDate = view.transactionDate

    }

}