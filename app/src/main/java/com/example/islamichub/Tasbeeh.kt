package com.example.islamichub


import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Tasbeeh : AppCompatActivity() {

    private var count = 0
    private lateinit var incrementButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasbeeh)


        incrementButton = findViewById(R.id.incrementButton)
        val resetButton: Button = findViewById(R.id.resetButton)

        incrementButton.setOnClickListener {
            count++
            updateCounter()
        }

        resetButton.setOnClickListener {
            count = 0
            updateCounter()
        }

        updateIncrementButton()
    }

    private fun updateCounter() {
//        counterTextView.text = count.toString()
        updateIncrementButton()
    }

    private fun updateIncrementButton() {
        incrementButton.text = "$count"
    }
}
