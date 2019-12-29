package com.example.yazlab_13

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_selection_screen.*


class SelectionScreen : AppCompatActivity() {

    var imageuri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selection_screen)
        supportActionBar?.hide()

        val Picture = getIntent().getStringExtra("picture")
        imageuri = Uri.parse(Picture)
        imageView2.setImageURI(imageuri)

        //button
        val segButton = findViewById<Button>(R.id.button)
        //handle button click
        segButton.setOnClickListener {
            //start activity intent
            val intent = Intent(this, Segmentation::class.java)
            intent.putExtra("picture", Picture)
            startActivity(intent)

        }

        //button2
        val scalingButton = findViewById<Button>(R.id.button2)
        //handle button click
        scalingButton.setOnClickListener {
            //start activity intent
            val intent = Intent(this, Scaling::class.java)
            intent.putExtra("picture", Picture)
            startActivity(intent)

        }

    }


}
