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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.AuthResult;

public class LoginActivity extends AppCompatActivity {
    //widgets
    private Button loginButton; //login button
    private Button forgotPasswordButton; //forgot password button
    private Button createAccountButton; //create an account button
    protected static EditText username; //edittext view for username/email
    protected static EditText password; //edittext view for password

    //data base reference
    FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initializing widgets
        loginButton = (Button) findViewById(R.id.loginBtn);
        forgotPasswordButton = (Button) findViewById(R.id.forgotBtn);
        createAccountButton = (Button) findViewById(R.id.createBtn);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        //connecting to database
        db = FirebaseFirestore.getInstance();//database instance
        mAuth = FirebaseAuth.getInstance();

        //code for login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    login();
                }
                catch(Exception e){
                    Toast.makeText(LoginActivity.this
                            ,"Invalid username or password"
                            ,Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
        //code for retrieving forgotten button
        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ForgotActivity.class);
                startActivity(i);
            }
        });
        //code for creating an account button
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CreateActivity.class);
                startActivity(i);
            }
        });
    }

    private void login(){
        String user = username.getText().toString();
        String pass = password.getText().toString();
        if (user.isEmpty()) {
            username.setError("Email is required");
            username.requestFocus();
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(user).matches()) {
            username.setError("Please enter a valid email");
            username.requestFocus();
        }
        else if (pass.isEmpty()) {
            password.setError("Password is required");
            password.requestFocus();
        }
        else {
            mAuth.signInWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        finish();
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                    } else {
                        password.setError("Incorrect password or account does not exist");
                        password.requestFocus();
                    }
                }
            });
        }
    }

    //retrieves login from database
    /*private void getLogin(final String username, final String password){
        db.collection("account")
                .document(username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            try{
                                DocumentSnapshot document = task.getResult();
                                if(document.getData().get("password").toString().equals(password)) {
                                    //passing data to Review Activity
                                    Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                                    startActivity(i);
                                }
                                else{
                                    Toast.makeText(LoginActivity
                                            .this,"Incorrect password",Toast
                                            .LENGTH_LONG)
                                            .show();
                                }
                            }
                            //in case retrieval fails
                            catch(Exception e){
                                Toast.makeText(LoginActivity
                                        .this,"Username not found",Toast
                                        .LENGTH_LONG)
                                        .show();
                            }
                        }
                    }
                });
    }*/

}
