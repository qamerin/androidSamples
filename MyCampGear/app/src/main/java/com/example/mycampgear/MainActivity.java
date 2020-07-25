package com.example.mycampgear;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.example.mycampgear.adapter.EventListViewAdapter;
import com.example.mycampgear.db.EventOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private String[] eventArray = null;
    private String[] dateArray = null;
    private String[] descriptionArray = null;

    // ちょっと冗長的ですが分かり易くするために
    private static final int[] photos = {
            R.drawable.profile,
            R.drawable.profile,
            R.drawable.profile,
            R.drawable.profile,
            R.drawable.profile,
            R.drawable.profile,
            R.drawable.profile,
            R.drawable.profile,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View add_btn = findViewById(R.id.add_btn);
        add_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View vew){
                Intent intent = new Intent(MainActivity.this, AddEventActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        // 全てのEventのタイトルを取得し、Listに格納する
        List<String> eventList = new ArrayList<>();
        List<String> dateList = new ArrayList<>();
        List<String> descriptionList = new ArrayList<>();
        SQLiteOpenHelper helper = new EventOpenHelper(this);
        SQLiteDatabase database = null;
        Cursor cursor = null;

        try {
            database = helper.getReadableDatabase();

            cursor = database.query("T_Event", null, null, null, null, null, null);

            if (cursor.moveToFirst()) {
                do {
                    String title = cursor.getString(cursor.getColumnIndex("title"));
                    String date = cursor.getString(cursor.getColumnIndex("date"));
                    String description = cursor.getString(cursor.getColumnIndex("description"));
                    eventList.add(title);
                    dateList.add(date);
                    descriptionList.add(description);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            Log.e(getLocalClassName(), "DBエラー発生", e);
        } finally {
            if (database != null) {
                database.close();
            }
            if (cursor != null) {
                cursor.close();
            }
        }
        eventArray = eventList.toArray(new String[eventList.size()]);
        dateArray = dateList.toArray(new String[dateList.size()]);
        descriptionArray = descriptionList.toArray(new String[descriptionList.size()]);

        // ListViewのインスタンスを生成
        ListView listView = findViewById(R.id.list_view);

        // BaseAdapter を継承したadapterのインスタンスを生成
        // レイアウトファイル list.xml を activity_main.xml に
        // inflate するためにadapterに引数として渡す
        BaseAdapter adapter = new EventListViewAdapter(this.getApplicationContext(),
                R.layout.list_event, eventArray,dateArray,descriptionArray ,photos);

        // ListViewにadapterをセット
        listView.setAdapter(adapter);

        // クリックリスナーをセット
        listView.setOnItemClickListener(this);

    }
        @Override
    public void onItemClick(AdapterView<?> parent, View v,
                            int position, long id) {

        Intent intent = new Intent(
                this.getApplicationContext(), EventActivity.class);

        // clickされたpositionのtextとphotoのID
        String selectedTitle = eventArray[position];
        String selectedDate = dateArray[position];
        int selectedPhoto = photos[position];
        // インテントにセット
        intent.putExtra("Text", selectedTitle);
        intent.putExtra("Date", selectedDate);
        intent.putExtra("Photo", selectedPhoto);

        // SubActivityへ遷移
        startActivity(intent);
    }
}