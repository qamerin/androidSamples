package com.example.mycampgear;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mycampgear.db.EventOpenHelper;
import com.example.mycampgear.entity.ItemEntity;

public class ItemDetailActivity extends AppCompatActivity {
    private ItemEntity item = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        Intent intent = getIntent();
        // MainActivityからintentで受け取ったものを取り出す
        int itemId = intent.getIntExtra("itemId",0);

        SQLiteOpenHelper helper = new EventOpenHelper(this);
        SQLiteDatabase database = null;
        Cursor cursorMItem = null;

        try {
            database = helper.getReadableDatabase();
            cursorMItem = database.query("M_ITEM", null, "_item_id=?", new String[]{String.valueOf(itemId)}, null, null, null, null);

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
                    item.setItemId(itemId);
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

        TextView categoryView = findViewById(R.id.label_category);
        categoryView.setText(item.getCategory());

        TextView itemNameView = findViewById(R.id.label_itemName);
        itemNameView.setText(item.getItemName());

        TextView descriptionView = findViewById(R.id.label_description);
        descriptionView.setText(item.getDescription());

        ImageView  imageView = findViewById(R.id.selected_photo);
        if(item.getImage()!=null){
            imageView.setImageBitmap(item.getImage());
        }else{
            imageView.setImageResource(R.drawable.no_image);
        }


        View add_btn = findViewById(R.id.btn_edit);
        add_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View vew){
                Intent intent = new Intent(ItemDetailActivity.this,
                        ItemEditActivity.class);

                intent.putExtra("itemId", item.getItemId());
                startActivity(intent);

            }
        });
    }
}