package com.example.mycampgear.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EventOpenHelper extends SQLiteOpenHelper {

    private static final String FILE_NAME = "EventEntity";
    private static final int VERSION = 1;

    private static final String DDL = "CREATE TABLE TodoEntity ( _id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, content TEXT)";

    public EventOpenHelper(Context context) {
        super(context, FILE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DDL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
