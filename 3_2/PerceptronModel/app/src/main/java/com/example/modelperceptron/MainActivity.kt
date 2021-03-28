package com.example.modelperceptron

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.*

class MainActivity : AppCompatActivity() {
    private lateinit var submitBtn: Button
    private lateinit var resultArea: TextView

    private var timeDeadline = 0.5
    private var iterationsDeadline = 100
    private var learningRate = 0.001
    private lateinit var perceptron: Perceptron

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val points = listOf(
                listOf(0, 6),
                listOf(1, 5),
                listOf(3, 3),
                listOf(2, 4)
        )
        perceptron = Perceptron(points, 4, 2)

        val spinnersIds = listOf(R.id.spinnerLearningRate, R.id.spinnerIterDeadline, R.id.spinnerTimeDeadline)
        val lrSpinnerClb: SpinnerClb = { parent, _, p, _ ->
            learningRate = parent!!.getItemAtPosition(p).toString().toDouble() }
        val iterSpinnerClb: SpinnerClb = { parent, _, p, _ ->
            iterationsDeadline = parent!!.getItemAtPosition(p).toString().toInt() }
        val timeSpinnerClb: SpinnerClb = { parent, _, p, _ ->
            timeDeadline = parent!!.getItemAtPosition(p).toString().toDouble() }
        val spinnersData = listOf(
                Pair(R.array.learning_rates, lrSpinnerClb),
                Pair(R.array.iteration_deadlines, iterSpinnerClb),
                Pair(R.array.time_deadlines, timeSpinnerClb)
        )
        spinnersIds.mapIndexed { i, id ->
            setUpSpinner(id, spinnersData[i].first, spinnersData[i].second)
        }

        submitBtn = findViewById(R.id.btn)
        resultArea = findViewById(R.id.resultArea)
        submitBtn.setOnClickListener { handleSubmitBtn() }
    }

    fun onRadioButtonClicked(view: View) {
        val checked = (view as RadioButton).isChecked
        if (view.getId() == R.id.radioTime && checked) perceptron.setDeadline(timeDeadline)
        if (view.getId() == R.id.radioIterations && checked) perceptron.setDeadline(iterationsDeadline)
    }

    private fun setUpSpinner(id: Int, res: Int, clb: SpinnerClb) {
        val spinner = findViewById<Spinner>(id)
        val resource = resources.getStringArray(res)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, resource)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                clb(parent, view, position, id)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }
    }

    private fun handleSubmitBtn() {
        perceptron.setLearningRate(learningRate)
        val startTime = SystemClock.elapsedRealtime()
        val result = perceptron.train()
        val endTime = SystemClock.elapsedRealtime()
        val elapsedMilliSeconds = endTime - startTime
        val elapsedSeconds = elapsedMilliSeconds / 1000.0
        val isOptimal = result.second
        val resultStr = "W1 = ${result.first[0]}\nW2 = ${result.first[1]}\n\n(calculated in $elapsedSeconds seconds)"
        val prefix = if (isOptimal) "Final result:\n" else "Best fit (missed deadline):\n"
        resultArea.text = prefix + resultStr
    }
}

private typealias SpinnerClb = (parent: AdapterView<*>?, view: View?, pos: Int, id: Long) -> Unit