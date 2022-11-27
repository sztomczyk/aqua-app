package com.example.aquaapp

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.IdRes
import kotlin.concurrent.thread
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private var connected = false
    private var tempConditions = mapOf<String, Int>("gupik" to 20, "pawie_oczko" to 25, "black_molly" to 30)
    private var lightConditions = mapOf<String, Int>("gupik" to 600, "pawie_oczko" to 660, "black_molly" to 700)
    private var pickedFishType = "gupik"

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

            if (findViewById<View>(R.id.gupik_bar).visibility == View.VISIBLE) {
                simulation("gupik")
            } else if (findViewById<View>(R.id.pawie_oczko_bar).visibility == View.VISIBLE) {
                simulation("pawie_oczko")
            } else if (findViewById<View>(R.id.black_molly_bar).visibility == View.VISIBLE) {
                simulation("black_molly")
            }
        }

        val gupikLayout: LinearLayout = findViewById(R.id.gupik)
        gupikLayout.setOnClickListener {
            toggleBars(R.id.gupik_bar)
            pickedFishType = "gupik"
            if (connected) simulation("gupik")
        }

        val pawieOczkoLayout: LinearLayout = findViewById(R.id.pawie_oczko)
        pawieOczkoLayout.setOnClickListener {
            toggleBars(R.id.pawie_oczko_bar)
            pickedFishType = "pawie_oczko"
            if (connected) simulation("pawie_oczko")
        }

        val blackMollyLayout: LinearLayout = findViewById(R.id.black_molly)
        blackMollyLayout.setOnClickListener {
            toggleBars(R.id.black_molly_bar)
            pickedFishType = "black_molly"
            if (connected) simulation("black_molly")
        }

        val dopasujLayout: LinearLayout = findViewById(R.id.dopasuj)
        dopasujLayout.setOnClickListener {
            toggleBars(R.id.dopasuj_bar)
        }
    }

    fun toggleBars(id: Int) {
        findViewById<View>(R.id.gupik_bar).visibility = View.INVISIBLE
        findViewById<View>(R.id.pawie_oczko_bar).visibility = View.INVISIBLE
        findViewById<View>(R.id.black_molly_bar).visibility = View.INVISIBLE
        findViewById<View>(R.id.dopasuj_bar).visibility = View.INVISIBLE

        findViewById<View>(id).visibility = View.VISIBLE
    }

    fun simulation(fishType: String) {
        val tempValueLabel: TextView = findViewById(R.id.tempValueLabel)
        val lightValueLabel: TextView = findViewById(R.id.lightValueLabel)
        val lastTempLabel: TextView = findViewById(R.id.lastTempLabel)
        val lastLightLabel: TextView = findViewById(R.id.lastLightLabel)

        thread {
            while (connected && pickedFishType == fishType) {
                runOnUiThread {
                    val tempCondition = tempConditions[fishType]
                    val lightCondition = lightConditions[fishType]

                    lastTempLabel.text = "Ostatni pomiar: " + tempValueLabel.text
                    lastLightLabel.text = "Ostatni pomiar: " + lightValueLabel.text
                    tempValueLabel.text = tempCondition?.let {
                        Random.nextInt(it,
                            it + 3
                        ).toString()
                    } + " °C"
                    lightValueLabel.text = lightCondition?.let {
                        Random.nextInt(it,
                            it + 10
                        ).toString()
                    } + " lux"
                }
                Thread.sleep(3000)
            }
        }
    }
}