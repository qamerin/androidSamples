package com.example.mycampgear;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        View add_btn = findViewById(R.id.add_btn);
        add_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View vew){
                String toastMessage = "イベントの追加が行われました";
                toastMake(toastMessage, 0, -200);

                // clickされたpositionのtextとphotoのID
                Intent intent = new Intent(AddEventActivity.this,
                        MainActivity.class);
                startActivity(intent);
            }
        });
    }
    private void toastMake(String message, int x, int y){
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        // 位置調整
        toast.setGravity(Gravity.CENTER, x, y);
        toast.show();
    }
}