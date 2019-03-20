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

    Book book;

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

        //retrieving and displaying book information
        retrieveAndDisplayBookInformation();

        //navigate home
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(i);
            }
        });
    }

    /** retrieves book info from IsbnActivity and displays the info using this activities' views*/
    private void retrieveAndDisplayBookInformation(){

        //retrieving book from isbnActivity
        book = (Book) getIntent().getSerializableExtra("book");

        //Displaying review
        reviewTextView.setText(book.getReview());
        ISBNNumberTextView.setText(book.getISBNNumber());
        authorTextView.setText(book.getAuthor());
        titleTextView.setText(book.getTitle());
        sellerTextView.setText("Seller: "+book.getSeller());//Temporary

        //retrieving and displaying book cover image
        Picasso.get()
                .load(book.getImageLink())
                .fit()
                .centerCrop()
                .into(image);
    }
}
