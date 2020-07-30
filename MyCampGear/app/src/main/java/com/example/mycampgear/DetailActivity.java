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
                Intent intent = new Intent(DetailActivity.this,
                        ItemEditActivity.class);

                intent.putExtra("itemId", item.getItemId());
                startActivity(intent);

//                intent.putExtra("itemId", String.valueOf(mItems.get(position).getItemId()));
                // Detailctivityへ遷移
                startActivity(intent);

//                // タイトルと内容を取得してデータベースに登録する
//                EditText editTextCategory = findViewById(R.id.input_category);
//                EditText editTextBrand = findViewById(R.id.input_brand);
//                EditText editTextItem = findViewById(R.id.input_item);
//                EditText editTextDesc = findViewById(R.id.input_description);
//
//                SQLiteOpenHelper helper = new EventOpenHelper(DetailrActivity.this);
//                SQLiteDatabase database = null;
//
//                try {
//                    database = helper.getWritableDatabase();
//
//                    ContentValues cv = new ContentValues();
//                    cv.put("category", editTextCategory.getText().toString());
//                    cv.put("brand", editTextBrand.getText().toString());
//                    cv.put("item_name", editTextItem.getText().toString());
//                    cv.put("description", editTextDesc.getText().toString());
//                    if (bitmap != null) {
//                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
//                        byte[] bytes = byteArrayOutputStream.toByteArray();
//                        cv.put("image", bytes);
//                    }
//                    database.insert("M_ITEM", null, cv);
//
//                    String toastMessage = "アイテムの追加が行われました";
//                    toastMake(toastMessage, 0, -200);
//


//                } catch (Exception e) {
//                    Log.e(getLocalClassName(), "DBエラー発生", e);
//                } finally {
//                    if (database != null) {
//                        database.close();
//                    }
//                }

            }
        });
    }
}