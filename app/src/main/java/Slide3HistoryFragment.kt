package com.example.potholedetector

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.fragment.app.Fragment

class Slide3HistoryFragment : Fragment() {

    private lateinit var tabLog: Button
    private lateinit var tabSettings: Button
    private lateinit var logPane: ScrollView
    private lateinit var settingsPane: ScrollView
    private lateinit var historyContainer: LinearLayout
    private lateinit var tvEmptyState: TextView
    private val handler = Handler(Looper.getMainLooper())
    private var lastCount = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_slide3_history, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        tabLog           = view.findViewById(R.id.tabLog)
        tabSettings      = view.findViewById(R.id.tabSettings)
        logPane          = view.findViewById(R.id.logPane)
        settingsPane     = view.findViewById(R.id.settingsPane)
        historyContainer = view.findViewById(R.id.historyContainer)
        tvEmptyState     = view.findViewById(R.id.tvEmptyState)

        tabLog.setOnClickListener {
            logPane.visibility      = View.VISIBLE
            settingsPane.visibility = View.GONE
            tabLog.setBackgroundColor(android.graphics.Color.WHITE)
            tabSettings.setBackgroundColor(android.graphics.Color.TRANSPARENT)
        }
        tabSettings.setOnClickListener {
            logPane.visibility      = View.GONE
            settingsPane.visibility = View.VISIBLE
            tabSettings.setBackgroundColor(android.graphics.Color.WHITE)
            tabLog.setBackgroundColor(android.graphics.Color.TRANSPARENT)
        }

        handler.post(object : Runnable {
            override fun run() {
                if (AppState.history.size != lastCount) {
                    lastCount = AppState.history.size
                    renderHistory()
                }
                handler.postDelayed(this, 500)
            }
        })
    }

    private fun renderHistory() {
        historyContainer.removeAllViews()
        if (AppState.history.isEmpty()) {
            tvEmptyState.visibility = View.VISIBLE
            return
        }
        tvEmptyState.visibility = View.GONE
        AppState.history.take(10).forEachIndexed { i, item ->
            val row = LayoutInflater.from(context)
                .inflate(R.layout.item_history_row, historyContainer, false)
            row.findViewById<TextView>(R.id.tvHistoryLabel).text =
                "Pothole #${AppState.history.size - i}"
            row.findViewById<TextView>(R.id.tvHistoryTime).text  = item.time
            row.findViewById<TextView>(R.id.tvHistoryForce).text =
                "%.1f m/s²".format(item.force)
            historyContainer.addView(row)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null)
    }
}