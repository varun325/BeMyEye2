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
        mRecord=(EditText)findViewById(R.id.textupload); //linking the text that has to be upload aka file name
        mRecordBtn=(Button)findViewById(R.id.uploadbutton); //linking the upload button
        mRecordlabel=(TextView)findViewById(R.id.recordlabel);//the notification text view
        fileName= Environment.getExternalStorageDirectory().getAbsolutePath();//get the file path from the directory in which we stored the file locally
        mStorageRef = FirebaseStorage.getInstance().getReference();//assigning the value to the fire base storage reference

        mProgress= new ProgressDialog(this);
        myRef = FirebaseDatabase.getInstance().getReference(); //assigning the value to the firebase real time database reference
        Button btn8=(Button)findViewById(R.id.uploadbutton); //linking the upload button
        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=true;
                fileName= Environment.getExternalStorageDirectory().getAbsolutePath();//getting the excat path where the file will be stored
                filename2=mRecord.getText().toString();//the file name
                fileName+="/"+filename2+".mp3";//appending the path and file name
                Log.d("filename",fileName);
                startRecording();//start the recording


                //code below has been depricated and isn't being used any more////
                myRef.child("users").addChildEventListener(new ChildEventListener() { //getting the child snapshot to get the number of childs
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

                //code in the above section is now depricated and isn't being used anymore//

                mRecordlabel.setText("recording now......");
            }
        });

        Button btn9=(Button)findViewById(R.id.stopbutton); //linking the stop button
        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fileName==".mp3"||flag==false){//check if the file name was empty or nothing was recorded
                    Toast.makeText(uploadactivity.this, "Nothing recorded", Toast.LENGTH_SHORT).show();
                }else{
                    flag=false;
                    stopRecording();

                    //This is the code for the upload//

                    mRecordlabel.setText("Uploading Audio");
                    //mProgress.setMessage("Uploading Audio");
                    //mProgress.show();
                    final String userUid = FirebaseAuth.getInstance().getCurrentUser().getEmail(); //get the email of teh current user
                    final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();//get the user is of the current user

                    StorageReference filepath = mStorageRef.child("Audio").child(userUid).child(fileName);//create a refrnce for the file path on the fitrebase database

                    Uri file = Uri.fromFile(new File(fileName));//get the uri of the file *now that i think about it maybe setting file path before isn't needed and will be removed later *

                    filepath.putFile(file) //puts the file on the fire base server
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // Get a URL to the uploaded content
                                   // Uri downloadUrl = taskSnapshot.getDownloadUrl().getResult();
                                 //   Task<Uri> downloadUrl= taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                    Task downloadUrl= taskSnapshot.getStorage().getDownloadUrl(); //get the url of the file after the upload

                                    while(!downloadUrl.isSuccessful()){ //get stuck if teh url was never downloaded *Yes we deliberately are hanging the app genius xD*

                                    }
                                    String string2=String.valueOf((Object)(Uri)downloadUrl.getResult());//cast the download url into a Uri and then cast it into an object and then parse it into a string

                                    Log.d("url",string2);
                                    //code to keep a track of the lecture numbers.



                        //    myRef.child("users").child(uid).child("Lecture"+lecno).setValue(downloadUrl.toString());
                                    myRef.child("users").child(Calendar.getInstance().getTime().toString()).child("Lecture").setValue(string2);//sending the url to the realtime database
                                    myRef.child("users").child(Calendar.getInstance().getTime().toString()).child("LectureName").setValue(filename2);//sending the filename to the real time database

                           //saves the url of the image on the real time data base
                            //mProgress.dismiss();
                            mRecordlabel.setText("upload complete");

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle unsuccessful uploads //Yeah we will add some code for the exception handling here later
                                    // ...
                                }
                            });

                    //this is the code for upload//


                }

            }
        });
    }

    private void startRecording() { //definition of the start recording function
        recorder = new MediaRecorder(); //assign value to the recorder
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);//set an audio source
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);//define format of teh audio
        recorder.setOutputFile(fileName);//set the output file name
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);//encode the audio

        try {
            recorder.prepare();//prepare the recorder
        } catch (IOException e) {
            Log.e("recorder", "prepare() failed");
        }

        recorder.start();//start the recorder
    }

    private void stopRecording() {//definition of the stop recording
        recorder.stop();//stop the recorder
        recorder.release();//release the recorder
        recorder = null;//set recorder to null after everything's done
    }
}
