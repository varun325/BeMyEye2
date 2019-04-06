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

     MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playerpage);
        mediaPlayer=new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        string1=getIntent().getStringExtra("FILE_URL");
        //Uri uri=Uri.parse(string1);
        try {
            mediaPlayer.setDataSource(string1);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // mediaPlayer.start();
        Button btn11=(Button) findViewById(R.id.Play1);
        btn11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("FILE_URL",string1);



                mediaPlayer.start();

            }
        });

    }
}
