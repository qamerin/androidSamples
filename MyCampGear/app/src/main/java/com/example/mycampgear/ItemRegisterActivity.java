package com.example.mycampgear;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.mycampgear.db.EventOpenHelper;


public class ItemRegisterActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_register);


        View add_btn = findViewById(R.id.add_btn);
        add_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View vew){

                // タイトルと内容を取得してデータベースに登録する
                EditText editTextCategory = findViewById(R.id.input_category);
                EditText editTextBrand = findViewById(R.id.input_brand);
                EditText editTextItem = findViewById(R.id.input_item);
                EditText editTextDesc = findViewById(R.id.input_description);

                SQLiteOpenHelper helper = new EventOpenHelper(ItemRegisterActivity.this);
                SQLiteDatabase database = null;

                try {
                    database = helper.getWritableDatabase();

                    ContentValues cv = new ContentValues();
                    cv.put("category", editTextCategory.getText().toString());
                    cv.put("brand", editTextBrand.getText().toString());
                    cv.put("item_name", editTextItem.getText().toString());
                    cv.put("description", editTextDesc.getText().toString());

                    database.insert("M_ITEM", null, cv);

                    String toastMessage = "アイテムの追加が行われました";
                    toastMake(toastMessage, 0, -200);

                    // 遷移元の画面に戻るため、finishメソッドを呼び出す
                    finish();


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

}