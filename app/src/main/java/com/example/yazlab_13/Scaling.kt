package com.example.yazlab_13

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_scaling.*

class Scaling : AppCompatActivity() {

    var imageuri: Uri? = null
    var abc = 0;

    private var mStorageRef: StorageReference? = null

    lateinit var storageReference: StorageReference
    lateinit var alertDialog: AlertDialog

    companion object{
         private val IMAGE_PICK_CODE = 1000;
    };


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scaling)
        supportActionBar?.hide()

//        val Picture = getIntent().getStringExtra("picture")
//        imageuri = Uri.parse(Picture)
//        imageView3.setImageURI(imageuri)

        //init
        alertDialog = SpotsDialog.Builder().setContext(this).build()
        storageReference = FirebaseStorage.getInstance().getReference("image/upload")


        mStorageRef = FirebaseStorage.getInstance().reference

        // Set a SeekBar change listener
        //val seekBar = findViewById<SeekBar>(R.id.seekBar)
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                // Display the current progress of SeekBar
                textView.text = "Kalite : $i"
                abc = i
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Do something
                //Toast.makeText(applicationContext,"start tracking",Toast.LENGTH_SHORT).show()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // Do something
                //Toast.makeText(applicationContext,"stop tracking",Toast.LENGTH_SHORT).show()

            }
        })

        uploadButton.setOnClickListener {
            imageView3.setImageDrawable(null)
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent,"Select Picture"), IMAGE_PICK_CODE)





        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE){
            alertDialog.show()
            println(imageuri)
            println(imageuri)
            println(imageuri)
            println(imageuri)

            //data!!.data = imageuri;
            val uploadTask = storageReference!!.putFile(data!!.data!!)
            val task = uploadTask.continueWithTask{
                task ->
                if(!task.isSuccessful){
                    Toast.makeText(this,"failed to upload",Toast.LENGTH_SHORT).show()
                }
                storageReference!!.downloadUrl
            }.addOnCompleteListener{ task ->
                if (task.isSuccessful){
                    val downloadUri = task.result
                    val url = downloadUri!!.toString().substring(0, downloadUri.toString().indexOf("&token"))
                    Log.d("DIRECTLINK", url)
                    alertDialog.dismiss()
                    Picasso.get().load(url).into(imageView3)
                }


            }
        }
    }


}

