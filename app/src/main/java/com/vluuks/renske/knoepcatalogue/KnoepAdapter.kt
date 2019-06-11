package com.vluuks.renske.knoepcatalogue

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import knoeps.*

/*
 * Created by Gebruiker on 11-6-2019.
 */
class KnoepAdapter (var aContext: Context, var resourceID: Int, var currentKnoepList: ArrayList<Knoep>) : ArrayAdapter<Knoep>(aContext, resourceID, currentKnoepList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        Log.d(TAG, "adapter called" + currentKnoepList.get(position).uri.toString())

        // todo: make recyclerview later
        val rowView = LayoutInflater.from(context).inflate(resourceID, parent, false)

        rowView.findViewById<TextView>(R.id.knoepName).setText(currentKnoepList.get(position).name)
        rowView.findViewById<TextView>(R.id.knoepType).setText(currentKnoepList.get(position).type)
//        rowView.findViewById<ImageView>(R.id.knoepPhoto).setImageURI()

        val uri = currentKnoepList.get(position).uri
        val iv = rowView.findViewById<ImageView>(R.id.knoepPhoto)

        // maybe still not small enough?
        Picasso.with(context).setIndicatorsEnabled(true)
        Picasso.with(context).setLoggingEnabled(true)

        val picasso = Picasso.Builder(context)
                .listener { picasso, uri, exception ->
                    Log.d("Picasso", exception.message)
                }
                .build()

        picasso.load(currentKnoepList.get(position).uri)
                .fit()
                .centerCrop()
                .into(iv)

//        Picasso.with(context).load(currentKnoepList.get(position).uri).fit().centerCrop().into(iv)

        return rowView
    }

}