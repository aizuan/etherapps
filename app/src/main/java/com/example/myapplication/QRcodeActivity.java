package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class QRcodeActivity extends AppCompatActivity {
    Button bopen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        bopen=findViewById(R.id.b_1);
        bopen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClassName("org.walleth", "org.walleth.activities.CreateTransactionActivity");
                startActivity(intent);
            }
        });
    }
}