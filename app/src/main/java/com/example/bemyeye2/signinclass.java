package com.example.bemyeye2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class signinclass extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_layout);
        mAuth = FirebaseAuth.getInstance();//create an instance of the firebase auth
        Button btn2= (Button) findViewById(R.id.back1);//link the back button
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2= new Intent(signinclass.this,MainActivity.class);//create  an intent to the main activity
                startActivity(intent2);//start next activity
            }
        });

        Button btn5=(Button) findViewById(R.id.Signin2);//link teh sign in activity
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText siusername=(EditText)findViewById(R.id.siusername);
                EditText sipassword=(EditText)findViewById(R.id.sipassword);
                String email=siusername.getText().toString();
                String password=sipassword.getText().toString();

                mAuth.signInWithEmailAndPassword(email, password)//sign in with the given email and password on the firebase server
                        .addOnCompleteListener(signinclass.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("signin" ,"signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                    Intent intent6 =new Intent(signinclass.this,dashboard_class.class);//create and intent for the dash board activity
                                    startActivity(intent6);//start next activity
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("signin", "signInWithEmail:failure", task.getException());
                                    Toast.makeText(signinclass.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();//create a toast if the authentication fails
                                    updateUI(null);
                                }

                                // ...
                            }
                        });

            }
        });
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
            Toast.makeText(this, "Signed in ,redirecting to dashboard", Toast.LENGTH_SHORT).show();//create a toast if the user is already signed in
            Intent intent7= new Intent(signinclass.this,dashboard_class.class);//pass an intent to the dashboard if the user is already logged in
            startActivity(intent7);//start next activity
        }
    }
}
