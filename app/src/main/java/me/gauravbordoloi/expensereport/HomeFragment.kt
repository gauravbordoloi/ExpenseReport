package me.gauravbordoloi.expensereport

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_home.*
import me.gauravbordoloi.expensereport.transaction.TransactionUtil
import me.gauravbordoloi.expensereport.util.DeviceUtil
import java.text.NumberFormat
import java.util.*

class HomeFragment : Fragment() {

    private var isReloaded = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.logo)
        val size = DeviceUtil.dpToPx(context, 36)
        drawable?.setBounds(0, 0, size, size)
        titleView.setCompoundDrawables(drawable, null, null, null)

        drawUI(view)

        btnViewAll.setOnClickListener {
            findNavController().navigate(R.id.transactionsFragment)
        }

        btnViewTags.setOnClickListener {
            findNavController().navigate(R.id.tagsFragment)
        }

        btnUpdate.setOnClickListener {
            drawUI(it)
        }

    }

    private fun drawUI(view: View) {
        TransactionUtil.getTransactions(requireContext(), null) { list ->
            if (list.isNullOrEmpty()) {
                Snackbar.make(view, "Some unknown error occurred", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Retry") {
                        drawUI(view)
                    }.show()
            } else {
                var credit = 0.toDouble()
                var debit = 0.toDouble()
                for (transaction in list) {
                    if (transaction.type == "CREDITED") {
                        credit += transaction.amount
                    }
                    if (transaction.type == "DEBITED") {
                        debit += transaction.amount
                    }
                }
                val currencyFormatter = NumberFormat.getCurrencyInstance(Locale.getDefault())
                textTotalCredit.text = currencyFormatter.format(credit)
                textTotalDebit.text = currencyFormatter.format(debit)

                resetGraph()

                //pie chart
                val creditPercentage = ((credit/(credit + debit))*100).toFloat()
                val pieEntries = mutableListOf<PieEntry>()
                pieEntries.add(PieEntry(creditPercentage, "Credit"))
                pieEntries.add(PieEntry(100-creditPercentage, "Expense"))
                val set = PieDataSet(pieEntries, "")
                set.selectionShift = 0f
                set.setColors(Color.parseColor("#5fa30f"), Color.parseColor("#D7263D"))
                set.valueTextColor = Color.WHITE
                val pieData = PieData(set)
                pieData.setValueFormatter(PercentFormatter())
                pieChart.data = pieData
                pieChart.description.isEnabled = false
                pieChart.legend.isEnabled = true
                pieChart.legend.orientation = Legend.LegendOrientation.VERTICAL
                pieChart.legend.verticalAlignment = Legend.LegendVerticalAlignment.CENTER
                pieChart.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
                pieChart.legend.setDrawInside(false)
                pieChart.isDrawHoleEnabled = false
                pieChart.setDrawEntryLabels(false)
                pieChart.invalidate()

                // bar chart
                val barData = TransactionUtil.groupMonthly(list)
                val xAxis = barChart.xAxis
                xAxis.granularity = 1f
                xAxis.valueFormatter = object : ValueFormatter() {
                    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                        return barData.first[value.toInt()]
                    }
                }
                xAxis.setDrawGridLines(false)
                xAxis.setDrawAxisLine(false)
                xAxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE
                barChart.axisRight.setDrawGridLines(false)
                barChart.axisLeft.setDrawGridLines(false)
                barChart.axisRight.setDrawTopYLabelEntry(false)
                barChart.axisRight.setDrawAxisLine(false)
                barChart.axisRight.setDrawZeroLine(false)
                barChart.axisRight.setDrawLabels(false)
                barChart.axisLeft.setDrawAxisLine(false)
                barChart.data = barData.second
                barChart.description.isEnabled = false
                barChart.legend.isEnabled = false
                barChart.groupBars(-0.5f, 0.08f, 0.02f)
                barChart.invalidate()

            }

            // THIS BLOCK IS USED FOR RECTIFYING AN UI BUG OF PIE GRAPH OF "MPAndroidChart" LIBRARY. THIS CAN BE COMMENTED.
            if (!isReloaded) {
                drawUI(view)
                isReloaded = true
            }

        }

    }

    private fun resetGraph() {
        barChart.invalidate()
        barChart.clear()
        pieChart.invalidate()
        pieChart.clear()
    }

}