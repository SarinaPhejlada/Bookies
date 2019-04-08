package com.example.bookies;

import android.content.Intent;

import android.support.annotation.NonNull;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class IsbnActivity extends AppCompatActivity {
    //declaration of widgets
    private ImageButton home; //home button
    private Button submit; //submit button
    private Button take; //used to snap photo
    protected static EditText isbnEditText; //EditText view for isbn number
    //made protected static to be updated by BarcodeScannerActivity

    //data base reference
    private FirebaseFirestore db;
    //book object
    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_isbn);

        // initialization of widgets
         home =  findViewById(R.id.homeBtn);
         submit =  findViewById(R.id.submitBtn);
         take =  findViewById(R.id.takeBtn);
         isbnEditText = findViewById(R.id.isbn);

         //connecting to database
         db = FirebaseFirestore.getInstance();//database instance

         //code for home button
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(i);
            }
        });

        //code for submit button
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                        //This method passes the database content to the ReviewActivity
                        getBookReview(String.valueOf(isbnEditText.getText()));
                    }
                catch(Exception e){
                    Toast.makeText(IsbnActivity.this
                            ,"ISBN field can't be null"
                            ,Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        //code for take button
        take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), BarcodeScannerActivity.class);
                startActivity(i);
            }
        });

    }

    /**validates format of ISBN number*/
    //use when making request to database
    protected boolean isValidISBNFormat(String isbnNumber){
        //TODO: Tweak conditions before use
        return isbnNumber.matches("^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})"
        +"[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)"
        +"(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$");
    }

    /**Retrieves book review from database */
    protected Book getBookReview(final String isbnNumber){

        final Book[] books = new Book[1];

        book = new Book();//instantiate book object

        db.collection("book")
                .document(isbnNumber)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {

                            try{

                            DocumentSnapshot document = task.getResult();

                            //passing data to Review Activity
                            Intent i = new Intent(getApplicationContext(), ReviewActivity.class);

                            //retrieving book data from database
                            book.setAuthor(document
                                    .getData()
                                    .get("author")
                                    .toString());

                            book.setReview(document
                                    .getData()
                                    .get("amazon review")
                                    .toString());

                            book.setTitle(document
                                    .getData()
                                    .get("title")
                                    .toString());

                            book.setSeller(document
                                    .getData()
                                    .get("seller")
                                    .toString());

                            book.setISBNNumber(isbnNumber);

                            book.setImageLink(document
                                    .getData()
                                    .get("image")
                                    .toString());
                            //passing book to reviewActivity
                            i.putExtra("book",book);

                            books[0] = book;
                            startActivity(i);//starting activity

                            }
                            //in case retrieval fails
                            catch(Exception e){
                                Toast.makeText(IsbnActivity
                                        .this,"ISBN number not found",Toast
                                        .LENGTH_LONG)
                                        .show();
                            }

                        }
                    }
                });
        return books[0];
    }

}
