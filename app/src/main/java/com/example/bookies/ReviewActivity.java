package com.example.bookies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;


public class ReviewActivity extends AppCompatActivity {

    //widgets
    TextView reviewTextView
            ,sellerTextView// using it for seller temporarily
            ,ISBNNumberTextView
            ,authorTextView
            ,titleTextView;
    ImageButton home;

    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        //initializing variables
        home = findViewById(R.id.homeButton);
        reviewTextView = findViewById(R.id.review1);
        sellerTextView = findViewById(R.id.seller_textview);
        ISBNNumberTextView = findViewById(R.id.isbnReviewDisplay);
        authorTextView = findViewById(R.id.author);
        titleTextView = findViewById(R.id.title);
        image = findViewById(R.id.bookImage);

        //Displaying review
        reviewTextView.setText(getIntent().getStringExtra("review"));
        ISBNNumberTextView.setText(getIntent().getStringExtra("isbnNumber"));
        authorTextView.setText(getIntent().getStringExtra("author"));
        titleTextView.setText(getIntent().getStringExtra("title"));
        sellerTextView.setText("Seller: "+getIntent().getStringExtra("seller"));//Temporary

        //for image
        Picasso.get()
                .load(getIntent().getStringExtra("imageURL"))
                .fit()
                .centerCrop()
                .into(image);

        //navigate home
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(i);
            }
        });
    }
}
