package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.Object;

public class TransactionRefundActivity extends AppCompatActivity {
    public SQLdata DH = null;
    public SQLiteDatabase db;
    SurfaceView surfaceView;
    EditText editText,editText1,editText2;
    TextView totalPrice;
    Button cart ,del,clear,t1;
    ImageButton mBtnAccount,trans;
    CameraSource cameraSource;
    BarcodeDetector barcodeDetector;
    Cursor cursor;
    String uid=null;
    ListView LV;
    int sum_id;
    double sum=0;
    String id_text=null;
    public String mail ;
    public double value;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    public String text;
    String url="https://etherscan.io/chart/etherprice";
    Elements title;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_refund);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    mail = user.getUid();
                }
            }
        };

        mBtnAccount = this.<ImageButton>findViewById(R.id.b1);
        mBtnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransactionRefundActivity.this,AccountActivity2.class);
                startActivity(intent);
            }
        });
//        mBtnBalance = this.<ImageButton>findViewById(R.id.b2);
//        mBtnBalance.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(TransactionRefundActivity.this,RecordActivity2.class);
//                startActivity(intent);
//            }
//        });
//        mBtnRecord = this.<ImageButton>findViewById(R.id.b3);
//        mBtnRecord.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                ComponentName componentName = new ComponentName("org.walleth","org.walleth.activities.MainActivity");
//                intent.setComponent(componentName);
//                startActivity(intent);
//            }
//        });
//        mBtnSwitch = this.<ImageButton>findViewById(R.id.b4);

        mAuth = FirebaseAuth.getInstance();
        getPermissionsCamera();
        surfaceView=(SurfaceView)findViewById(R.id.surfaceView);
        editText=findViewById(R.id.editText);
        editText1=findViewById(R.id.editText1);
        editText2=findViewById(R.id.editText2);
        totalPrice=(TextView)findViewById(R.id.totalPrice);

        LV=(ListView)findViewById(R.id.LV);
        DH = new SQLdata(this);
        db = DH.getWritableDatabase();
        select();

        trans = this.<ImageButton>findViewById(R.id.trans);
        trans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransactionRefundActivity.this,TransactionActivity.class);
                startActivity(intent);
            }
        });

        cart=(Button)findViewById(R.id.cart);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add(editText1.getText().toString(), editText2.getText().toString());
                select();
                TotalServings();
            }
        });

        del = (Button) findViewById(R.id.del);
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                del(id_text);
                select();
                id_text = null;
                TotalServings();
            }
        });

        clear=(Button) findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.delete("TAB", null, null);
                select();
                TotalServings();
            }
        });

        t1=(Button)findViewById(R.id.t1);
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonarr();
                go2Login();
            }
        });



        barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSource=new CameraSource.Builder(this,barcodeDetector).setRequestedPreviewSize(300,300).build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback(){
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if(ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.CAMERA)
                        !=PackageManager.PERMISSION_GRANTED)
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
                    editText.post(new Runnable() {
                        @Override
                        public void run() {
                            editText.setText(qrCodes.valueAt(0).displayValue);
                            long id = Integer.parseInt(editText.getText().toString());
                            cursor = get(id);
                            UpdataAdapter(cursor);
                        }
                    });
                }
            }
        });

