package com.example.potholedetector

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class Slide2GraphFragment : Fragment() {

    private lateinit var lineChart: LineChart
    private lateinit var tvPeak: TextView
    private lateinit var tvAvg: TextView
    private lateinit var tvThreshVal: TextView
    private lateinit var seekThreshold: SeekBar

    private val handler = Handler(Looper.getMainLooper())
    private var xIndex = 0f

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_slide2_graph, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lineChart    = view.findViewById(R.id.lineChart)
        tvPeak       = view.findViewById(R.id.tvPeak)
        tvAvg        = view.findViewById(R.id.tvAvg)
        tvThreshVal  = view.findViewById(R.id.tvThreshVal)
        seekThreshold= view.findViewById(R.id.seekThreshold)

        setupChart()

        seekThreshold.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar, progress: Int, fromUser: Boolean) {
                val value = progress + 8   // range 8..25
                AppState.threshold = value.toFloat()
                tvThreshVal.text = value.toString()
                updateThresholdLine()
            }
            override fun onStartTrackingTouch(sb: SeekBar) {}
            override fun onStopTrackingTouch(sb: SeekBar) {}
        })

        handler.post(object : Runnable {
            override fun run() {
                if (AppState.isDetecting) addChartPoint(AppState.currentAccel)
                tvPeak.text = "%.1f".format(AppState.peakForce)
                tvAvg.text  = "%.1f".format(AppState.avgForce)
                handler.postDelayed(this, 300)
            }
        })
    }

    private fun setupChart() {
        lineChart.apply {
            description.isEnabled = false
            legend.isEnabled      = false
            setTouchEnabled(false)
            setDrawGridBackground(false)
            axisRight.isEnabled   = false
            xAxis.isEnabled       = false
            axisLeft.apply {
                axisMinimum = 0f
                axisMaximum = 30f
                gridColor   = Color.parseColor("#22000000")
                textColor   = Color.parseColor("#888888")
            }
            data = LineData(makeDataSet())
        }
        updateThresholdLine()
    }

    private fun makeDataSet(): LineDataSet {
        return LineDataSet(mutableListOf(), "Accel").apply {
            color         = Color.parseColor("#185FA5")
            lineWidth     = 2f
            setDrawCircles(false)
            setDrawValues(false)
            mode          = LineDataSet.Mode.CUBIC_BEZIER
            setDrawFilled(true)
            fillColor     = Color.parseColor("#185FA5")
            fillAlpha     = 25
        }
    }

    private fun addChartPoint(value: Float) {
        val data = lineChart.data ?: return
        val set  = data.getDataSetByIndex(0) as LineDataSet
        set.addEntry(Entry(xIndex++, value))
        if (set.entryCount > 50) set.removeFirst()
        data.notifyDataChanged()
        lineChart.notifyDataSetChanged()
        lineChart.setVisibleXRangeMaximum(50f)
        lineChart.moveViewToX(xIndex)
    }

    private fun updateThresholdLine() {
        lineChart.axisLeft.removeAllLimitLines()
        val ll = LimitLine(AppState.threshold, "Threshold").apply {
            lineColor    = Color.parseColor("#E24B4A")
            lineWidth    = 1.5f
            enableDashedLine(10f, 8f, 0f)
            textColor    = Color.parseColor("#E24B4A")
            textSize     = 10f
        }
        lineChart.axisLeft.addLimitLine(ll)
        lineChart.invalidate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null)
    }
}