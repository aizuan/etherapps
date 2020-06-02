package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class Main3Activity extends AppCompatActivity {
    private Button mBtnAccount;
    private Button mBtnBalance;
    private Button mBtnRecord;
    private Button mBtnQRcode;
    private Button mBtnTranslate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        mBtnAccount = this.<Button>findViewById(R.id.btn_a);
        mBtnBalance = this.<Button>findViewById(R.id.btn_b);
        mBtnRecord = this.<Button>findViewById(R.id.btn_r);
        mBtnQRcode = this.<Button>findViewById(R.id.btn_q);
        mBtnTranslate = this.<Button>findViewById(R.id.btn_t);
        setListeners();
    }

    private void setListeners() {
        OnClick onClick = new OnClick();
        mBtnAccount.setOnClickListener(onClick);
        mBtnBalance.setOnClickListener(onClick);
        mBtnRecord.setOnClickListener(onClick);
        mBtnQRcode.setOnClickListener(onClick);
        mBtnTranslate.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.btn_a:
                    intent = new Intent(Main3Activity.this, AccountActivity.class);

                    break;
                case R.id.btn_b:
                    intent = new Intent(Main3Activity.this, BalacneActivity.class);

                    break;
                case R.id.btn_r:
                    intent = new Intent(Main3Activity.this, RecordActivity.class);

                    break;
                case R.id.btn_q:
                    intent = new Intent(Main3Activity.this, QRcodeActivity.class);

                    break;
                case R.id.btn_t:
                    intent = new Intent(Main3Activity.this, TranslateActivity.class);

                    break;
            }
            startActivity(intent);
        }
    }
}

