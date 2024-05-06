package com.example.scan;


import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class QRScanner extends AppCompatActivity {

    Button btn_Scanner;
    FirebaseDatabase database;
    DatabaseReference cartRef;
    ActivityResultLauncher<ScanOptions> barLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscanner);
        btn_Scanner = findViewById(R.id.btn_scanner);
        database = FirebaseDatabase.getInstance(); // Initialize Firebase Realtime Database
        cartRef = database.getReference("cart"); // Reference to the "cart" node in the database

        btn_Scanner.setOnClickListener(v -> {
            scannerCode();
        });

        // Initialize the barcode launcher
        barLauncher = registerForActivityResult(new ScanContract(), result -> {
            if (result.getContents() != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(QRScanner.this);
                builder.setTitle("Result");
                builder.setMessage(result.getContents());
                builder.setPositiveButton("Add To Cart", (dialogInterface, i) -> {
                    // Store the scanned item into Firebase Realtime Database
                    String scannedItem = result.getContents();
                    addScannedItemToDatabase(scannedItem);
                    dialogInterface.dismiss();
                }).show();
            }
        });
    }

    private void scannerCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }

    private void addScannedItemToDatabase(String scannedItem) {
        // Generate a unique key for the scanned item
        String key = cartRef.push().getKey();
        // Add the scanned item to the "cart" node with the unique key
        cartRef.child(key).setValue(scannedItem)
                .addOnSuccessListener(aVoid -> {
                    Log.d("QRScanner", "Scanned item added to cart with key: " + key);
                    Toast.makeText(QRScanner.this, "Item added to cart", Toast.LENGTH_SHORT).show();
                    // Handle success if needed reload activity
                    recreate();
                })
                .addOnFailureListener(e -> {
                    Log.w("QRScanner", "Error adding scanned item to cart", e);
                    Toast.makeText(QRScanner.this, "Failed to add item to cart", Toast.LENGTH_SHORT).show();
                    // Hand
                })
                .addOnSuccessListener(aVoid -> {
                    Log.d("QRScanner", "Scanned item added to cart with key: " + key);
                    Toast.makeText(QRScanner.this, "Item added to cart successfully", Toast.LENGTH_SHORT).show();
                    // Handle success if needed
                })
                .addOnFailureListener(e -> {
                    Log.w("QRScanner", "Error adding scanned item to cart", e);
                    Toast.makeText(QRScanner.this, "Failed to add item to cart", Toast.LENGTH_SHORT).show();
                    // Handle failure if needed
                });
    }
}
