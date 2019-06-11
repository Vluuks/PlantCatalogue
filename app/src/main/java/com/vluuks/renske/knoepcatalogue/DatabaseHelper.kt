package com.vluuks.renske.knoepcatalogue

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri

/**
 * Created by Gebruiker on 11-6-2019.
 */

// dat is me nogal een mond vol
class DatabaseHelper private constructor (val context: Context, val name: String, val cursorFactory : SQLiteDatabase.CursorFactory?, var version : Int)
    : SQLiteOpenHelper(context, name, cursorFactory, version) {


    companion object {

        @Volatile var instance : DatabaseHelper? = null

        val DATABASE_VERSION = 1
        val DATABASE_NAME = "KnoepDatabase"
        val TABLE_NAME = "KnoepTable"
        val COL_NAME = "name"
        val COL_TYPE = "type"
        val COL_SIZE = "size"
        val COL_URI = "uri"
        val COL_DESC = "desc"
        val COL_TS = "ts"

        // I understand nothing of this anymore (https://stackoverflow.com/questions/40398072/singleton-with-parameter-in-kotlin)
        fun getInstance(context: Context): DatabaseHelper? {

            // create new if none
            if(instance == null){
                instance = DatabaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION)
            }
            return instance!!
        }
    }


    // (val context: Context!, val databaseName: String, val cursorFactory : SQLiteDatabase.CursorFactory)

    override fun onOpen(db: SQLiteDatabase?) {
        super.onOpen(db)
    }

    override fun onCreate(db: SQLiteDatabase?) {

        val queryString = "CREATE TABLE $TABLE_NAME (_id INTEGER PRIMARY KEY AUTOINCREMENT, $COL_NAME TEXT, $COL_TYPE TEXT, $COL_SIZE INTEGER, $COL_DESC VARCHAR, $COL_URI TEXT, $COL_TS DATETIME DEFAULT CURRENT_TIMESTAMP)"
        db?.execSQL(queryString)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }


    fun insert(aKnoep: Knoep) {

        val contentValues = ContentValues()
        contentValues.put(COL_NAME, aKnoep.name)
        contentValues.put(COL_TYPE, aKnoep.type)
        contentValues.put(COL_SIZE, aKnoep.size)
        contentValues.put(COL_DESC, aKnoep.desc)
        contentValues.put(COL_URI, aKnoep.uri.toString())

        // add to db
        writableDatabase.insert(TABLE_NAME, null, contentValues)

    }

    fun selectAll(): Cursor {
        return readableDatabase.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }

    fun selectAllConvert(): ArrayList<Knoep> {

        val cursor = readableDatabase.rawQuery("SELECT * FROM $TABLE_NAME", null)

        val knoepList = ArrayList<Knoep>()
        while(cursor.moveToNext()) {

            knoepList.add(Knoep(
                    cursor.getString(cursor.getColumnIndex(COL_NAME)),
                    cursor.getInt(cursor.getColumnIndex(COL_SIZE)),
                    cursor.getString(cursor.getColumnIndex(COL_TYPE)),
                    cursor.getString(cursor.getColumnIndex(COL_DESC)),
                    Uri.parse(cursor.getString(cursor.getColumnIndex(COL_URI)))
            ))
        }

        return knoepList
    }

}