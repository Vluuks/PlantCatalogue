package com.vluuks.renske.knoepcatalogue

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.squareup.picasso.Picasso
import knoeps.*

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "ja of nee")
        Log.d(TAG, knoepList.toString())

        // the argument type and name are reversed, this is going to be confusing
//        val aKnoep = Knoep("Vluuks", 30, "De kleinste knoep")
//        knoepList.add(aKnoep)

        // adding listener, this is a lot cleaner than in Java
        addKnoepFAB.setOnClickListener {

            // create simple intent
            val intent = Intent(this, AddKnoepActivity::class.java)
            startActivity(intent)
        }


        listView.adapter = KnoepAdapter(this, R.layout.knoep_item, knoepList)


    }


    override fun onResume() {
        super.onResume()

        Log.d(TAG, "onResume" + knoepList.toString())
//      listView.deferNotifyDataSetChanged()

        listView.adapter = KnoepAdapter(this, R.layout.knoep_item, knoepList)
    }
}

    // if it does not return anything you dont need to say void
    // apparently it does not know that it is linked, even though the XML does seem to know

