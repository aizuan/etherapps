package com.example.myapplication;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;

import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;


public class custmeraccount extends AppCompatActivity{
    public String mail ;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custmeraccount);
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    mail = user.getUid();
                    getCode();
                }
            }
        };

    }


    public void getCode() {
        ImageView ivCode = (ImageView) findViewById(R.id.surfaceView2);
        BarcodeEncoder encoder = new BarcodeEncoder();
        try {
            Bitmap bit = encoder.encodeBitmap(mail
                    , BarcodeFormat.QR_CODE, 250, 250);
            ivCode.setImageBitmap(bit);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
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
}