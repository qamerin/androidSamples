package com.example.mycampgear;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mycampgear.db.EventOpenHelper;
import com.example.mycampgear.entity.ItemEntity;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    private ItemEntity item = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        // MainActivityからintentで受け取ったものを取り出す
        String itemId = intent.getStringExtra("itemId");

        SQLiteOpenHelper helper = new EventOpenHelper(this);
        SQLiteDatabase database = null;
        Cursor cursorMItem = null;

        try {
            database = helper.getReadableDatabase();
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
                    item = new ItemEntity();
                    item.setItemId(Integer.parseInt(itemId));
                    item.setCategory(category);
                    item.setBrand(brand);
                    item.setItemName(itemName);
                    item.setImage(bmp);
                    item.setDescription(description);

                }while (cursorMItem.moveToNext());
            }
        } catch (Exception e) {
            Log.e(getLocalClassName(), "DBエラー発生", e);
        } finally {
            if (database != null) {
                database.close();
            }
        }

        TextView textView = findViewById(R.id.selected_event);
        textView.setText(item.getItemName());
        ImageView  imageView = findViewById(R.id.selected_photo);
        if(item.getImage()!=null){
            imageView.setImageBitmap(item.getImage());
        }else{
            imageView.setImageResource(R.drawable.no_image);
        }

    }
}