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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;
import java.util.Map;

public class CreateActivity extends AppCompatActivity {
    //widgets
    private EditText email;
    private EditText password;
    private Button createButton;

    boolean exist;

    //data base reference
    FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        //initializing widgets
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        createButton = (Button) findViewById(R.id.createBtn);

        //connecting to database
        db = FirebaseFirestore.getInstance();//database instance
        mAuth = FirebaseAuth.getInstance();

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    /*This method verifies the credentials the user entered
                    if(!String.valueOf(email.getText()).equals("") || !String.valueOf(password.getText()).equals(""))
                        createAccount(String.valueOf(email.getText()), String.valueOf(password.getText()));
                    else{
                        Toast.makeText(CreateActivity.this
                                ,"Username/password cannot be null"
                                ,Toast.LENGTH_LONG)
                                .show();
                    }*/
                    registerUser();
                }
                catch(Exception e){
                    Toast.makeText(CreateActivity.this
                            ,"Failed to create an account"
                            ,Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }

    private void registerUser(){
        String user = email.getText().toString();
        String pass = password.getText().toString();
        if (user.isEmpty()) {
            email.setError("Email is required");
            email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(user).matches()) {
            email.setError("Please enter a valid email");
            email.requestFocus();
            return;
        }
        if (pass.isEmpty()) {
            password.setError("Password is required");
            password.requestFocus();
            return;
        }
        mAuth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    finish();
                    startActivity(new Intent(CreateActivity.this, HomeActivity.class));
                } else {

                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(getApplicationContext(), "Error occurred: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }

                }
            }
        });

    }

    //create account in database
    /*private void createAccount(final String email, final String password){
        Map<String, Object> user = new HashMap<>();
        user.put("password", password);
        if(existingAccount(email)) {
            db.collection("account").document(email)
                    .set(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(CreateActivity.this
                                    , "Account created"
                                    , Toast.LENGTH_LONG)
                                    .show();
                            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(i);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CreateActivity.this
                                    , "Account could not be created"
                                    , Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
        }
        else{
            Toast.makeText(CreateActivity.this
                    , "Account already exists for email"
                    , Toast.LENGTH_LONG)
                    .show();
        }
    }
    //check if account already exists
    private boolean existingAccount(final String email){
        exist = false;
        db.collection("account").document(email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists())
                                exist = true;
                            else
                                exist = false;
                        }
                        else{
                            Toast.makeText(CreateActivity.this
                                    ,"Account could not be created"
                                    ,Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                });
        return exist;
    }*/
}
