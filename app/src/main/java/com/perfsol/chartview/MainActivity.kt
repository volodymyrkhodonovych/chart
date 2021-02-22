package com.perfsol.chartview

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val progressView = findViewById<CircularProgressView>(R.id.progressView)
        progressView.setProgress(50f)
        val chartView = findViewById<CircularChartView>(R.id.chartView)
        chartView.setList(
            listOf(
                Pair(25f, Color.parseColor("#f44336")),
                Pair(25f, Color.parseColor("#e91e63")),
                Pair(25f, Color.parseColor("#9c27b0")),
                Pair(25f, Color.parseColor("#3f51b5"))
            )
        )
    }
}