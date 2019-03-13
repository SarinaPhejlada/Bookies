package com.example.bookies;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateActivity extends AppCompatActivity {
    //widgets
    private EditText email;
    private EditText password;
    private Button createButton;

    //data base reference
    FirebaseFirestore db;

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

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //This method verifies the credentials the user entered
                    if(!String.valueOf(email.getText()).equals("") || !String.valueOf(password.getText()).equals(""))
                        createAccount(String.valueOf(email.getText()), String.valueOf(password.getText()));
                    else{
                        Toast.makeText(CreateActivity.this
                                ,"Username/password cannot be null"
                                ,Toast.LENGTH_LONG)
                                .show();
                    }
                }
                catch(Exception e){
                    Toast.makeText(CreateActivity.this
                            ,"Failed to an create account"
                            ,Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
    }
    //create account in database
    private void createAccount(final String email, final String password){
        Map<String, Object> user = new HashMap<>();
        user.put("password", password);
        db.collection("account").document(email)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(CreateActivity.this
                                ,"Account created"
                                ,Toast.LENGTH_LONG)
                                .show();
                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(i);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CreateActivity.this
                                ,"Account could not be created"
                                ,Toast.LENGTH_LONG)
                                .show();
                    }
                });
    }
}
