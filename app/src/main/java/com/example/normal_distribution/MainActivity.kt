package com.example.normal_distribution

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.exp
import kotlin.math.sqrt
import java.util.Random
import kotlin.math.round

class MainActivity : AppCompatActivity() {
    private val randomGenerator = Random()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val meanEditText = findViewById<EditText>(R.id.mean_val)
        val varianceEditText = findViewById<EditText>(R.id.variance_value)
        val resultTextView = findViewById<TextView>(R.id.random_number_result)
        val generateButton = findViewById<Button>(R.id.get_random_num)

        generateButton.setOnClickListener {
            try {
                val mean = meanEditText.text.toString().toDouble()
                val variance = varianceEditText.text.toString().toDouble()
                val stdDev = sqrt(variance)

                val normalRandom = randomGenerator.nextGaussian()
                val logNormalValue = exp(mean + stdDev * normalRandom)
                val roundedValue = round(logNormalValue * 10) / 10

                resultTextView.text = roundedValue.toString()
            } catch (e: Exception) {
                resultTextView.text = "Ошибка ввода"
            }
        }
    }
}