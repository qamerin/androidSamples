package com.example.to_do

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

public class ToDoDBHelper(context: Context, databaseName:String, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, databaseName, factory, version){
    override fun onCreate(database: SQLiteDatabase?) {
        database?.execSQL("create table if not exists SampleTable (id text primary key, name text, type integer)");
    }

    override fun onUpgrade(database: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < newVersion) {
            database?.execSQL("alter table SampleTable add column deleteFlag integer default 0")
        }
    }

}