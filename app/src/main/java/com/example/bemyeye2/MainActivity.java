package com.example.bemyeye2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance(); //assigning a value to the firebase auth instance
        Button btn1=(Button) findViewById(R.id.Signin3); //linking the sign in button
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1= new Intent(MainActivity.this,signinclass.class); //declaring the intent for the sign in activity
                startActivity(intent1); //starting the next activity
            }
        });

        Button btn2=(Button) findViewById(R.id.Signup3); //linking the sign up button
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2= new Intent(MainActivity.this,signupclass.class); //declaring the instent to the sign up activity
                startActivity(intent2); //starting the next activity
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser(); //get the value of the current user on the start of the activity
        updateUI(currentUser);

    }

    private void updateUI(FirebaseUser currentUser) {
        if(currentUser!=null){
            Toast.makeText(this, "Signed in ,redirecting to dashboard", Toast.LENGTH_SHORT).show(); //create a toast if the user is signed in
            Intent intent7= new Intent(MainActivity.this,dashboard_class.class); //pass an intent to the dash board activity
            startActivity(intent7);//move back to the dash board directly is the user hasn't signed out

        }
    }
}
