package com.example.myapplication;

import android.os.Bundle;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.content.ContentValues;
import android.support.v7.app.AppCompatActivity;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import  java.lang.Object;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountActivity extends AppCompatActivity {
    public SQLdata DH = null;
    public SQLiteDatabase db;
    public ListView LV1,LV2;
    public Button b1,b2,b3,btnsearch,btnsearchall;
    public EditText t1,t2,editText;
    public TextView te1,te2,te3;
    String id_text=null;
    Cursor cursor;
    int sum_id;
    String [] value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        value = new String[20];

///*        1st
        btnsearch = (Button) findViewById(R.id.btnfind);
        btnsearchall = (Button) findViewById(R.id.searchall);
        LV2 = (ListView) findViewById(R.id.lvfind);
        editText = (EditText) findViewById(R.id.etfind);
//*/
        DH = new SQLdata(this);
        db = DH.getWritableDatabase();
        LV1 = (ListView) findViewById(R.id.LV);
        select();//開啟就先載入資料
        t1 = (EditText) findViewById(R.id.editText1);
        t2 = (EditText) findViewById(R.id.editText2);
        b1 = (Button) findViewById(R.id.button5);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add(t1.getText().toString(), t2.getText().toString());
                select();
            }
        });

        b2 = (Button) findViewById(R.id.button3);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                del(id_text);
                select();
                id_text = null;
            }
        });

        b3 = (Button) findViewById(R.id.button4);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update(id_text, t1.getText().toString(), t2.getText().toString());
            }
        });

        LV1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                te1 = (TextView) view.findViewById(R.id.text1);
                id_text = te1.getText().toString();
                te2 = (TextView) view.findViewById(R.id.text2);
                t1.setText(te2.getText().toString());
                te3 = (TextView) view.findViewById(R.id.text3);
                t2.setText(te3.getText().toString());
            }
        });

        btnsearchall.setOnClickListener(myListener);
        btnsearch.setOnClickListener(myListener);
//        LV2.setOnItemClickListener(listview01Listener);
//        */
        cursor = getAll();
        UpdataAdapter(cursor);
    }

//    private ListView.OnItemClickListener listview01Listener=
//            new ListView.OnItemClickListener(){
//                public void onItemClick(AdapterView<?> parent, View v,
//                                        int position, long id) {
//                    cursor.moveToPosition(position);
//                    Cursor c=get(id);
//                    String s= "id=" + id + "\r\n" + "name=" + c.getString(1) + "\r\n" + "price=" + c.getInt(2);
//                    Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
//                }
//            };

    @Override
    protected void onDestroy(){
        super.onDestroy();
        db.close(); // 關閉資料庫
    }

///*        1st

    private Button.OnClickListener myListener = new Button.OnClickListener() {
        public void onClick(View v) {
            try {
                switch (v.getId()) {
                    case R.id.btnfind:
                    {
                        long id = Integer.parseInt(editText.getText().toString());
                        cursor = get(id);
                        UpdataAdapter(cursor);
                        break;
                    }
                    case R.id.searchall:
                    {
                        cursor = getAll();
                        UpdataAdapter(cursor);
                        break;
                    }
                }
            } catch (Exception err) {
                Toast.makeText(getApplicationContext(), "can not found", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public void UpdataAdapter (Cursor cursor){
        if (cursor != null && cursor.getCount() >= 0) {
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                    this,
                    R.layout.layout,
                    cursor,
                    new String[]{"_id","_title", "_name"},
                    new int[]{R.id.txtId, R.id.txtName,R.id.txtPrice},
                    0);
            LV2.setAdapter(adapter);
        }
    }
//    */

    ///*        1st
    public Cursor getAll () {
        Cursor cursor = db.rawQuery("SELECT _id,_title,_name FROM TB2019", null);
        return cursor;
    }

    public Cursor get( long rowId) throws SQLException {
        Cursor cursor = db.rawQuery("SELECT _id,_title,_name FROM TB2019 WHERE _id=" + rowId, null);
        if (cursor.getCount() > 0)
            cursor.moveToFirst();
        else
            Toast.makeText(getApplicationContext(), "can not found", Toast.LENGTH_SHORT).show();
        return cursor;
    }

//*/

    private void update(String id_text, String s, String n) {
        ContentValues values = new ContentValues();
        values.put("_title",s.toString());
        values.put("_name",n.toString());
        db.update("TB2019",values,"_id="+id_text,null);
        select();
    }

    private void add(String s,String n) {
        db = DH.getWritableDatabase(); //getWritableDatabase 用於讀取和寫入的資料庫
        ContentValues values = new ContentValues();
        values.put("_id", String.valueOf(sum_id+1));
        values.put("_title", s.toString());//載入title
        values.put("_name", n.toString());//載入name
        db.insert("TB2019", null, values);
        select();
    }

    private void del(String id){
        db.delete("TB2019","_id="+id_text,null);
        String up_del ="update TB2019 set _id=_id-1 where _id>"+id_text;
        db.execSQL(up_del);
    }

    private void select(){
        //查詢資料庫並載入
        Cursor cursor = db.query("TB2019", new String[]{"_id", "_title","_name"}, null, null, null, null, null); //Cursor 為創建資料集
        List<Map<String, Object>> items = new ArrayList<Map<String, Object>>(); //List建立資料活動
        cursor.moveToFirst(); //創建資料集後moveToFirst()移動到第一筆
        //叫出資料庫的資料
        sum_id = cursor.getCount();//getCount()資料總筆數
        for (int i = 0; i < sum_id; i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("_id", cursor.getString(0));
            item.put("_title", cursor.getString(1));
            item.put("_name", cursor.getString(2));
            items.add(item); //新增
            cursor.moveToNext(); //移下一筆資料
        }
        SimpleAdapter SA = new SimpleAdapter(this, items,
                R.layout.list_text,
                new String[]{"_id", "_title","_name"},
                new int[]{R.id.text1, R.id.text2,R.id.text3}); //android.R.layout.simple_expandable_list_item_2 為內建排版元件
        LV1.setAdapter(SA);
    }
}