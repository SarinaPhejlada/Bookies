package com.example.bookies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class BarcodeScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    ZXingScannerView scannerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);//set instead of R.layout.activity_scanner

    }

    @Override
    public void handleResult(Result result) {

        String text;
        if(result == null) {
            text = "No barcode scanned";
        }
        else{
            if(!result.getBarcodeFormat().toString().equalsIgnoreCase("EAN_13")){
                text = "Barcode is not an ISBN";
            }
            else{
                text = result.getText();
            }

        }
        IsbnActivity.isbnEditText.setText(text);
        onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();

        scannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();

        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }
}
