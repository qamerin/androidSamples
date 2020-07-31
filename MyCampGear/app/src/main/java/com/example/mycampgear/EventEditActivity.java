package com.example.mycampgear;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.mycampgear.common.DatePick;
import com.example.mycampgear.db.EventOpenHelper;
import com.example.mycampgear.entity.EventEntity;
import com.example.mycampgear.entity.ItemEntity;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EventEditActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private EventEntity event = new EventEntity();
    private EditText editTextDate;

    private final static int RESULT_CAMERA = 1001;
    private ImageView imageView;

    private Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);

    }
    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        // MainActivityからintentで受け取ったものを取り出す
        final int eventId = intent.getIntExtra("EventId",0);


        SQLiteOpenHelper helper = new EventOpenHelper(this);
        SQLiteDatabase database = null;
        Cursor cursorTEvent = null;
        Cursor cursorTItem = null;
        Cursor cursorMItem = null;

        try {
            database = helper.getReadableDatabase();
            cursorTEvent = database.query("T_Event", null, "_event_id=?", new String[]{String.valueOf(eventId)}, null, null, null, null);
            if (cursorTEvent.moveToFirst()) {
                do {
                    String title = cursorTEvent.getString(cursorTEvent.getColumnIndex("title"));
                    String date = cursorTEvent.getString(cursorTEvent.getColumnIndex("date"));
                    String description = cursorTEvent.getString(cursorTEvent.getColumnIndex("description"));
                    byte[] dataValue = cursorTEvent.getBlob(cursorTEvent.getColumnIndex("image")); //image
                    Bitmap bmp = null;
                    if (dataValue != null) {
                        bmp = BitmapFactory.decodeByteArray(dataValue, 0, dataValue.length);
                    }
                    event.setEventId(eventId);
                    event.setTitle(title);
                    event.setDate(date);
                    event.setDescription(description);
                    event.setImage(bmp);
                } while (cursorTEvent.moveToNext());
            }

            if(event != null) {
                TextView eventNameView = findViewById(R.id.input_eventName);
                eventNameView.setText(event.getTitle());
                TextView dateView = findViewById(R.id.input_date);
                dateView.setText(event.getDate());
                TextView descriptionView = findViewById(R.id.input_description);
                descriptionView.setText(event.getDescription());
                ImageView  imageView = findViewById(R.id.image_view);
                if(event.getImage()!=null){
                    imageView.setImageBitmap(event.getImage());
                }else{
                    imageView.setImageResource(R.drawable.no_image);
                }
            }


        } catch (Exception e) {
            Log.e(getLocalClassName(), "DBエラー発生", e);
        } finally {
            if (database != null) {
                database.close();
            }
            if (cursorTItem != null) {
                cursorTItem.close();
            }
        }







        imageView = findViewById(R.id.image_view);

        Button cameraButton = findViewById(R.id.camera_button);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, RESULT_CAMERA);
            }
        });


        editTextDate = findViewById(R.id.input_date);

        View add_btn = findViewById(R.id.btn_apply);
        add_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View vew){

                // タイトルと内容を取得してデータベースに登録する
                EditText editTextTitle = findViewById(R.id.input_eventName);
                EditText editTextContent = findViewById(R.id.input_description);
                EditText editTextDate = findViewById(R.id.input_date);

                String dob_var=(editTextDate.getText().toString());
                DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
                Date dateObject;

                SQLiteOpenHelper helper = new EventOpenHelper(EventEditActivity.this);
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

                    database.update("T_EVENT",  cv, "_event_id=?", new String[]{String.valueOf(eventId)});


                    String toastMessage = "イベントの更新が行われました";
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