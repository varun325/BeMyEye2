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
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class signupclass extends AppCompatActivity {
    EditText username;
    String email;
    EditText pass;
    String password;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout);



        Button btn3= (Button) findViewById(R.id.back2);
        Button btn5=(Button)findViewById(R.id.Signup2);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2= new Intent(signupclass.this,MainActivity.class);
                startActivity(intent2);
            }
        });



       btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username=findViewById(R.id.username1);
                email=username.getText().toString();
                pass=findViewById(R.id.password1);
                password=pass.getText().toString();
               // email="sharmavarun1300@gmail.com";
                //password="varun1234@";

                if(email!=""&&email!=null&&password!=""&&password!=null){
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(signupclass.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("signup", "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        updateUI(user);
                                        Intent intent5=new Intent(signupclass.this,dashboard_class.class);
                                        startActivity(intent5);

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("signup", "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(signupclass.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        updateUI(null);
                                    }

                                    // ...
                                }
                            });
                }
                else{
                    Toast.makeText(signupclass.this, "Fields null or empty", Toast.LENGTH_SHORT).show();
                }



            }
        });
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
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
            Toast.makeText(this, "Signed in ,redirecting to dashboard", Toast.LENGTH_SHORT).show();
            Intent intent7= new Intent(signupclass.this,dashboard_class.class);
            startActivity(intent7);
        }
    }





}

