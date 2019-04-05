package com.example.bemyeye2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.Calendar;

import java.io.File;
import java.io.IOException;

public class uploadactivity extends AppCompatActivity {
    private EditText mRecord;
    private Button mRecordBtn;
    private MediaRecorder recorder;
    private String fileName=null;
    private TextView mRecordlabel;
    private StorageReference mStorageRef;
    private ProgressDialog mProgress;
    private boolean flag=false;
    private DatabaseReference myRef;
    private long lectureNo;
    private String lecno="0";
    private String filename2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uploadscreen);
        mRecord=(EditText)findViewById(R.id.textupload);
        mRecordBtn=(Button)findViewById(R.id.uploadbutton);
        mRecordlabel=(TextView)findViewById(R.id.recordlabel);
        fileName= Environment.getExternalStorageDirectory().getAbsolutePath();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        mProgress= new ProgressDialog(this);
        myRef = FirebaseDatabase.getInstance().getReference();
        Button btn8=(Button)findViewById(R.id.uploadbutton);
        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=true;
                fileName= Environment.getExternalStorageDirectory().getAbsolutePath();
                filename2=mRecord.getText().toString();
                fileName+="/"+filename2+".3gp";
                Log.d("filename",fileName);
                startRecording();
                myRef.child("users").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        lectureNo= dataSnapshot.getChildrenCount();
                        lectureNo+=1;
                        lecno=String.valueOf(lectureNo);

                        //  lectureNo=(String) dataSnapshot.getChildrenCount();
                        Log.d(dataSnapshot.getKey(),lecno + "");
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                mRecordlabel.setText("recording now......");
            }
        });

        Button btn9=(Button)findViewById(R.id.stopbutton);
        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fileName==".3gp"||flag==false){
                    Toast.makeText(uploadactivity.this, "Nothing recorded", Toast.LENGTH_SHORT).show();
                }else{
                    flag=false;
                    stopRecording();

                    //This is the code for the upload//

                    mRecordlabel.setText("Uploading Audio");
                    //mProgress.setMessage("Uploading Audio");
                    //mProgress.show();
                    final String userUid = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                    final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    StorageReference filepath = mStorageRef.child("Audio").child(userUid).child(fileName);

                    Uri file = Uri.fromFile(new File(fileName));

                    filepath.putFile(file)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // Get a URL to the uploaded content
                                   // Uri downloadUrl = taskSnapshot.getDownloadUrl().getResult();
                                    Task<Uri> downloadUrl= taskSnapshot.getMetadata().getReference().getDownloadUrl();


                                    //code to keep a track of the lecture numbers.



                        //    myRef.child("users").child(uid).child("Lecture"+lecno).setValue(downloadUrl.toString());
                                    myRef.child("users").child(Calendar.getInstance().getTime().toString()).child("Lecture").setValue(downloadUrl.toString());
                                    myRef.child("users").child(Calendar.getInstance().getTime().toString()).child("LectureName").setValue(filename2);

                           //saves the url of the image on the real time data base
                            //mProgress.dismiss();
                            mRecordlabel.setText("upload complete");

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle unsuccessful uploads
                                    // ...
                                }
                            });

                    //this is the code for upload//


                }

            }
        });
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e("recorder", "prepare() failed");
        }

        recorder.start();
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }
}
