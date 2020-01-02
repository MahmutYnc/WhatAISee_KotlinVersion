package com.example.yazlab_13

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.label.FirebaseVisionCloudImageLabelerOptions
import kotlinx.android.synthetic.main.activity_segmentation.*

class Segmentation : AppCompatActivity() {
    var imageuri: Uri? = null
    val builder = StringBuilder()

    companion object{
        private val IMAGE_PICK_CODE = 1000;
    };
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_segmentation)
        supportActionBar?.hide()

        //buradan çalışıyor


        //button2
        val button = findViewById<Button>(R.id.button)
        //handle button click
        button.setOnClickListener {

            textView2.setText(null)

            //start activity intent
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent,"Select Picture"), IMAGE_PICK_CODE)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_PICK_CODE){

            imageuri = data?.data
            imageView4.setImageURI(data?.data)
            var bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageuri);
            runImageLabeling(bitmap)

        }
    }


    @SuppressLint("ResourceType")
    private fun runImageLabeling(bitmap: Bitmap) {
        //Create a FirebaseVisionImage
        val image = FirebaseVisionImage.fromBitmap(bitmap)
        // Or, to set the minimum confidence required:
        val options = FirebaseVisionCloudImageLabelerOptions.Builder()
            .setConfidenceThreshold(0.7f)
            .build()
        val labeler = FirebaseVision.getInstance().getCloudImageLabeler(options)

        labeler.processImage(image)
            .addOnSuccessListener { labels ->
                // Task completed successfully
                // ...
                builder.clear()
                for (label in labels) {
                    val text = label.text
                    val entityId = label.entityId
                    val confidence = label.confidence
                    builder.append(text)
                        .append(" -- Confidence: ")
                        .append(confidence)
                        .append("\n")
                }

                textView2.setText(builder)
                //Toast.makeText(baseContext, builder, Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener { e ->
                // Task failed with an exception
                // ...
                Toast.makeText(baseContext, "Üzgün olmakla birlikte, bir şeyler ters gitti!", Toast.LENGTH_SHORT).show()
            }


    }


}
