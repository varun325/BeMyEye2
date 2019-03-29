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
        mAuth = FirebaseAuth.getInstance();
        Button btn2= (Button) findViewById(R.id.back1);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2= new Intent(signinclass.this,MainActivity.class);
                startActivity(intent2);
            }
        });

        Button btn5=(Button) findViewById(R.id.Signin2);
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText siusername=(EditText)findViewById(R.id.siusername);
                EditText sipassword=(EditText)findViewById(R.id.sipassword);
                String email=siusername.getText().toString();
                String password=sipassword.getText().toString();

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(signinclass.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("signin" ,"signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                    Intent intent6 =new Intent(signinclass.this,dashboard_class.class);
                                    startActivity(intent6);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("signin", "signInWithEmail:failure", task.getException());
                                    Toast.makeText(signinclass.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "Signed in ,redirecting to dashboard", Toast.LENGTH_SHORT).show();
            Intent intent7= new Intent(signinclass.this,dashboard_class.class);
            startActivity(intent7);
        }
    }
}
