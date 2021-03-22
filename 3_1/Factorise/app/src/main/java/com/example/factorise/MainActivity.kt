package com.example.factorise

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import java.lang.Math.ceil
import java.lang.Math.sqrt
import kotlin.math.pow

class MainActivity : AppCompatActivity() {
    private lateinit var input: EditText
    private lateinit var button: Button
    private lateinit var result: TextView
    private lateinit var iterations: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        input = findViewById(R.id.editTextNumber)
        button = findViewById(R.id.factoriseBtn)
        result = findViewById(R.id.textView)
        iterations = findViewById(R.id.textViewCounter)

        button.setOnClickListener { handleBtnClick() }
    }

    private fun handleBtnClick() {
        var output = ""
        val text = input.text.toString()
        output = if (text.isEmpty()) "Input area is empty"
        else {
            val n = text.toInt()

            val res = fermatFactorise(n)
            if (res !== null) {
                val iter = "Iterations count: ${res[2]}"
                iterations.text = iter
                "Result: ${res[0]}, ${res[1]}"
            } else "Invalid input data"
        }
        result.text = output
    }

    private fun fermatFactorise(n: Int): List<Int>? {
        if (n <= 0) return null
        if (n % 2 == 0) return listOf(2, n / 2, 1)

        var iterationsCount = 0
        var x = kotlin.math.ceil(kotlin.math.sqrt(n.toDouble()))
        var y = x.pow(2) - n

        while (kotlin.math.sqrt(y) != kotlin.math.ceil(kotlin.math.sqrt(y))) {
            iterationsCount++
            x++
            y = x.pow(2) - n
        }

        val ySqrt = kotlin.math.sqrt(y)
        return listOf((x + ySqrt).toInt(), (x - ySqrt).toInt(), iterationsCount)
    }
}