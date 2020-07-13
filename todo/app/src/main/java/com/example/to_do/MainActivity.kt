package com.example.to_do

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.util.SparseBooleanArray
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.ListView
import androidx.core.view.size
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity() {

    private val dbName: String = "SampleDB"
    private val tableName: String = "SampleTable"
    private val dbVersion: Int = 1
    private var todoArray: ArrayList<ToDo> = arrayListOf()
    private var arrayListId: ArrayList<String> = arrayListOf()
    private var arrayListName: ArrayList<String> = arrayListOf()
    private var arrayListType: ArrayList<Int> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       selectData()

        // Initializing the array lists and the adapter
//        var itemlist = arrayListOf<String>()
        var adapter =ArrayAdapter<String>(this,
            android.R.layout.simple_list_item_multiple_choice
            , arrayListName)
//            , itemlist)

       val listview = findViewById<ListView>(R.id.listView)
       listview.adapter = adapter
//       listview.adapter = selectData()

        add.setOnClickListener{
            arrayListName.add(editText.text.toString())
            adapter.notifyDataSetChanged()

            insertData(listview.size.toString(),editText.text.toString(),1)

            editText.text.clear()
        }

        delete.setOnClickListener{

            val position: SparseBooleanArray = listView.checkedItemPositions
            val count = listView.count
            var item = count -1
            while (item >= 0){
                if(position.get(item))
                {
                    adapter.remove(arrayListName.get(item))
                    deleteData(item.toString())
                }
                item--
            }
            position.clear()
            adapter.notifyDataSetChanged()
        }


        done.setOnClickListener{

            val position: SparseBooleanArray = listView.checkedItemPositions
            val count = listView.count
            var item = count -1
            while (item >= 0){
                if(position.get(item))
                {
                    updateData(item.toString(),arrayListName.get(item),2)
                    adapter.remove(arrayListName.get(item))
                }
                item--
            }
            position.clear()
            adapter.notifyDataSetChanged()
        }



        listView.setOnItemClickListener{
        adapterView, view,i,l ->
            android.widget.Toast.makeText(this,"You Selected the item -->"
                    +arrayListName.get(i),android.widget.Toast.LENGTH_SHORT).show()
        }


    }


    private fun insertData(id: String, name: String, type: Int) {
        try {
            val dbHelper = ToDoDBHelper(applicationContext, dbName, null, dbVersion);
            val database = dbHelper.writableDatabase

            val values = ContentValues()
            values.put("id", id)
            values.put("name", name)
            values.put("type", type)

            database.insertOrThrow(tableName, null, values)
        }catch(exception: Exception) {
            Log.e("insertData", exception.toString())
        }
    }

    private fun selectData() {
        try {
            arrayListId.clear();arrayListName.clear();arrayListType.clear();

            val dbHelper = ToDoDBHelper(applicationContext, dbName, null, dbVersion)
            val database = dbHelper.readableDatabase

            val sql = "select id, name, type from " + tableName + " where type in (1, 2) order by id"

            val cursor = database.rawQuery(sql, null)
            if (cursor.count > 0) {
                cursor.moveToFirst()
                while (!cursor.isAfterLast) {
//                    Todo todo = new Todo(cursor.getC)
                    arrayListId.add(cursor.getString(0))
                    arrayListName.add(cursor.getString(1))
                    arrayListType.add(cursor.getInt(2))
                    cursor.moveToNext()
                }
            }
        }catch(exception: Exception) {
            Log.e("selectData", exception.toString());
        }
    }

    private fun deleteData(whereId: String) {
        try {
            val dbHelper = ToDoDBHelper(applicationContext, dbName, null, dbVersion);
            val database = dbHelper.writableDatabase

            val whereClauses = "id = ?"
            val whereArgs = arrayOf(whereId)
            database.delete(tableName, whereClauses, whereArgs)
        }catch(exception: Exception) {
            Log.e("deleteData", exception.toString())
        }
    }

    private fun updateData(whereId: String, newName: String, newType: Int) {
        try {
            val dbHelper = ToDoDBHelper(applicationContext, dbName, null, dbVersion);
            val database = dbHelper.writableDatabase

            val values = ContentValues()
            values.put("name", newName)
            values.put("type", newType)

            val whereClauses = "id = ?"
            val whereArgs = arrayOf(whereId)
            database.update(tableName, values, whereClauses, whereArgs)
        }catch(exception: Exception) {
            Log.e("updateData", exception.toString())
        }
    }

}
