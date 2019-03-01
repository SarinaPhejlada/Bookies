package com.example.bookies;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import androidx.annotation.RequiresApi;

public class IsbnActivity extends AppCompatActivity {
    //declaration of widgets
    private ImageButton home; //home button
    private Button submit; //submit button
    private Button take; //used to snap photo
    private Button upload; //used to upload already existing photo
    private EditText isbnEditText; //EditText view for isbn number

    //variables used for image capture/upload
    static final int REQUEST_IMAGE_CAPTURE = 1;// camera request code for onActivityResult method
    static final int REQUEST_IMAGE_UPLOAD = 2;// upload request code for onActivityResult method
    //private ImageView imageView; //to show the image captured (optional)
    private Bitmap bitmapImage; //saves image taken or uploaded
    private String retrievedText; //contains text after it's processed by detector
    //data base reference
    FirebaseFirestore db;

    //variables to pass to ReviewActivity
    String author
            ,title
            ,review
            ,isbnNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_isbn);

        // initialization of widgets
         home =  findViewById(R.id.homeBtn);
         submit =  findViewById(R.id.submitBtn);
         take =  findViewById(R.id.takeBtn);
         upload =  findViewById(R.id.uploadBtn);
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
                //this method pulls up the camera and takes a picture
                dispatchTakePictureIntent();
            }
        });

        //code for upload button
        upload.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                //this method allows user to pick a photo from their gallery
                requestPhotoFromGallery();
            }
        });
    }

    /**method to start intent for camera and take picture*/
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    /**method to start intent and upload picture from gallery*/
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void requestPhotoFromGallery(){
        //invokes image gallery
        Intent photoChooserIntent = new Intent(Intent.ACTION_PICK);

        //location of file
        File pictureDirectory = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        //get URI representation
        Uri data = Uri.parse(pictureDirectory.getPath());

        //getting all image types
        photoChooserIntent.setDataAndType(data, "image/*");

        //invoke activity
        startActivityForResult(photoChooserIntent, REQUEST_IMAGE_UPLOAD);

    }

    /**saves photo as bitmap in extras under "data"*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            bitmapImage = (Bitmap) extras.get("data");
            processPhoto(bitmapImage);

        }
        else if(requestCode == REQUEST_IMAGE_UPLOAD && resultCode == RESULT_OK){

            try {//reads photo image
                bitmapImage = BitmapFactory//converted from InputStream to Bitmap
                        .decodeStream(getContentResolver()//converted from Uri to InputStream
                                .openInputStream(data.getData()));//data in form of Uri

                //process photo using Firebase ML detector
                processPhoto(bitmapImage);

            }catch (FileNotFoundException e) {

                Toast.makeText(IsbnActivity.this,"No image found",Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    /**process photo taken from camera using ML model*/
    private void processPhoto(Bitmap bImage){

        //converts bitmap image
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bImage);
        //gets image recognizer
        FirebaseVisionTextRecognizer detector = FirebaseVision
                .getInstance()
                .getOnDeviceTextRecognizer();
        //processes image
        final Task<FirebaseVisionText> result =
                detector.processImage(image)
                        .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                            @Override
                            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                                // Task completed successfully
                                updateISBN(firebaseVisionText);
                            }
                        })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Task failed with an exception
                                        // ...
                                    }
                                });

    }

    /**updates ISBN string from the text recognizer*/
    private void updateISBN(FirebaseVisionText text){
        List<FirebaseVisionText.TextBlock> blocks = text.getTextBlocks();
        if(blocks.size() == 0){
            Toast.makeText(IsbnActivity.this,"No text found",Toast.LENGTH_LONG).show();
            return;
        }

        for(FirebaseVisionText.TextBlock block : text.getTextBlocks()){
            retrievedText = block.getText();
            isbnEditText.setText(filterTextAndAcquireISBN(retrievedText));//temporary to see feedback
            //TODO: filter data to only read the isbn number and not any other text.
        }
    }

    /**filters text from photo and acquires ISBN number*/
    private String filterTextAndAcquireISBN(String text){
        //TODO: make more accurate.
        //splits text into seperate strings
        String[] textArray = text.trim().split(" ");
        //search through string array
        for(int i = 0; i<textArray.length;i++){
            if(textArray[i].equals("ISBN")){
                return textArray[++i];
            }
        }
        Toast.makeText(IsbnActivity.this
                ,"ISBN Number Not Found "
                ,Toast.LENGTH_LONG)
                .show();
        return null;
    }

    /**validates format of ISBN number*/
    //use when making request to database
    private boolean isValidISBNFormat(String isbnNumber){

        return isbnNumber.matches("^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})"
        +"[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)"
        +"(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$");
    }

    /**Retrieves book review from database */
    private void getBookReview(final String isbnNumber){

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

                            //Data to be passed to ReviewActivity from database
                            i.putExtra("review", document
                                    .getData()
                                    .get("amazon review")
                                    .toString());

                            i.putExtra("author", document
                                    .getData()
                                    .get("author")
                                    .toString());

                            i.putExtra("title", document
                                    .getData()
                                    .get("title")
                                    .toString());

                            i.putExtra("seller", document
                                    .getData()
                                    .get("seller")
                                    .toString());

                            i.putExtra("isbnNumber"
                                    ,"ISBN #: "
                                            + isbnNumber);

                            startActivity(i);

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

    }

}
