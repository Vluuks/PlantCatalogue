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

/*
    This activity is used to add a new entry to the database. Users can fill in the fields that are
    required of an entry, select an image and then confirm the contents to add it to the database,
    after which they will be returned to the MainActivity.
 */
class AddKnoepActivity : AppCompatActivity() {

    // Request codes for permissions and image selection
    val GALLERY_REQUEST_CODE = 184
    val PERMISSION_REQUEST_STORAGE = 123

    // Ref to the selected image's URI
    // (this is probably not the way to go but I can't think of something else for now)
    var selectedImage: Uri? = null

    /*
        Usual business of onCreate. Setting listeners here but defining contents elsewhere
        seems to be the most elegant and avoid bulk?
     */
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


    /*
        Obtains information from the fields and adds it to the database. If there are empty fields
        or missing permissions, the user will be notified.
     */
    fun addKnoep() {

        // Verify contents and permissions
        if(verify() && checkPermissions()) {

            // Create object and insert into database
            val aKnoep = Knoep(nameET.text.toString(), sizeET.text.toString().toInt(), typeET.text.toString(), "test", selectedImage)
            val databaseHelper = DatabaseHelper.getInstance(this)
            databaseHelper?.insert(aKnoep)

            // Close this activity
            finish()
        }
    }


    /*
        Checks for the necessary permissions, in this case reading of the external storage.
     */
    fun checkPermissions(): Boolean {

        // Permissions are missing
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_STORAGE)

            return false
        }

        return true

    }


    /*
        Callback from permission request.
    */
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        // Appears to be some kind of anonymous function like structure?
        when (requestCode) {
            PERMISSION_REQUEST_STORAGE -> {

                Log.d("Requesting", "asked" +  requestCode.toString())
                Log.d("Requesting", "got" + grantResults[0].toString())

                // If there is a code and it's the right one, continue with adding the entry
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Log.d("Requesting", "success")
                    addKnoep()

                }
                // Otherwise, stop this activity because a photo is required
                else {
                    Log.d("Requesting", "failed")
                    finish()
                }
                return
            }
        }
    }


    /*
        Checks whether all fields have been filled in and a photo is selected.
     */
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


    /*
        Creates an intent that opens the gallery or other photo selector app of the user's choice
        to pick a photo that will be added to the entry.
     */
    fun selectPhoto() {

        // Make intent and say that we want it to pick images
        var intent = Intent(Intent.ACTION_PICK)
        intent.setType("image/*")

        // Specifiy types of images
        var mimeTypes = arrayOf("image/jpeg", "image/png")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)

        // Launch intent with for result, then wait for callback
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }


    /*
        Callback for the photo select request, will be executed when the user returns from
        selecting a photo.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Initial check
        if (resultCode == Activity.RESULT_OK) {

            // Check if the right code was received (switch died in kotlin)
            if (requestCode == GALLERY_REQUEST_CODE) {

                // Hold a reference to this URI so we can add it to the entry later
                selectedImage = data?.data

                // Set image to the button so user can see what they picked
                selectKnoepButton.setImageURI(selectedImage)
            }
        }
    }
}
