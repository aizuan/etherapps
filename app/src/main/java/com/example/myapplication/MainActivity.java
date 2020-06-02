package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    public Boolean a=true;
    // UI references.
    public TextView textView;
    private EditText mEmail, mPassword;
    private Button btnSignIn,btnSignOut,btnViewDatabase,btnSignUp;
    ImageButton btnImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEmail = (EditText)findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        btnSignIn = (Button) findViewById(R.id.email_sign_in_button);
        btnSignOut = (Button) findViewById(R.id.email_sign_out_button);
        btnViewDatabase = (Button) findViewById(R.id.view_items_screen);
        btnSignUp = (Button)findViewById(R.id.btn_sign);
        textView = (TextView)findViewById(R.id.tv);
        btnImage = (ImageButton)findViewById(R.id.iv_1);
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                a = loadData();
                if(a==true) {
                    if (user != null) {
                        // User is signed in
                        Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                        toastMessage("Successfully signed in with: " + user.getEmail());
                        Intent intent = new Intent(MainActivity.this, TransactionActivity.class);
                        startActivity(intent);
                        Toast.makeText(MainActivity.this, "登入成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // User is signed out
                        Log.d(TAG, "onAuthStateChanged:signed_out");
                        //toastMessage("Successfully signed out.");
                    }
                }else {
                    if (user != null) {
                        // User is signed in
                        Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                        toastMessage("Successfully signed in with: " + user.getEmail());
                        Intent intent = new Intent(MainActivity.this, Transaction2Activity.class);
                        startActivity(intent);
                        Toast.makeText(MainActivity.this, "登入成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // User is signed out
                        Log.d(TAG, "onAuthStateChanged:signed_out");
                        //toastMessage("Successfully signed out.");
                    }
                }
            }
        };

        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(a==true){
                    a=false;
                    toastMessage("For Customer");
                }else {
                    a=true;
                    toastMessage("For Merchant");
                }
                saveData();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString();
                String pass = mPassword.getText().toString();
                if(!email.equals("") && !pass.equals("")){
                    mAuth.signInWithEmailAndPassword(email,pass);
                }else{
                    toastMessage("You didn't fill in all the fields.");
                }

                if(email.indexOf("@")<0) {
                    toastMessage("Wrong Email or Password");
                }
            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                toastMessage("Signing Out...");
            }
        });

        btnViewDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ViewDatabase.class);
                startActivity(intent);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void toastMessage(String message){

        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    public void saveData() {
        SharedPreferences pref = getPreferences(MODE_PRIVATE);
        pref.edit()
                .putBoolean("USER", a)
                .commit();
    }

    public Boolean  loadData() {
       Boolean userid = getPreferences(MODE_PRIVATE)
                .getBoolean("USER", true);
        return userid;

    }


}