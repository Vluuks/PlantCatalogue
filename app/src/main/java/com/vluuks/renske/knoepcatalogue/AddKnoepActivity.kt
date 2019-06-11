package com.vluuks.renske.knoepcatalogue

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_add_knoep.*
import knoeps.*


class AddKnoepActivity : AppCompatActivity() {

    val GALLERY_REQUEST_CODE = 184
    val PERMISSION_REQUEST_STORAGE = 123

    // ref to image selected, this is probably not the way to go but I can't think of something else
    var selectedImage: Uri? = null


    // oncreate etc as usual
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_knoep)

        // attach to the confirm button, check for input and then make an object + return
        confirmButton.setOnClickListener {
            addKnoep()
        }

        selectKnoepButton.setOnClickListener {
            selectPhoto()
        }
    }

    fun addKnoep() {

        // if there is content
        if(verify() && checkPermissions()) {

            Log.d(TAG, selectedImage.toString())
            val aKnoep = Knoep(nameET.text.toString(), sizeET.text.toString().toInt(), typeET.text.toString(), selectedImage)
            // why you cannot go from editable to int as well? now we need an ugly double cast
            knoepList.add(aKnoep)

            Log.d(TAG, knoepList.toString())

            // close this activity
            finish()
        }
    }

    fun checkPermissions(): Boolean {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            Log.d("Requesting", "no permissions")

            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_STORAGE)

            return false

        }
        Log.d("Requesting", "yes permissions")

        return true

    }


    // Callback from permission request
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_STORAGE -> {

                Log.d("Requesting", "asked" +  requestCode.toString())
                Log.d("Requesting", "got" + grantResults[0].toString())

                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Log.d("Requesting", "success")
                    addKnoep()

                } else {
                    Log.d("Requesting", "failed")
                    finish()
                }
                return
            }

        // Add other 'when' lines to check for other
        // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }


    // checks the contents of the edittexts
    fun verify(): Boolean {

        if(nameET.text.toString().trim().isEmpty() ||
           typeET.text.toString().trim().isEmpty() ||
           sizeET.text.toString().trim().isEmpty() ||
           selectedImage == null ) {

            Toast.makeText(applicationContext,"Please fill in all fields and pick a photo!", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    fun selectPhoto() {

        // make intent and say that we want it to pick images
        var intent = Intent(Intent.ACTION_PICK)
        intent.setType("image/*")

        // specifiy types of images
        var mimeTypes = arrayOf("image/jpeg", "image/png")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)

        // launch intent with for result, ensuring callback
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }


    // callback function when image is picked
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // initial check
        if (resultCode == Activity.RESULT_OK) {

            // check if the right code was received (switch died in kotlin)
            if (requestCode == GALLERY_REQUEST_CODE) {

                // not 100% on the syntax
                selectedImage = data?.data
                selectKnoepButton.setImageURI(selectedImage)
                Log.d(TAG, selectedImage.toString())


            }
        }
    }
}
