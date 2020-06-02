package com.example.myapplication;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
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

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import 	java.lang.Object;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountActivity2 extends AppCompatActivity {
    public SQLdata DH = null;
    public SQLiteDatabase db;
    public ListView LV1,LV2;
    public Button b1,b2,b3,btnsearch,btnsearchall;
    public EditText editText1,editText2,editText;
    String id_text=null;
    Cursor cursor;
    int sum_id;
    SurfaceView surfaceView;
    TextView textView;
    CameraSource cameraSource;
    BarcodeDetector barcodeDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account2);

        surfaceView=(SurfaceView)findViewById(R.id.surfaceView2);
        textView=(TextView)findViewById(R.id.textView123);

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSource=new CameraSource.Builder(this,barcodeDetector)
                .setRequestedPreviewSize(300,300).build();
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback(){
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED)
                    return;
                try{
                    cameraSource.start(holder);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>(){

            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrCodes=detections.getDetectedItems();
                if(qrCodes.size()!=0){
                    textView.post(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(qrCodes.valueAt(0).displayValue);
                            Intent intent = new Intent();
                            ComponentName componentName = new ComponentName("com.example.myapplication","com.example.myapplication.TransactionRefundActivity");
                            Bundle bundle = new Bundle();
                            bundle.putString("KEY2",textView.getText().toString());
                            intent.putExtras(bundle);
                            intent.setComponent(componentName);
                            startActivity(intent);
                            try {
                                Thread.currentThread().sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
//*/
        DH = new SQLdata(this);
        db = DH.getWritableDatabase();
        LV1 = (ListView) findViewById(R.id.LV);
        select();//開啟就先載入資料
        editText1 = (EditText) findViewById(R.id.editText1);
        editText2 = (EditText) findViewById(R.id.editText2);
        b1 = (Button) findViewById(R.id.button5);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add(editText1.getText().toString(), editText2.getText().toString());
                select();
            }
        });
        //add("0"); //加入的資料
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
                update(id_text, editText1.getText().toString(), editText2.getText().toString());
                select();
            }
        });

        LV1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView te1 = (TextView) view.findViewById(R.id.text1);
                id_text = te1.getText().toString();
                TextView te2 = (TextView) view.findViewById(R.id.text2);
                editText1.setText(te2.getText().toString());
                TextView te3 = (TextView) view.findViewById(R.id.text3);
                editText2.setText(te3.getText().toString());
            }
        });

        cursor = getAll();
//        Button submit = (Button)findViewById(R.id.btn_123);
//        submit.setOnClickListener(new Button.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                test2();
//            }
//        });
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        db.close(); // 關閉資料庫
    }

///*        1st

    public void test2(){
        Intent intent = new Intent();
        ComponentName componentName = new ComponentName("com.example.myapplication","com.example.myapplication.TransactionActivity");
        Bundle bundle = new Bundle();
        bundle.putString("KEY2",textView.getText().toString());
        intent.putExtras(bundle);
        intent.setComponent(componentName);
        startActivity(intent);
    }
    ///*        1st
    public Cursor getAll () {
        Cursor cursor = db.rawQuery("SELECT _id,_title,_name FROM TB2019", null);
        return cursor;
    }

//    public Cursor get( long rowId) throws SQLException {
//        Cursor cursor = db.rawQuery("SELECT _id,_title,_name FROM TB2019 WHERE _id=" + rowId, null);
//        if (cursor.getCount() > 0)
//            cursor.moveToFirst();
//        else
//            Toast.makeText(getApplicationContext(), "can not found", Toast.LENGTH_SHORT).show();
//        return cursor;
//    }

//*/

    private void update(String id_text, String s, String n) {
        ContentValues values = new ContentValues();
        values.put("_title",s.toString());
        values.put("_name",n.toString());
        db.update("TB2019",values,"_id="+id_text,null);
//        select();
    }
    //    add(editText1.getText().toString(), editText2.getText().toString());
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
        sum_id = cursor.getCount();//getCount()資料總筆數

        for (int i = 0; i < sum_id; i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("_id", cursor.getString(0));
            item.put("_title", cursor.getString(1));
            item.put("_name", cursor.getString(2));
            items.add(item); //新增
            cursor.moveToNext(); //移下一筆資料
        }

        SimpleAdapter SA = new SimpleAdapter(this,
                items,
                R.layout.list_text,
                new String[]{"_id", "_title","_name"},
                new int[]{R.id.text1, R.id.text2,R.id.text3});
        LV1.setAdapter(SA);
    }
}