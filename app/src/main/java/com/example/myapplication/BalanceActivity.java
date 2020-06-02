package com.example.myapplication;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BalanceActivity extends AppCompatActivity {
    TextView displaySharedValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);

        displaySharedValue = (TextView)findViewById(R.id.tv_main);
        try {
            Context con = createPackageContext("com.example.tts", 0);
            SharedPreferences pref = con.getSharedPreferences(
                    "demostring", Context.MODE_PRIVATE);
            String data = pref.getString("demostring", "No Value");
            displaySharedValue.setText(data);

        } catch (PackageManager.NameNotFoundException e) {
            Log.e("Not data shared", e.toString());
        }
    }
}
