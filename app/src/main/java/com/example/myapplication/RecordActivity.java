package com.example.myapplication;

import android.app.DownloadManager;
import android.os.Bundle;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.content.ContentValues;
import android.support.v7.app.AppCompatActivity;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import 	java.lang.Object;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RecordActivity extends AppCompatActivity {
        ArrayAdapter<String> arrayAdapter, arrayAdapter2, arrayAdapter3;
        ListView lv;
        private FirebaseAuth mAuth;
        public String userid, a, b, c;
        private FirebaseUser currentuser;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_record);

            mAuth = FirebaseAuth.getInstance();
            currentuser=mAuth.getCurrentUser();
            userid = currentuser.getUid();

            lv = (ListView) findViewById(R.id.lv);
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("交易").child(userid);
            ArrayList<String> names = new ArrayList<>();
            ArrayList<String> keys = new ArrayList<>();
            ArrayList<String> items = new ArrayList<>();
            arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, names);
            arrayAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, keys);
            arrayAdapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, items);
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
                    lv.setAdapter(arrayAdapter);

                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                            String selected_item= String.valueOf(adapterView.getItemAtPosition(position));
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
                                    lv.setAdapter(arrayAdapter2);

                                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                            String selected_item= String.valueOf(adapterView.getItemAtPosition(position));
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
                                                    lv.setAdapter(arrayAdapter3);
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
                public void onCancelled(DatabaseError databaseError) {
                    //handle databaseError
                }
            });
        }
    public String getstring(String item){
        String a = item.substring(item.indexOf("t")+4,item.indexOf("}"));
        String b = item.substring(item.indexOf("_")+7,item.indexOf(","));
        return "Item : " + a + "\n" + "Price : " + b;
    }
}

//System.out.println("TEST1" + dataSnapshot.toString());
//        System.out.println("TEST KEY" + dataSnapshot.getChildren());
////                    keys.add(snapshot.getKey());
//        Iterator<DataSnapshot> dit = dataSnapshot.getChildren().iterator();
//        while (dit.hasNext()) {
//        DataSnapshot s = dit.next();
//        a = s.getKey();
//        System.out.println("TESTaaa:"+ s);
//        System.out.println("TESTkey:" + s.getKey());
//                        Iterator<DataSnapshot> ss = s.getChildren().iterator();
//                        System.out.println("TESTss:" + s);
//                         while(ss.hasNext()) {
//                             DataSnapshot  sss  = ss.next();
//                             System.out.println("TESTkey sss:"+sss.getKey());
//                             Iterator<DataSnapshot> sssit = sss.getChildren().iterator();
//                             while (sssit.hasNext()) {
//                                 DataSnapshot s4 =  sssit.next();
//                                 System.out.println("TESTkey s4   :"+s4.getKey());
//                                 System.out.println("TESTkey s4   :"+s4.getValue());
//                                 a = s4.getValue().toString();
//                             }
//                         }