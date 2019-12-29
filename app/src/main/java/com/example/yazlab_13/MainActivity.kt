package com.example.yazlab_13

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private val PERMISSION_CODE = 1000
    private val IMAGE_CAPTURE_CODE = 1001
    var image_uri: Uri? = null



    var i = 0
    var Picture = "";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()



        //BUTTON CLICK
        img_pick_btn.setOnClickListener {
            //check runtime permission
            if (VERSION.SDK_INT >= VERSION_CODES.M){
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED){
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE)
                }
                else{
                    //permission already granted
                    pickImageFromGallery()
                }
            }
            else{
                //system OS is < Marshmallow
                pickImageFromGallery()
            }




        }

        //button click
        takePhoto.setOnClickListener {
            //if system os is Marshmallow or Above, we need to request runtime permission
            if (VERSION.SDK_INT >= VERSION_CODES.M){
                if (checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED){
                    //permission was not enabled
                    val permission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    //show popup to request permission
                    requestPermissions(permission, PERMISSION_CODE)
                }
                else{
                    //permission already granted
                    openCamera()
                }
            }
            else{
                //system os is < marshmallow
                openCamera()
            }
        }
        /*
        //button
        val mStartActBtn = findViewById<Button>(R.id.next)
        //handle button click
        mStartActBtn.setOnClickListener {
            //start activity intent
            startActivity(Intent(this@MainActivity, SelectionScreen::class.java))
        }
        */
    }

    private fun pickImageFromGallery() {
        i=0
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000
        //Permission code
        private val PERMISSION_CODE = 1001
    }

    //handle requested permission result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.size >0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup granted
                    pickImageFromGallery()
                    //permission from popup was granted
                    openCamera()
                }
                else{
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }

    private fun openCamera() {
        i = 1
        var values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        //camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)

    }


    //handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            imageView.setImageURI(data?.data)


            val selectedImage = data?.data
            val filepath = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = contentResolver.query(selectedImage,filepath,null,null,null)
            cursor.moveToFirst()
            val Index = cursor.getColumnIndex(filepath[0])
            Picture = cursor.getString(Index)
            cursor.close()

            val intent2 = Intent(this, SelectionScreen::class.java)
            intent2.putExtra("picture", Picture)
            startActivity(intent2)

        }
        //called when image was captured from camera intent
        if (resultCode == Activity.RESULT_OK && i == 1){
            //set image captured to image view
            imageView.setImageURI(image_uri)

            val selectedImage = image_uri
            val filepath = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = contentResolver.query(selectedImage,filepath,null,null,null)
            cursor.moveToFirst()
            val Index = cursor.getColumnIndex(filepath[0])
            val Picture = cursor.getString(Index)
            cursor.close()

            val intent2 = Intent(this, SelectionScreen::class.java)
            intent2.putExtra("picture", Picture)
            startActivity(intent2)
        }

    }


}
