package com.example.bookies;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    //declare widgets
    ListView list;
    ImageButton home;

    History hist;
    ArrayList<String> books;
    ArrayAdapter adapter;

    //database reference
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //initialize widgets
        home = (ImageButton) findViewById(R.id.homeBtn);
        list = (ListView) findViewById(R.id.listView);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(i);
            }
        });

        //initialize db
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        hist = new History();
        books = new ArrayList<>();
        getHistory(mAuth.getCurrentUser().getEmail()); //entry is not being listed
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, books);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(HistoryActivity.this, "Clicked item " + books.get(position), Toast.LENGTH_SHORT).show();
                //should pull up review of isbn selected
            }
        });
    }
    //get history from database
    private void getHistory(final String email){
        db.collection("history").document(email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        try {
                            DocumentSnapshot document = task.getResult();
                            //retrieving book data from database
                            hist.setOne(document
                                    .getData()
                                    .get("1")
                                    .toString());
                            books.add(document.getData().get("1").toString());
                            //Toast.makeText(HistoryActivity.this, document.getData().get("1").toString(), Toast.LENGTH_SHORT).show();
                        }
                        catch(Exception e){
                            Toast.makeText(HistoryActivity
                                    .this,"Error generating history",Toast
                                    .LENGTH_LONG)
                                    .show();
                        }
                    }
                });
    }
}

