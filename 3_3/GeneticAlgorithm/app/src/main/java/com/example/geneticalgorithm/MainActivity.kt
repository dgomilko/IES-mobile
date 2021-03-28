package com.example.geneticalgorithm

import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var inputs = mutableListOf<EditText>()
    private lateinit var button: Button
    private lateinit var result: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val ids = listOf(R.id.a, R.id.b, R.id.c, R.id.d, R.id.res)
        inputs = ids.map { id -> findViewById<EditText>(id) }.toMutableList()
        button = findViewById(R.id.btn)
        result = findViewById(R.id.resultArea)

        button.setOnClickListener { handleBtnClick() }
    }

    private fun handleBtnClick() {
        var output = ""
        if (inputs.none { input -> input.text.isEmpty() }) {
            val args = inputs.map { input -> input.text.toString().toDouble() }.toMutableList()
            val expected = args.removeLast()
            val startTime = SystemClock.elapsedRealtime()
            val res = GeneticAlgo(10, args, expected).runAlgorithm()
            val endTime = SystemClock.elapsedRealtime()
            val elapsedMilliSeconds = endTime - startTime
            val elapsedSeconds = elapsedMilliSeconds / 1000.0
            output = "Result:\nX1 = ${res[0]},\nX2 = ${res[1]},\nX3 = ${res[2]},\nX4 = ${res[3]}\n\n" +
                    "(calculated in $elapsedSeconds seconds)"
        }
        else output = "Invalid input (some of the fields are empty)"
        result.text = output
    }
}