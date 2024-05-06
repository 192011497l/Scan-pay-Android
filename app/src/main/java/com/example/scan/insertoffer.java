package com.example.scan;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class insertoffer extends AppCompatActivity {

    private EditText etOffer, etExpiresDate, etOfferDetails;
    private Button saveButton;

    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertoffer);

        // Initialize views
        etOffer = findViewById(R.id.etEmployeeName);
        etExpiresDate = findViewById(R.id.expire);
        etOfferDetails = findViewById(R.id.details);
        saveButton = findViewById(R.id.saveButton);



        // Set click listener for the save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("offers");

                String offer = etOffer.getText().toString();
                String expire = etExpiresDate.getText().toString();
                String details = etOfferDetails.getText().toString();

                offerclass helperClass = new offerclass(offer, expire, details);
                reference.child(offer).setValue(helperClass);

                Toast.makeText(insertoffer.this, "New Offer Added successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(insertoffer.this, home.class);
                startActivity(intent);
            }
        });
    }


}
