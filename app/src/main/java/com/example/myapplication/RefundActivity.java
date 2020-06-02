package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

public class RefundActivity extends AppCompatActivity {
    SurfaceView surfaceView;
    TextView textView,totalprice;
    CameraSource cameraSource;
    BarcodeDetector barcodeDetector;
    ArrayAdapter<String> arrayAdapter, arrayAdapter2, arrayAdapter3;
    ListView LVRefund;
    ImageButton back;
    private FirebaseAuth mAuth;
    Button t1;
    public String userid, a, b, c;
    private FirebaseUser currentuser;
    public double sum=0;
    public double value;
    public String text;
    String url="https://etherscan.io/chart/etherprice";
    Elements title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund);

        totalprice = (TextView)findViewById(R.id.totalPrice);
        back = (ImageButton)findViewById(R.id.back);
        textView = (TextView)findViewById(R.id.TVID);
        surfaceView=(SurfaceView)findViewById(R.id.qrID);
        LVRefund = (ListView)findViewById(R.id.LVRefund);

        t1=(Button)findViewById(R.id.send);
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                go2Login();
            }
        });


        back = this.<ImageButton>findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RefundActivity.this,TransactionActivity.class);
                startActivity(intent);
            }
        });
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> keys = new ArrayList<>();
        ArrayList<String> items = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, names);
        arrayAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, keys);
        arrayAdapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, items);

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
                            userid= qrCodes.valueAt(0).displayValue;
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("交易").child(userid);
                            ref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    arrayAdapter.clear();
                                    Iterator<DataSnapshot> dit = dataSnapshot.getChildren().iterator();
                                    while (dit.hasNext()) {
                                        DataSnapshot s = dit.next();
                                        a = s.getKey();
                                        names.add(a);
                                    }
                                    LVRefund.setAdapter(arrayAdapter);

                                    LVRefund.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                            String selected_item = String.valueOf(adapterView.getItemAtPosition(position));
                                            DatabaseReference ref2 = ref.child(selected_item);
                                            ref2.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    Iterator<DataSnapshot> dit = dataSnapshot.getChildren().iterator();
                                                    while (dit.hasNext()) {
                                                        DataSnapshot s = dit.next();
                                                        b = s.getKey();
                                                        keys.add(b);
                                                    }
                                                    LVRefund.setAdapter(arrayAdapter2);

                                                    LVRefund.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                        @Override
                                                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                                            String selected_item = String.valueOf(adapterView.getItemAtPosition(position));
                                                            DatabaseReference ref3 = ref2.child(selected_item);
                                                            ref3.addValueEventListener(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                    Iterator<DataSnapshot> dit = dataSnapshot.getChildren().iterator();
                                                                    while (dit.hasNext()) {
                                                                        DataSnapshot s = dit.next();
                                                                        c = s.getValue().toString();
                                                                        items.add(getstring(c));
                                                                    }
                                                                    LVRefund.setAdapter(arrayAdapter3);

                                                                    LVRefund.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                                        @Override
                                                                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                                                            String item_price = String.valueOf(adapterView.getItemAtPosition(position));
                                                                            String a = item_price.substring(item_price.indexOf("ce")+4);
                                                                            TotalServings(Double.valueOf(a));
                                                                        }
                                                                    });
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                }
                                                            });
                                                        }
                                                    });

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
//                            Intent intent = new Intent();
//                            ComponentName componentName = new ComponentName("com.example.myapplication","com.example.myapplication.TransactionActivity");
//                            Bundle bundle = new Bundle();
//                            bundle.putString("KEY2",textView.getText().toString());
//                            intent.putExtras(bundle);
//                            intent.setComponent(componentName);
//                            startActivity(intent);
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
    @Override
    protected void onResume() {
        super.onResume();
        getvalue();
    }

    public double sumdata(double num){
        sum = sum+num;
        System.out.println("TESTsum:" + sum);
        return sum;
    }

    public void TotalServings(double number){
        number = number/30;
        number = number/value;
        if(number!=0) {
            BigDecimal b = new BigDecimal(number);
            number = b.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        sum = sum + number;
        totalprice.setText(String.valueOf(sum));
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
        String sentvalue = numberchange(sum);
        bundle.putString("KEY3", sentvalue);
        intent.putExtras(bundle);
        intent.setComponent(componentName);
        startActivity(intent);
    }

    public String getstring(String item){
        String a = item.substring(item.indexOf("t")+4,item.indexOf("}"));
        String b = item.substring(item.indexOf("_")+7,item.indexOf(","));
        return  "Item : " + a + "\n" + "Price : " + b;
    }
}
