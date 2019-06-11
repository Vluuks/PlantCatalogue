package com.vluuks.renske.knoepcatalogue

import android.net.Uri
import android.util.Log

/**
 * Created by Gebruiker on 11-6-2019.
 */

// the constructor word can be omitted if there are no annotations or modifiers on the arguments
class Knoep constructor(var name: String, var size: Int, var type: String, var uri: Uri?) {

//    private var name = ""
//    private var size = ""
//    private var type = ""

    init{
        Log.d("MAKING A KNOEP", "ECHT GOED")
    }

    override fun toString(): String {
        return "Knoep(name='$name', size=$size, type='$type', uri='$uri')"
    }

    // it does not seem to be strictly typed
    // val -> final, var -> regular variable
    // final object/instances cannot be reassigned but their properties can be modified





}