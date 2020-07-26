package com.example.mycampgear;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mycampgear.adapter.AddItemListViewAdapter;
import com.example.mycampgear.db.EventOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class AddItemActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private String[] categoryArray = null;
    private String[] brandArray = null;
    private String[] itemNameArray = null;
    private String[] descriptionArray = null;

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

                // ListViewのインスタンスを生成
//        ListView listView = findViewById(R.id.list_add_view);


        Intent intent = getIntent();
        int eventId = intent.getIntExtra("EventId",0);

        View add_new_item_btn = findViewById(R.id.add_new_item);
        add_new_item_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View vew){

                // clickされたpositionのtextとphotoのID
                Intent intent = new Intent(AddItemActivity.this,
                        ItemRegisterActivity.class);

                startActivity(intent);
            }
        });



    }


    @Override
    protected void onResume() {
        super.onResume();

        // 全てのM_ITEMの情報を取得し、Listに格納する
        List<String> categoryList = new ArrayList<>();
        List<String> brandList = new ArrayList<>();
        List<String> itemNameList = new ArrayList<>();
        List<String> descriptionList = new ArrayList<>();
        SQLiteOpenHelper helper = new EventOpenHelper(this);
        SQLiteDatabase database = null;
        Cursor cursor = null;

        try {
            database = helper.getReadableDatabase();

            cursor = database.query("M_ITEM", null, null, null, null, null, null);

            if (cursor.moveToFirst()) {
                do {
                    String category = cursor.getString(cursor.getColumnIndex("category"));
                    String brand = cursor.getString(cursor.getColumnIndex("brand"));
                    String itemName = cursor.getString(cursor.getColumnIndex("item_name"));
                    String description = cursor.getString(cursor.getColumnIndex("description"));

                    categoryList.add(category);
                    brandList.add(brand);
                    itemNameList.add(itemName);
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
        categoryArray = categoryList.toArray(new String[categoryList.size()]);
        brandArray = brandList.toArray(new String[brandList.size()]);
        itemNameArray = itemNameList.toArray(new String[itemNameList.size()]);
        descriptionArray = descriptionList.toArray(new String[descriptionList.size()]);

        // ListViewのインスタンスを生成
//        final ListView listView = findViewById(R.id.list_add_view);
            listView = findViewById(R.id.list_add_view);

        // BaseAdapter を継承したadapterのインスタンスを生成
        // レイアウトファイル list.xml を activity_main.xml に
        // inflate するためにadapterに引数として渡す
        BaseAdapter adapter = new AddItemListViewAdapter(this.getApplicationContext(),
                R.layout.list_add_item, categoryArray, brandArray, itemNameArray, descriptionArray, photos);

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

                // 選択の方式の設定
                listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

                SparseBooleanArray checked = listView.getCheckedItemPositions();
                for (int i = 0; i < checked.size(); i++) {
                    // チェックされているアイテムの key の取得
                    int key = checked.keyAt(i);
                    Log.v(getClass().getSimpleName(), "values: " + key);
                }

                // clickされたpositionのtextとphotoのID
                Intent intent = new Intent(AddItemActivity.this,
                        EventActivity.class);

                String selectedText = scenes[0];
                int selectedPhoto = photos[0];
                // インテントにセット
                intent.putExtra("Text", selectedText);
                intent.putExtra("Photo", selectedPhoto);
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