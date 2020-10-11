package com.example.diceroller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.roll_button).run { setOnClickListener {rollDice()} }
    }

    private fun rollDice() {
        findViewById<TextView>(R.id.roll_result_text).run { text = (Random().nextInt(6)+1).toString() }
    }
}