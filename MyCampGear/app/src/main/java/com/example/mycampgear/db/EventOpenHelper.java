package com.example.mycampgear.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EventOpenHelper extends SQLiteOpenHelper {

    private static final String FILE_NAME = "Event";
    private static final int VERSION = 1;

    private static final String DDL_EVENT = "CREATE TABLE Event (" +
            " _event_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            " title TEXT," +
            " date DATE," +
            " content TEXT" +
            ")";

    private static final String DDL_ITEM = "CREATE TABLE ITEM ( " +
            "_item_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            " event_id TEXT," +
            " title TEXT," +
            " date DATE, " +
            "content TEXT)";

    public EventOpenHelper(Context context) {
        super(context, FILE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DDL_EVENT);
        sqLiteDatabase.execSQL(DDL_ITEM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
