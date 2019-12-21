package com.example.yazlab_13

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class SelectionScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selection_screen)

        //button
        val segButton = findViewById<Button>(R.id.button)
        //handle button click
        segButton.setOnClickListener {
            //start activity intent
            startActivity(Intent(this, Segmentation::class.java))
        }

        //button2
        val scalingButton = findViewById<Button>(R.id.button2)
        //handle button click
        scalingButton.setOnClickListener {
            //start activity intent
            startActivity(Intent(this, Scaling::class.java))
        }
    }
}
