package com.example.myapplication;

import android.content.ComponentName;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Transaction2Activity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    public String mail ;
    private FirebaseAuth.AuthStateListener mAuthListener;
    ImageButton mBtnAccount,mBtnBalance,mBtnRecord,mBtnSwitch,shoot;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction2);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    mail = user.getUid();
                }
            }
        };
        mAuth = FirebaseAuth.getInstance();

        mBtnAccount = this.<ImageButton>findViewById(R.id.b1);
        mBtnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Transaction2Activity.this,custmeraccount.class);
                startActivity(intent);
            }
        });
        mBtnBalance = this.<ImageButton>findViewById(R.id.b2);
        mBtnBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Transaction2Activity.this,RecordActivity.class);
                startActivity(intent);
            }
        });
        mBtnRecord = this.<ImageButton>findViewById(R.id.b3);
        mBtnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                ComponentName componentName = new ComponentName("org.walleth","org.walleth.activities.MainActivity");
                intent.setComponent(componentName);
                startActivity(intent);
            }
        });
        mBtnSwitch = this.<ImageButton>findViewById(R.id.b4);
        mBtnSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Transaction2Activity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        mBtnSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(Transaction2Activity.this,MainActivity.class);
                toastMessage("Successfully signed out.");
                startActivity(intent);
            }
        });
        shoot = this.<ImageButton>findViewById(R.id.iv_1);
        shoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                ComponentName componentName = new ComponentName("org.walleth","org.walleth.activities.RequestActivity");
                intent.setComponent(componentName);
                startActivity(intent);
            }
        });
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

    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}