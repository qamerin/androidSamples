package com.example.mycampgear;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class SubActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        Intent intent = getIntent();
        // MainActivityからintentで受け取ったものを取り出す
        String selectedText = intent.getStringExtra("Text");
        int selectedPhoto = intent.getIntExtra("Photo", 0);

        TextView textView = findViewById(R.id.selected_text);
        textView.setText(selectedText);
        ImageView  imageView = findViewById(R.id.selected_photo);
        imageView.setImageResource(selectedPhoto);
    }
}