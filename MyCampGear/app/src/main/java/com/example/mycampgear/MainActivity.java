package com.example.mycampgear;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.example.mycampgear.adapter.EventListViewAdapter;
import com.example.mycampgear.db.EventOpenHelper;
import com.example.mycampgear.entity.EventEntity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private List<EventEntity> tEvents = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View add_btn = findViewById(R.id.add_btn);
        add_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View vew){
                Intent intent = new Intent(MainActivity.this, EventRegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        // 全てのEventのタイトルを取得し、Listに格納する
        SQLiteOpenHelper helper = new EventOpenHelper(this);
        SQLiteDatabase database = null;
        Cursor cursor = null;

        try {
            database = helper.getReadableDatabase();

            cursor = database.query("T_Event", null, null, null, null, null, null);

            if (cursor.moveToFirst()) {
                do {
                    Integer eventId = cursor.getInt(cursor.getColumnIndex("_event_id"));
                    String title = cursor.getString(cursor.getColumnIndex("title"));
                    String date = cursor.getString(cursor.getColumnIndex("date"));
                    String description = cursor.getString(cursor.getColumnIndex("description"));
                    byte[] dataValue = cursor.getBlob(cursor.getColumnIndex("image")); //image
                    Bitmap bmp = null;
                    if (dataValue != null) {
                        bmp = BitmapFactory.decodeByteArray(dataValue, 0, dataValue.length);
                    }
                    EventEntity event = new EventEntity();
                    event.setEventId(eventId);
                    event.setTitle(title);
                    event.setDate(date.replaceAll("-","/"));
                    event.setDescription(description);
                    event.setImage(bmp);

                    // duplicate check for events
                    boolean isEventExists=false;
                    for(EventEntity entity:tEvents){
                        if(entity.getEventId() == eventId){
                            isEventExists = true;
                        }
                    }
                    if(!isEventExists){
                        tEvents.add(event);
                    }

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

        // ListViewのインスタンスを生成
        ListView listView = findViewById(R.id.list_view);

        // BaseAdapter を継承したadapterのインスタンスを生成
        // レイアウトファイル list.xml を activity_main.xml に
        // inflate するためにadapterに引数として渡す
        BaseAdapter adapter = new EventListViewAdapter(this.getApplicationContext(),
                R.layout.list_event, tEvents);

        // ListViewにadapterをセット
        listView.setAdapter(adapter);

        // クリックリスナーをセット
        listView.setOnItemClickListener(this);

    }
        @Override
    public void onItemClick(AdapterView<?> parent, View v,
                            int position, long id) {

        Intent intent = new Intent(
                this.getApplicationContext(), EventDetailActivity.class);

        // インテントにセット
        intent.putExtra("EventId",tEvents.get(position).getEventId());
        // EventActivityへ遷移
        startActivity(intent);
    }
}