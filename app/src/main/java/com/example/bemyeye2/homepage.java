package com.example.bemyeye2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.net.URL;
import java.util.ArrayList;

public class homepage extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    private ArrayList<String> files;
    private ArrayList<String> fileu;
    private ListView listView;
   /* public static String getFileNameFromUrl(String url) {

        String urlString = url;

        return urlString.substring(urlString.lastIndexOf('/') + 1).split("\\?")[0].split("#")[0];
    }*/

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
        listView=(ListView)findViewById(R.id.listview1);
       // final ArrayList<String> tables= new ArrayList<>(50);
        //final ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(homepage.this,android.R.layout.simple_list_item_activated_1,tables);

        FirebaseDatabase.getInstance().getReference().child("users")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        files=new ArrayList<>(200);
                        fileu=new ArrayList<>(200);
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            final User user = snapshot.getValue(User.class);
                         //  System.out.println(user.Lecture1);
                           // Toast.makeText(homepage.this, user.Lecture, Toast.LENGTH_SHORT).show();
                            Log.d("lecture",user.Lecture);
                            Log.d("LectureName",user.LectureName);
                            files.add(user.LectureName);
                            fileu.add(user.Lecture);
                            ArrayAdapter<String> arrayAdapter= new ArrayAdapter<String>(homepage.this,android.R.layout.simple_list_item_activated_1,files);
                            // listView.setAdapter(arrayAdapter);

                            //arrayAdapter.notifyDataSetChanged();//updating the array adapter each time inside the onProgress listener


                            listView.setAdapter(arrayAdapter); //set the adapter to your list view
                          //  files.add(user.LectureName);

                         // String filename=  URLUtil.guessFileName(user.Lecture, null, null);
                         //   String filename= getFileNameFromUrl(user.Lecture);
                        //  Log.d("filename",filename);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                     Log.d("url-clicked",fileu.get(position));
                                    //Intent i = new Intent(getActivity(), DiscussAddValu.class);
                                   // startActivity(i);
                                }
                            });
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
       // files.add("hello");


    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if(currentUser!=null){
            Toast.makeText(this, "Authenticated", Toast.LENGTH_SHORT).show();
        }
    }
}
