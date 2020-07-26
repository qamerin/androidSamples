package com.example.mycampgear;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mycampgear.adapter.ItemListViewAdapter;

public class EventActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private static final String[] scenes = {
            "アメニティ・ドーム",
            "Wroxall",
            "Whitewell",
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
        setContentView(R.layout.activity_event);

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


        // ListViewのインスタンスを生成
        ListView listView = findViewById(R.id.list_view);

        // BaseAdapter を継承したadapterのインスタンスを生成
        // レイアウトファイル list.xml を activity_main.xml に
        // inflate するためにadapterに引数として渡す
        BaseAdapter adapter = new ItemListViewAdapter(this.getApplicationContext(),
                R.layout.list_item, scenes, photos);

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
        String selectedText = scenes[position];
        int selectedPhoto = photos[position];
        // インテントにセット
        intent.putExtra("Text", selectedText);
        intent.putExtra("Photo", selectedPhoto);

        // SubActivityへ遷移
        startActivity(intent);
    }
}