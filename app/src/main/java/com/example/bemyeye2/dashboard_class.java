package com.example.bemyeye2;

        import android.content.Intent;
        import android.os.Bundle;
        import android.support.annotation.Nullable;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.Toast;

        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;

public class dashboard_class extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_layout);
        mAuth = FirebaseAuth.getInstance();//create an instance of the firebase auth
        Button btn4= (Button) findViewById(R.id.signout);//link the sign out button
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(dashboard_class.this, "Signed out successfully", Toast.LENGTH_SHORT).show();
                Intent intent4= new Intent(dashboard_class.this,signinclass.class);//create an isntent for the sign in activity
                startActivity(intent4);//start the new activity
            }
        });



        Button btn10=(Button) findViewById(R.id.Search);//link the search button
        btn10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("btn10","button pressed search");
                Intent intent10=new Intent(dashboard_class.this,search.class);//create an intent for the search activity
                startActivity(intent10);//start the new activity
            }
        });


        Button btn5=(Button)findViewById(R.id.upload1);//link the upload button
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent5=new Intent(dashboard_class.this,uploadactivity.class);//create an intent for the upload activity
                startActivity(intent5);//start the next activity
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
            Toast.makeText(this, "Successfully signed in", Toast.LENGTH_SHORT).show(); //create a toast if the user is already signed in
        }
    }


}
