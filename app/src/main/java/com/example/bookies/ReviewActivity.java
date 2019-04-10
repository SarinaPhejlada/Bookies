package com.example.bookies;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;


public class ReviewActivity extends AppCompatActivity {

    //widgets
    TextView reviewTextView
            ,sellerTextView// using it for seller temporarily
            ,ISBNNumberTextView
            ,authorTextView
            ,titleTextView;
    ImageView image;

    //other
    Book book;
    boolean exist;

    //database
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        //retrieving book from isbnActivity
        book = (Book) getIntent().getSerializableExtra("book");

        //initializing variables
        reviewTextView = findViewById(R.id.review1);
        sellerTextView = findViewById(R.id.seller_textview);
        ISBNNumberTextView = findViewById(R.id.isbnReviewDisplay);
        authorTextView = findViewById(R.id.author);
        titleTextView = findViewById(R.id.title);
        image = findViewById(R.id.bookImage);

        //initialize db
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        //retrieving and displaying book information
        retrieveAndDisplayBookInformation(book);

        //adding book to history table
        addHistory(mAuth.getCurrentUser().getEmail(), book);

    }

    /** retrieves book info from IsbnActivity and displays the info using this activities' views*/
    protected void retrieveAndDisplayBookInformation(Book book){

        //Displaying review
        reviewTextView.setText(book.getReview());
        ISBNNumberTextView.setText("ISBN #: " + book.getISBNNumber());
        authorTextView.setText(book.getAuthor());
        titleTextView.setText(book.getTitle());
        sellerTextView.setText("Seller: "+book.getSeller());//Temporary

        //retrieving and displaying book cover image
        Picasso.get()
                .load(book.getImageLink())
                .fit()
                .centerInside()
                .into(image);
    }

    /** adds book to history table for user*/
    private void addHistory(final String email, final Book book){
        Map<String, Object> history = new HashMap<>();
        history.put("1", book.getISBNNumber());
        if(!existingAccount(email)) {
            db.collection("history").document(email)
                    .set(history)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(ReviewActivity.this, "Book added to history", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ReviewActivity.this, "Error adding book to history", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else{
            db.collection("history").document(email)
                    .set(email)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            addHistory(email, book);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ReviewActivity.this, "Error creating account history", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    /** checks if user exists in history table*/
    private boolean existingAccount(String email){
        db.collection("history").document(email)
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
                    }
                });
        return exist;
    }
}
