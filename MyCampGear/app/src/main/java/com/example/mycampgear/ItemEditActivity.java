package com.example.mycampgear;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.mycampgear.common.DatePick;
import com.example.mycampgear.db.EventOpenHelper;
import com.example.mycampgear.entity.ItemEntity;

import java.io.ByteArrayOutputStream;


public class ItemEditActivity extends AppCompatActivity {

    private final static int RESULT_CAMERA = 1001;
    private ImageView imageView;

    private Bitmap bitmap;
    private ItemEntity item = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_edit);

    }
    @Override
    protected void onResume() {
        super.onResume();

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


        TextView categoryView = findViewById(R.id.input_category);
        categoryView.setText(item.getCategory());

        TextView brandView = findViewById(R.id.input_brand);
        brandView.setText(item.getBrand());

        TextView itemNameView = findViewById(R.id.input_item);
        itemNameView.setText(item.getItemName());

        TextView descriptionView = findViewById(R.id.input_description);
        descriptionView.setText(item.getDescription());

        imageView = findViewById(R.id.image_view);
        if(item.getImage()!=null){
            imageView.setImageBitmap(item.getImage());
        }else{
            imageView.setImageResource(R.drawable.no_image);
        }

        Button cameraButton = findViewById(R.id.camera_button);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, RESULT_CAMERA);
            }
        });

        View add_btn = findViewById(R.id.add_btn);
        add_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View vew){

                // タイトルと内容を取得してデータベースに登録する
                EditText editTextCategory = findViewById(R.id.input_category);
                EditText editTextBrand = findViewById(R.id.input_brand);
                EditText editTextItem = findViewById(R.id.input_item);
                EditText editTextDesc = findViewById(R.id.input_description);

                SQLiteOpenHelper helper = new EventOpenHelper(ItemEditActivity.this);
                SQLiteDatabase database = null;

                try {
                    database = helper.getWritableDatabase();

                    ContentValues cv = new ContentValues();
                    cv.put("category", editTextCategory.getText().toString());
                    cv.put("brand", editTextBrand.getText().toString());
                    cv.put("item_name", editTextItem.getText().toString());
                    cv.put("description", editTextDesc.getText().toString());
                    if (bitmap != null) {
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                        byte[] bytes = byteArrayOutputStream.toByteArray();
                        cv.put("image", bytes);
                    }
                    database.update("M_ITEM", cv,"_item_id=?", new String[]{String.valueOf(item.getItemId())} );

                    String toastMessage = "アイテムの更新が行われました";
                    toastMake(toastMessage, 0, -200);


                    Intent intent = new Intent(ItemEditActivity.this,
                            ItemDetailActivity.class);

                    intent.putExtra("itemId", item.getItemId());
                    startActivity(intent);


                } catch (Exception e) {
                    Log.e(getLocalClassName(), "DBエラー発生", e);
                } finally {
                    if (database != null) {
                        database.close();
                    }
                }

            }
        });




    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePick();
        newFragment.show(getSupportFragmentManager(), "datePicker");

    }

    private void toastMake(String message, int x, int y){
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        // 位置調整
        toast.setGravity(Gravity.CENTER, x, y);
        toast.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_CAMERA) {
            // cancelしたケースも含む
            if (data.getExtras() == null) {
                Log.d("debug", "cancel ?");
                return;
            } else {
                bitmap = (Bitmap) data.getExtras().get("data");
                if (bitmap != null) {
                    // 画像サイズを計測
                    int bmpWidth = bitmap.getWidth();
                    int bmpHeight = bitmap.getHeight();
                    Log.d("debug", String.format("w= %d", bmpWidth));
                    Log.d("debug", String.format("h= %d", bmpHeight));
                }
            }

            imageView.setImageBitmap(bitmap);
        }
    }

}