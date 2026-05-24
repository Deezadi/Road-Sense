package com.example.potholedetector

import android.content.Context
import android.hardware.SensorManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        // Init shared sensor state
//        val sm = getSystemService(Context.SENSOR_SERVICE) as SensorManager
//        AppState.init(sm)

        // Setup ViewPager2 with 3 slides
        val pager = findViewById<ViewPager2>(R.id.viewPager)
        pager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = 3
            override fun createFragment(position: Int): Fragment = when (position) {
                0 -> Slide1DetectionFragment()
                1 -> Slide2GraphFragment()
                else -> Slide3HistoryFragment()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (AppState.isDetecting) AppState.stopDetection()
    }9
}