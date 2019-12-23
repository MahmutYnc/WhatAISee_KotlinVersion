package com.example.yazlab_13

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_selection_screen.*


class SelectionScreen : AppCompatActivity() {

    var imageuri: Uri? = null
    var imagedata = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selection_screen)
        supportActionBar?.hide()

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
        val bitmap = intent.getParcelableExtra("BitmapImage") as Bitmap

        imageView2.setImageURI(imageuri)

    }


}
