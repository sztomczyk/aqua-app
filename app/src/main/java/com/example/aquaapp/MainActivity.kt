package com.example.aquaapp

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import kotlin.concurrent.thread
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private var connected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val connectButton: Button = findViewById(R.id.connectButton)
        val connectionStatusValueLabel: TextView = findViewById(R.id.connectionStatusValueLabel)
        connectButton.setOnClickListener {
            connectButton.text = if (connected) "Połącz" else "Rozłącz"
            connectionStatusValueLabel.text = if (connected) "Niepołączony" else "Połączony"
            connectionStatusValueLabel.setTextColor(if (connected) Color.RED else Color.GREEN)
            connected = !connected
            simulation()
        }
    }

    fun simulation() {
        val tempValueLabel: TextView = findViewById(R.id.tempValueLabel)
        val lightValueLabel: TextView = findViewById(R.id.lightValueLabel)
        val lastTempLabel: TextView = findViewById(R.id.lastTempLabel)
        val lastLightLabel: TextView = findViewById(R.id.lastLightLabel)

        thread {
            while (connected) {
                runOnUiThread {
                    lastTempLabel.text = "Ostatni pomiar: " + tempValueLabel.text
                    lastLightLabel.text = "Ostatni pomiar: " + lightValueLabel.text
                    tempValueLabel.text = Random.nextInt(27, 30).toString() + " °C"
                    lightValueLabel.text = Random.nextInt(680, 720).toString() + " lux"
                }
                Thread.sleep(3000)
            }
        }
    }
}