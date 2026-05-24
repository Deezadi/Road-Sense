
    package com.example.potholedetector

    import android.os.Bundle
    import android.os.Handler
    import android.os.Looper
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.Button
    import android.widget.LinearLayout
    import android.widget.TextView
    import androidx.fragment.app.Fragment

    class Slide1DetectionFragment : Fragment() {

        private lateinit var alertBanner: LinearLayout
        private lateinit var tvAlertForce: TextView
        private lateinit var tvStatusCircle: TextView
        private lateinit var tvStatusLabel: TextView
        private lateinit var tvStatusSub: TextView
        private lateinit var tvAccelVal: TextView
        private lateinit var tvCountVal: TextView
        private lateinit var btnStart: Button
        private lateinit var btnStop: Button
        private lateinit var btnReset: Button

        private val handler = Handler(Looper.getMainLooper())

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View = inflater.inflate(R.layout.fragment_slide1_detection, container, false)

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            alertBanner   = view.findViewById(R.id.alertBanner)
            tvAlertForce  = view.findViewById(R.id.tvAlertForce)
            tvStatusCircle= view.findViewById(R.id.tvStatusCircle)
            tvStatusLabel = view.findViewById(R.id.tvStatusLabel)
            tvStatusSub   = view.findViewById(R.id.tvStatusSub)
            tvAccelVal    = view.findViewById(R.id.tvAccelVal)
            tvCountVal    = view.findViewById(R.id.tvCountVal)
            btnStart      = view.findViewById(R.id.btnStart)
            btnStop       = view.findViewById(R.id.btnStop)
            btnReset      = view.findViewById(R.id.btnReset)

            btnStart.setOnClickListener { AppState.startDetection() }
            btnStop.setOnClickListener  { AppState.stopDetection()  }
            btnReset.setOnClickListener { AppState.reset()          }

            // Observe shared state every 300ms
            handler.post(object : Runnable {
                override fun run() {
                    refreshUI()
                    handler.postDelayed(this, 300)
                }
            })
        }

        private fun refreshUI() {
            tvAccelVal.text    = "%.1f m/s²".format(AppState.currentAccel)
            tvCountVal.text    = AppState.potholeCount.toString()
            btnStart.isEnabled = !AppState.isDetecting
            btnStop.isEnabled  = AppState.isDetecting

            when (AppState.statusState) {
                "idle" -> {
                    tvStatusCircle.text = "○"
                    tvStatusCircle.setBackgroundResource(R.drawable.bg_circle_gray)
                    tvStatusLabel.text  = "Idle"
                    tvStatusSub.text    = "Press start to begin"
                    alertBanner.visibility = View.GONE
                }
                "detecting" -> {
                    tvStatusCircle.text = "●"
                    tvStatusCircle.setBackgroundResource(R.drawable.bg_circle_gray)
                    tvStatusLabel.text  = "Detecting"
                    tvStatusSub.text    = "Monitoring road surface..."
                    alertBanner.visibility = View.GONE
                }
                "pothole" -> {
                    tvStatusCircle.text = "!"
                    tvStatusCircle.setBackgroundResource(R.drawable.bg_circle_red)
                    tvStatusLabel.text  = "Pothole!"
                    tvAlertForce.text   = "Force: %.1f m/s²".format(AppState.lastForce)
                    alertBanner.visibility = View.VISIBLE
                }
            }
        }

        override fun onDestroyView() {
            super.onDestroyView()
            handler.removeCallbacksAndMessages(null)
        }
    }
