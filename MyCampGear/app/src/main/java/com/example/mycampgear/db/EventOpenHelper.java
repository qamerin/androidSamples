package com.example.mycampgear.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EventOpenHelper extends SQLiteOpenHelper {

    private static final String FILE_NAME = "Event";
    private static final int VERSION = 1;

    private static final String DDL_T_EVENT = "CREATE TABLE T_Event (" +
            " _event_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            " title TEXT," +
            " date DATE," +
            " description TEXT" +
            ")";

    private static final String DDL_T_ITEM = "CREATE TABLE T_ITEM ( " +
            "_event_item_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            " event_id TEXT," +
            " item_id TEXT" +
            ")";

    private static final String DDL_M_ITEM = "CREATE TABLE M_ITEM ( " +
            "_item_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            " category TEXT," +
            " brand TEXT," +
            " item_name TEXT," +
            " image BLOB," +
             "description TEXT)";

    public EventOpenHelper(Context context) {
        super(context, FILE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DDL_T_EVENT);
        sqLiteDatabase.execSQL(DDL_T_ITEM);
        sqLiteDatabase.execSQL(DDL_M_ITEM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
