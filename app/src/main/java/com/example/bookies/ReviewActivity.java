package com.example.bookies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class ReviewActivity extends AppCompatActivity {

    //widgets
    TextView review1TextView
            ,review2TextView// using it for seller temporarily
            ,ISBNNumberTextView
            ,authorTextView
            ,titleTextView;
    ImageButton home;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        //initializing variables
        home = findViewById(R.id.homeButton);
        review1TextView = findViewById(R.id.review1);
        review2TextView = findViewById(R.id.review2);
        ISBNNumberTextView = findViewById(R.id.isbnReviewDisplay);
        authorTextView = findViewById(R.id.author);
        titleTextView = findViewById(R.id.title);

        //Displaying review
        review1TextView.setText(getIntent().getStringExtra("review"));
        ISBNNumberTextView.setText(getIntent().getStringExtra("isbnNumber"));
        authorTextView.setText(getIntent().getStringExtra("author"));
        titleTextView.setText(getIntent().getStringExtra("title"));
        review2TextView.setText(getIntent().getStringExtra("seller"));//Temporary


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(i);
            }
        });
    }
}
