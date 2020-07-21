package me.gauravbordoloi.expensereport.tag

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.mindorks.Screenshot
import com.mindorks.properties.Quality
import kotlinx.android.synthetic.main.fragment_tags.*
import me.gauravbordoloi.expensereport.R
import me.gauravbordoloi.expensereport.util.DeviceUtil
import me.gauravbordoloi.expensereport.util.SimpleItemDecoration

class TagsFragment : Fragment() {

    private lateinit var adapter: TagAdapter
    private var isGraph = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tags, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.setHasFixedSize(true)
        //recyclerView.addItemDecoration(SimpleItemDecoration(requireContext(), 16))
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        adapter = TagAdapter()
        recyclerView.adapter = adapter

        drawUI(view)

        btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        btnViewType.setOnClickListener {
            if (isGraph) {
                btnViewType.setImageResource(R.drawable.ic_chart)
                recyclerView.visibility = View.VISIBLE
                barChart.visibility = View.GONE
                btnScreenShot.visibility = View.GONE
            } else {
                btnViewType.setImageResource(R.drawable.ic_list)
                recyclerView.visibility = View.GONE
                barChart.visibility = View.VISIBLE
                btnScreenShot.visibility = View.VISIBLE
            }
            isGraph = !isGraph
            drawUI(it)
        }

        btnScreenShot.setOnClickListener {
            askPermission(it)
        }

    }

    private fun askPermission(view: View) {
        Dexter.withActivity(requireActivity())
            .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    val bitmap = Screenshot.with(requireActivity())
                        .setView(barChart)
                        .setQuality(Quality.HIGH)
                        .getScreenshot()
                    DeviceUtil.saveImage(bitmap, requireContext())
                    Snackbar.make(
                        view,
                        "Report saved successfully to internal gallery.",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    Snackbar.make(
                        view,
                        "WRITE STORAGE permission is required to save the graph",
                        Snackbar.LENGTH_LONG
                    ).setAction("Retry") {
                        askPermission(view)
                    }.show()
                }
            }).check()
    }

    private fun drawUI(view: View) {
        if (isGraph) {
            TagUtil.getTransactions(requireContext()) { list ->
                if (list.isNullOrEmpty()) {
                    Snackbar.make(view, "No tags added yet", Snackbar.LENGTH_INDEFINITE).show()
                } else {
                    val data = TagUtil.groupTags(list)
                    resetGraph()

                    val xAxis = barChart.xAxis
                    xAxis.granularity = 1f
                    xAxis.valueFormatter = object : ValueFormatter() {
                        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                            return data.first[value.toInt()]
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
                    barChart.data = data.second
                    barChart.description.isEnabled = false
                    barChart.legend.isEnabled = false
                    barChart.groupBars(-0.5f, 0.08f, 0.02f)
                    barChart.invalidate()
                }
            }
        } else {
            TagUtil.getTags(requireContext()) { list ->
                if (list.isNullOrEmpty()) {
                    Snackbar.make(view, "No tags added yet", Snackbar.LENGTH_INDEFINITE).show()
                } else {
                    adapter.setTags(list)
                }
            }
        }
    }

    private fun resetGraph() {
        barChart.invalidate()
        barChart.clear()
    }

}