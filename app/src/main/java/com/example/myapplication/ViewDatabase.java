package com.example.myapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewDatabase extends AppCompatActivity{
//    private static final String TAG = "ViewDatabase";
//
//    private FirebaseDatabase mFirebaseDatabase;
//    private FirebaseAuth mAuth;
//    private FirebaseAuth.AuthStateListener mAuthListener;
//    private DatabaseReference myRef;
//    public String userID;
//    private ListView mListView;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.view_database_layout);
//
//        mListView = (ListView) findViewById(R.id.listview);
//
//        mAuth = FirebaseAuth.getInstance();
//        mFirebaseDatabase = FirebaseDatabase.getInstance();
//        myRef = mFirebaseDatabase.getReference();
//        FirebaseUser user = mAuth.getCurrentUser();
//        userID = user.getUid();
//
//        mAuthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if(user !=null) {
//                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
//                    toastMessage("Successfully signed in with: " + user.getEmail());
//                } else {
//                    Log.d(TAG, "onAuthStateChanged:signed_out");
//                    toastMessage("Successfully signed out.");
//                }
//            }
//        };
//
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                showData(dataSnapshot);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }
//
//    private void showData(DataSnapshot dataSnapshot) {
//        for (DataSnapshot ds: dataSnapshot.getChildren()){
//            UserInformation uInfo = new UserInformation();
//            uInfo.setEmail(ds.child(userID).getValue(UserInformation.class).getEmail());
//            uInfo.setNumber(ds.child(userID).getValue(UserInformation.class).getNumber());
//            uInfo.setPassword(ds.child(userID).getValue(UserInformation.class).getPassword());
//
//            Log.d(TAG,"showData: email: "+ uInfo.getEmail());
//            Log.d(TAG,"showData: number: "+ uInfo.getNumber());
//            Log.d(TAG,"showData: password: "+ uInfo.getPassword());
//
//            ArrayList<String> array = new ArrayList<>();
//            array.add(uInfo.getEmail());
//            array.add(uInfo.getNumber());
//            array.add(uInfo.getPassword());
//            ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_expandable_list_item_1,array);
//            mListView.setAdapter(adapter);
//        }
//    }
//
//    @Override
//    public void onStart(){
//        super.onStart();
//        mAuth.addAuthStateListener(mAuthListener);
//    }
//
//    @Override
//    public void onStop(){
//        super.onStop();
//        if(mAuthListener != null){
//            mAuth.removeAuthStateListener(mAuthListener);
//        }
//    }
//
//    private void toastMessage(String message){
//        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
//    }
}
