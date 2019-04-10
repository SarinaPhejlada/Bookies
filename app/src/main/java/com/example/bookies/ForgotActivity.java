package com.example.bookies;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotActivity extends AppCompatActivity {
    //widgets
    protected static EditText email;
    private Button submit;
    protected static String error;

    //database reference
    private FirebaseAuth mAuth;

    //variables for testing
    protected static boolean valid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        //initialize widgets
        submit = (Button) findViewById(R.id.submitBtn);
        email = (EditText) findViewById(R.id.email);

        //connecting to database
        mAuth = FirebaseAuth.getInstance();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    forgot();
                }
                catch (Exception e){
                    Toast.makeText(ForgotActivity.this
                            ,"Failed to send email"
                            ,Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
    }
    private void forgot(){
        String user = email.getText().toString();
        if (user.isEmpty()) {
            email.setError("Email is required");
            email.requestFocus();
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(user).matches()) {
            email.setError("Please enter a valid email");
            email.requestFocus();
        }
        else {
            mAuth.sendPasswordResetEmail(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Check your email to reset your password", Toast.LENGTH_LONG).show();
                        valid = true;
                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(i);
                    } else {
                        email.setError("Account does not exist for email");
                        email.requestFocus();
                        //Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
