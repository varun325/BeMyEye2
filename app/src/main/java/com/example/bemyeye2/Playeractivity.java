package com.example.bemyeye2;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;


public class Playeractivity extends AppCompatActivity {
    private String string1;

     MediaPlayer mediaPlayer; //create a reference to a media player
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playerpage);
        mediaPlayer=new MediaPlayer(); //assign the value to the media player
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC); //set stream type for the media player
        string1=getIntent().getStringExtra("FILE_URL"); //get the value of the url from the intent that was passed before
        //Uri uri=Uri.parse(string1);
        try {
            mediaPlayer.setDataSource(string1); //set the resource or the file path that is the url of our file on firebase
            mediaPlayer.prepare();//prepare the media player
        } catch (IOException e) {
            e.printStackTrace();
        }

        // mediaPlayer.start();
        Button btn11=(Button) findViewById(R.id.Play1); //linking the play button
        btn11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //declare an on click listener for the play button
                Log.d("FILE_URL",string1);



                mediaPlayer.start(); //start the media player

            }
        });

    }
}
