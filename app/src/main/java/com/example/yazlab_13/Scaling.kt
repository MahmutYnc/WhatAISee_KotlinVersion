package com.example.yazlab_13

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.signature.ObjectKey
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_scaling.*


class Scaling : AppCompatActivity() {

    var imageuri: Uri? = null
    var abc = 1920;
    var url = "";
    private var mStorageRef: StorageReference? = null

    lateinit var storageReference: StorageReference
    lateinit var alertDialog: AlertDialog

    companion object {
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
                if(abc <= 30){
                    abc = 100
                }
                else if (abc>30 && abc <= 55){
                    abc = 400
                }
                else if (abc>55 && abc <= 80){
                    abc = 720
                }
                else if (abc > 80){
                    abc = 1920
                }

            }
        })

        uploadButton.setOnClickListener {
            imageView3.setImageDrawable(null)
            url = "";
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_PICK_CODE)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        url = ""
        //Thread(Runnable { Glide.get(this@Scaling).clearDiskCache() }).start()
        imageView3.setImageDrawable(null)
        if (requestCode == IMAGE_PICK_CODE) {

            alertDialog.show()
            println(imageuri)
            println(imageuri)
            println(imageuri)
            println(imageuri)


            //data!!.data = imageuri;
            val uploadTask = storageReference!!.putFile(data!!.data!!)
            val task = uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    Toast.makeText(this, "failed to upload", Toast.LENGTH_SHORT).show()
                }
                storageReference!!.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val downloadUri = task.result
                    Log.d("download", downloadUri.toString())
                    url = downloadUri!!.toString().substring(
                        0,
                        downloadUri.toString().indexOf("upload")
                    ) + "upload@s_"+abc+"?alt=media"
                    Log.d("DIRECTLINK", url)
                    alertDialog.dismiss()
                    Toast.makeText(this, url, Toast.LENGTH_SHORT).show()
                    val randomValue =Math.random()

                    Glide.with(this)
                        .load(url)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .fitCenter()
                        .signature(ObjectKey(randomValue))
                        .into(imageView3);
                }
            }
        }
    }



}




