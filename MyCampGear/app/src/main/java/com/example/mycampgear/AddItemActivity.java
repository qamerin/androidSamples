package com.example.mycampgear;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddItemActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
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

    private static final String[] dates = {
            "2020/10/19",
            "2020/10/19",
            "2020/10/19",
            "2020/10/19",
            "2020/10/19",
            "2020/10/19",
            "2020/10/19",
            "2020/10/19",
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
        ListView listView = findViewById(R.id.list_add_view);

        // BaseAdapter を継承したadapterのインスタンスを生成
        // レイアウトファイル list.xml を activity_main.xml に
        // inflate するためにadapterに引数として渡す
//        BaseAdapter adapter = new EventListViewAdapter(this.getApplicationContext(),
//                R.layout.list_add_item, scenes,dates, photos);
        BaseAdapter adapter = new ItemListViewAdapter(this.getApplicationContext(),
                R.layout.list_add_item, scenes, photos);

        // ListViewにadapterをセット
        listView.setAdapter(adapter);

        // クリックリスナーをセット
        listView.setOnItemClickListener(this);



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



        View add_btn = findViewById(R.id.add_item_btn);
        add_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View vew){
                String toastMessage = "アイテムの追加が行われました";
                toastMake(toastMessage, 0, -200);

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