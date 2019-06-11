package com.vluuks.renske.knoepcatalogue

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by Gebruiker on 11-6-2019.
 */

// dat is me nogal een mond vol
class DatabaseHelper(val context: Context, val name: String, val cursorFactory : SQLiteDatabase.CursorFactory, var version : Int)
    : SQLiteOpenHelper(context, name, cursorFactory, version) {


    companion object {
        val DATABASE_VERSION = 1
        val DATABASE_NAME = "KnoepDatabase"
        val TABLE_NAME = "KnoepTable"
        val COL_NAME = "name"
        val COL_TYPE = "type"
        val COL_SIZE = "size"
        val COL_URI = "uri"
        val COL_DESC = "desc"
        val COL_TS = "ts"
    }


    // (val context: Context!, val databaseName: String, val cursorFactory : SQLiteDatabase.CursorFactory)

    override fun onOpen(db: SQLiteDatabase?) {
        super.onOpen(db)
    }

    override fun onCreate(db: SQLiteDatabase?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

        val queryString = "CREATE TABLE $TABLE_NAME (_id INTEGER PRIMARY KEY AUTOINCREMENT, $COL_NAME TEXT, $COL_TYPE TEXT, $COL_SIZE INTEGER, $COL_DESC VARCHAR, $COL_URI TEXT, $COL_TS DATETIME DEFAULT CURRENT_TIMESTAMP)"
        db?.execSQL(queryString)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}