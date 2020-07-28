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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mycampgear.adapter.ItemListViewAdapter;
import com.example.mycampgear.db.EventOpenHelper;
import com.example.mycampgear.entity.ItemEntity;

import java.util.ArrayList;
import java.util.List;

public class EventActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
//    private static final String[] scenes = {
//            "アメニティ・ドーム",
//            "Wroxall",
//            "Whitewell",
//            "Ryde",
//            "StLawrence",
//            "Lake",
//            "Sandown",
//            "Shanklin"
//    };

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

    private List<ItemEntity> mItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);


    }
    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        // MainActivityからintentで受け取ったものを取り出す
        final int eventId = intent.getIntExtra("EventId",0);
        String selectedText = intent.getStringExtra("Text");
        String selectedDate = intent.getStringExtra("Date");
        String selectedDescription = intent.getStringExtra("Description");

        int selectedPhoto = intent.getIntExtra("Photo", 0);

        TextView textView = findViewById(R.id.selected_event);
        textView.setText(selectedText);
        TextView dateView = findViewById(R.id.selected_date);
        dateView.setText(selectedDate);
        TextView descriptionView = findViewById(R.id.selected_description);
        descriptionView.setText(selectedDescription);
        ImageView  imageView = findViewById(R.id.selected_photo);
        imageView.setImageResource(selectedPhoto);




        SQLiteOpenHelper helper = new EventOpenHelper(this);
        SQLiteDatabase database = null;
        Cursor cursorTItem = null;
        Cursor cursorMItem = null;

//        List<ItemEntity> mItems = new ArrayList<>();

        try {
            database = helper.getReadableDatabase();

            cursorTItem = database.query("T_ITEM", null, "event_id=?", new String[]{String.valueOf(eventId)}, null, null, null, null);

            if (cursorTItem.moveToFirst()) {
                do {
                    String itemId = cursorTItem.getString(cursorTItem.getColumnIndex("item_id"));

                    cursorMItem = database.query("M_ITEM", null, "_item_id=?", new String[]{itemId}, null, null, null, null);

                    if (cursorMItem.moveToFirst()) {
                        do {
                            String category = cursorMItem.getString(cursorMItem.getColumnIndex("category"));
                            String brand = cursorMItem.getString(cursorMItem.getColumnIndex("brand"));
                            String itemName = cursorMItem.getString(cursorMItem.getColumnIndex("item_name"));
                            String description = cursorMItem.getString(cursorMItem.getColumnIndex("description"));
                            byte[] dataValue = cursorMItem.getBlob(cursorMItem.getColumnIndex("image")); //image
                            Bitmap bmp = null;
                            if (dataValue != null) {
                                bmp = BitmapFactory.decodeByteArray(dataValue, 0, dataValue.length);
                            }
                            ItemEntity item = new ItemEntity();
                            item.setItemId(Integer.parseInt(itemId));
                            item.setCategory(category);
                            item.setBrand(brand);
                            item.setItemName(itemName);
                            item.setImage(bmp);
                            item.setDescription(description);
                            mItems.add(item);

                        }while (cursorMItem.moveToNext());
                    }

                } while (cursorTItem.moveToNext());
            }

        } catch (Exception e) {
            Log.e(getLocalClassName(), "DBエラー発生", e);
        } finally {
            if (database != null) {
                database.close();
            }
            if (cursorTItem != null) {
                cursorTItem.close();
            }
        }

        // ListViewのインスタンスを生成
        ListView listView = findViewById(R.id.list_view);

        // BaseAdapter を継承したadapterのインスタンスを生成
        // レイアウトファイル list.xml を activity_main.xml に
        // inflate するためにadapterに引数として渡す
        BaseAdapter adapter = new ItemListViewAdapter(this.getApplicationContext(),
                R.layout.list_item,mItems);
//                R.layout.list_item, scenes, photos);

        // ListViewにadapterをセット
        listView.setAdapter(adapter);

        // クリックリスナーをセット
        listView.setOnItemClickListener(this);

        View add_btn = findViewById(R.id.add_item_btn);
        add_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View vew){

                Intent intent = new Intent(EventActivity.this, AddItemActivity.class);
                intent.putExtra("EventId", eventId);
                startActivity(intent);

            }
        });

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View v,
                            int position, long id) {

        System.out.println("clicked");
        Intent intent = new Intent(
                this.getApplicationContext(), DetailActivity.class);

        // clickされたpositionのtextとphotoのID
//        String selectedText = scenes[position];
        String selectedText = mItems.get(position).getItemName() ;

        int selectedPhoto = photos[position];
        // インテントにセット
        intent.putExtra("Text", selectedText);
        intent.putExtra("Photo", selectedPhoto);

        // SubActivityへ遷移
        startActivity(intent);
    }
}