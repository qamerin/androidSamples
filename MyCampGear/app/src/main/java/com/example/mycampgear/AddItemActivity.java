package com.example.mycampgear;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mycampgear.adapter.AddItemArrayListViewAdapter;
import com.example.mycampgear.db.EventOpenHelper;
import com.example.mycampgear.entity.ItemEntity;

import java.util.ArrayList;
import java.util.List;

public class AddItemActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{


    ListView listView = null;

    private static final String[] scenes = {
            "サーカスTC",
            "Wroxall",
            "サーカスTC",
            "Ryde",
            "StLawrence",
            "Lake",
            "Sandown",
            "Shanklin"
    };


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
        setContentView(R.layout.activity_add_item);
        Intent intent = getIntent();
        final int eventId = intent.getIntExtra("EventId",0);

        View add_new_item_btn = findViewById(R.id.add_new_item);
        add_new_item_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View vew){

                // clickされたpositionのtextとphotoのID
                Intent intent = new Intent(AddItemActivity.this,
                        ItemRegisterActivity.class);

                intent.putExtra("EventId", eventId);
                startActivity(intent);
            }
        });



    }


    @Override
    protected void onResume() {
        super.onResume();

        SQLiteOpenHelper helper = new EventOpenHelper(this);
        SQLiteDatabase database = null;
        Cursor cursor = null;

        List<ItemEntity> mItems = new ArrayList<>();

        try {
            database = helper.getReadableDatabase();

            cursor = database.query("M_ITEM", null, null, null, null, null, null);

            if (cursor.moveToFirst()) {
                do {
                    int itemId = cursor.getInt(cursor.getColumnIndex("_item_id"));
                    String category = cursor.getString(cursor.getColumnIndex("category"));
                    String brand = cursor.getString(cursor.getColumnIndex("brand"));
                    String itemName = cursor.getString(cursor.getColumnIndex("item_name"));
                    byte[] dataValue = cursor.getBlob(cursor.getColumnIndex("image")); //image
                    Bitmap bmp = null;
                    if (dataValue != null) {
                            bmp = BitmapFactory.decodeByteArray(dataValue, 0, dataValue.length);
                    }
                    String description = cursor.getString(cursor.getColumnIndex("description"));

                    ItemEntity item = new ItemEntity();
                    item.setItemId(itemId);
                    item.setCategory(category);
                    item.setBrand(brand);
                    item.setItemName(itemName);
                    item.setDescription(description);
                    item.setImage(bmp);
                    mItems.add(item);

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
        listView = findViewById(R.id.list_add_view);
        AddItemArrayListViewAdapter adapter = new AddItemArrayListViewAdapter(this, R.layout.list_add_item, mItems);

        // ListViewにadapterをセット
        listView.setAdapter(adapter);

        // フォーカスが当たらないよう設定
        listView.setItemsCanFocus(false);

        // 選択の方式の設定
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        // クリックリスナーをセット
        listView.setOnItemClickListener(this);

        View add_btn = findViewById(R.id.add_item_btn);
        add_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View vew){

                String toastMessage = "アイテムの追加が行われました";
                toastMake(toastMessage, 0, -200);
                // 現在チェックされているアイテムを取得
                // チェックされてないアイテムは含まれない模様

                // フォーカスが当たらないよう設定
                listView.setItemsCanFocus(false);

                Intent intent = getIntent();
                int eventId = intent.getIntExtra("EventId",0);
                System.out.println("Event Id : "+String.valueOf(eventId));

                SQLiteOpenHelper helper = new EventOpenHelper(AddItemActivity.this);
                SQLiteDatabase database = null;


                // リストビューのチェック状態をログに出力する
                ListView lv = findViewById(R.id.list_add_view);
                for(int i = 0;i < lv.getCount();i++) {
                    AddItemArrayListViewAdapter adapter = (AddItemArrayListViewAdapter)lv.getAdapter();
                    View view = adapter.getView(i,null,lv);
                    TextView tv = view.findViewById(R.id.category);

                    Log.i("Category : ", tv.getText().toString());
                    CheckBox cb = view.findViewById(R.id.checkbox_1);

                    if(adapter.checkList.get(i)){
                        Log.i("Checkbox", tv.getText().toString()+"はtrueです。");
                        try {
                            database = helper.getWritableDatabase();

                            TextView itemId = view.findViewById(R.id.itemId);
                            ContentValues cv = new ContentValues();
                            cv.put("event_id", String.valueOf(eventId));
                            cv.put("item_id", itemId.getText().toString());

                            database.insert("T_ITEM", null, cv);

                        } catch (Exception e) {
                            Log.e(getLocalClassName(), "DBエラー発生", e);
                        } finally {
                            if (database != null) {
                                database.close();
                            }
                        }

                    } else {
                        Log.i("Checkbox", tv.getText().toString() + "はfalseです。");
                    }



                }

                // clickされたpositionのtextとphotoのID
                Intent intentToEventPage = new Intent(AddItemActivity.this,
                        EventActivity.class);

                String selectedText = scenes[0];
                int selectedPhoto = photos[0];
                // インテントにセット
                intentToEventPage.putExtra("EventId", eventId);
                intentToEventPage.putExtra("Text", selectedText);
                intentToEventPage.putExtra("Photo", selectedPhoto);
                startActivity(intentToEventPage);
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
        String selectedText = scenes[position];
        int selectedPhoto = photos[position];
        // インテントにセット
        intent.putExtra("Text", selectedText);
        intent.putExtra("Photo", selectedPhoto);

        // SubActivityへ遷移
        startActivity(intent);
    }

    private void toastMake(String message, int x, int y){
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        // 位置調整
        toast.setGravity(Gravity.CENTER, x, y);
        toast.show();
    }

}