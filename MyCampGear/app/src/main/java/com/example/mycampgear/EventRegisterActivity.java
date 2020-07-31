package com.example.mycampgear;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.mycampgear.common.DatePick;
import com.example.mycampgear.db.EventOpenHelper;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EventRegisterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private EditText editTextDate;

    private final static int RESULT_CAMERA = 1001;
    private ImageView imageView;

    private Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_register);


        imageView = findViewById(R.id.image_view);

        Button cameraButton = findViewById(R.id.camera_button);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, RESULT_CAMERA);
            }
        });


        editTextDate = findViewById(R.id.label_branc);

        View add_btn = findViewById(R.id.add_btn);
        add_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View vew){

                // タイトルと内容を取得してデータベースに登録する
                EditText editTextTitle = findViewById(R.id.input_category);
                EditText editTextContent = findViewById(R.id.input_description);
                EditText editTextDate = findViewById(R.id.label_branc);

                String dob_var=(editTextDate.getText().toString());
                DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
                Date dateObject;

                SQLiteOpenHelper helper = new EventOpenHelper(EventRegisterActivity.this);
                SQLiteDatabase database = null;

                try {
                    database = helper.getWritableDatabase();

                    ContentValues cv = new ContentValues();
                    cv.put("title", editTextTitle.getText().toString());
                    dateObject = formatter.parse(dob_var);
                    cv.put("date",
                            new SimpleDateFormat("yyyy-MM-dd").format(dateObject));
                    cv.put("description", editTextContent.getText().toString());
                    if (bitmap != null) {
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                        byte[] bytes = byteArrayOutputStream.toByteArray();
                        cv.put("image", bytes);
                    }

                    database.insert("T_EVENT", null, cv);


                    String toastMessage = "イベントの追加が行われました";
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

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        String str = String.format(Locale.US, "%d/%d/%d",year, monthOfYear+1, dayOfMonth);
        editTextDate.setText(str);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_CAMERA) {
//            Bitmap bitmap;
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