//        mBtnSwitch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mAuth.signOut();
//                db.delete("TAB", null, null);
//                Intent intent = new Intent(TransactionRefundActivity.this,MainActivity.class);
//                toastMessage("Successfully signed out.");
//                startActivity(intent);
//            }
//        });

        LV.setAdapter(null);
        LV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView te1 = (TextView) view.findViewById(R.id.txtId);
                id_text = te1.getText().toString();
                TextView te2 = (TextView) view.findViewById(R.id.txtName);
                editText1.setText(te2.getText().toString());
                TextView te3 = (TextView) view.findViewById(R.id.txtPrice);
                editText2.setText(te3.getText().toString());
            }
        });
    }

    public void getvalue() {
        new Thread(runnable).start();
    }
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                Document doc = Jsoup.connect(url).get();
                title = doc.select("#ethPrice").select("div").select("span");
                text = title.toString();
                int a = text.indexOf("$");
                int b = text.indexOf("</");
                text = text.substring(a + 1, b);
                value = Double.parseDouble(text);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            handler.sendEmptyMessage(0);
        }
    };


    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    class Details{
        public String _id,_item,_price;
        public Details(String id,String item,String price){
            this._id = id;
            this._item = item;
            this._price = price;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        select();
        db.delete("TAB", null, null);
        getvalue();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        super.onStop();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        db.close(); // 關閉資料庫
    }


    private void getPermissionsCamera() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},1);
        }
    }

    public double TotalServings(){

        DecimalFormat df = new DecimalFormat("#.#######");
        double serving = 0;
        Cursor cursor = db.rawQuery(
                "SELECT SUM(_name)as double FROM TAB", null);
        if(cursor.moveToFirst()) {
            serving = cursor.getDouble(0);
            String str = df.format(serving);
            serving=Double.valueOf(str);
        }
        totalPrice.setText(String.valueOf(serving));
        sum =serving;
        serving = serving/30;
        serving = serving/value;
        if(serving!=0) {
            BigDecimal b = new BigDecimal(serving);
            serving = b.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        totalPrice.setText(String.valueOf(serving));
        return serving;
    }
    public void UpdataAdapter (Cursor cursor){
        if (cursor != null && cursor.getCount() >= 0) {
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                    R.layout.layout,
                    cursor,
                    new String[]{"_id","_title", "_name"},
                    new int[]{R.id.txtId,R.id.txtName,R.id.txtPrice},
                    0);
            LV.setAdapter(adapter);
        }
    }

    public void jsonarr() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        String dateTime = simpleDateFormat.format(calendar.getTime());
        SimpleDateFormat formatter = new SimpleDateFormat   ("yyyy/MM/dd");
        Date curDate =  new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        List<Details> dataList = new ArrayList<Details>();
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            String value = bundle.getString("KEY2");
            uid = value;
        }else{
            toastMessage("No ID detective!!!");
            Intent intent = new Intent(TransactionRefundActivity.this,AccountActivity.class);
            startActivity(intent);
        }
        DatabaseReference ContactsRef = FirebaseDatabase.getInstance().getReference("退款").child(uid);

        Cursor cursor = db.rawQuery("SELECT * FROM TAB ", null);
        if (cursor.moveToFirst()) {
            do {
                dataList.add(new Details(cursor.getString(cursor.getColumnIndex("_id")),
                        cursor.getString(cursor.getColumnIndex("_title")),
                        cursor.getString(cursor.getColumnIndex("_name"))));
            } while (cursor.moveToNext());
        }

        if (dataList.size() > 0) {
            DatabaseReference ref = ContactsRef.child(str).child(dateTime);
            for (Details d : dataList) {
                ref.push().setValue(d);
            }
        }
    }

    public Cursor get( long rowId) throws SQLException {
        Cursor cursor = db.rawQuery("SELECT _id,_title,_name FROM TB2019 WHERE _id=" + rowId, null);
        if (cursor.getCount() > 0)
            cursor.moveToNext();
        else
            Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
        return cursor;
    }

    private void add(String s,String n) {
        db = DH.getWritableDatabase(); //getWritableDatabase 用於讀取和寫入的資料庫
        ContentValues values = new ContentValues();
        values.put("_id", String.valueOf(sum_id+1));
        values.put("_title", s.toString());//載入title
        values.put("_name", n.toString());//載入name
        db.insert("TAB", null, values);
        select();
    }

    private void del(String id){
        db.delete("TAB","_id="+id_text,null);
        String up_del ="update TAB set _id=_id-1 where _id>"+id_text;
        db.execSQL(up_del);
    }

    private void select(){
        //查詢資料庫並載入
        Cursor cursor = db.query("TAB", new String[]{"_id", "_title","_name"}, null, null, null, null, null); //Cursor 為創建資料集
        List<Map<String, Object>> commodity = new ArrayList<Map<String, Object>>(); //List建立資料活動
        cursor.moveToFirst(); //創建資料集後moveToFirst()移動到第一筆
        sum_id = cursor.getCount();//getCount()資料總筆數

        for (int i = 0; i < sum_id; i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("_id", cursor.getString(0));
            item.put("_title", cursor.getString(1));
            item.put("_name", cursor.getString(2));
            commodity.add(item); //新增
            cursor.moveToNext(); //移下一筆資料
        }

        SimpleAdapter SA = new SimpleAdapter(this,
                commodity,
                R.layout.layout,
                new String[]{"_id", "_title","_name"},
                new int[]{R.id.txtId, R.id.txtName,R.id.txtPrice});
        LV.setAdapter(SA);
    }

    public String numberchange(double  num){
        int a = (int)num;
        String b = String.valueOf(num) ;
        int c = b.length()-b.indexOf(".")-1;
        int len =b.length();
        b = a + b.substring(b.indexOf(".")+1,len);
        return num+"|"+b+"/"+c;
    }

    public void go2Login(){
        Intent intent = new Intent();
        ComponentName componentName = new ComponentName("org.walleth","org.walleth.activities.CreateTransactionActivity");
        Bundle bundle = new Bundle();
        String sentvalue = numberchange(TotalServings());
        bundle.putString("KEY3", sentvalue);
        intent.putExtras(bundle);
        intent.setComponent(componentName);
        startActivity(intent);
    }

    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}