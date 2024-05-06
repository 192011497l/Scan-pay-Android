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

public class insertemp extends AppCompatActivity {

    private EditText etEmployeeName, etEmail, etMobileNumber, etAge, etRole, etQualification, etExperience, etAddress;
    private Button saveButton;

    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertemp);

        // Initialize views
        etEmployeeName = findViewById(R.id.etEmployeeName);
        etEmail = findViewById(R.id.etEmail);
        etMobileNumber = findViewById(R.id.etMobileNumber);
        etAge = findViewById(R.id.etAge);
        etRole = findViewById(R.id.etRole);
        etQualification = findViewById(R.id.etQualification);
        etExperience = findViewById(R.id.etExperience);
        etAddress = findViewById(R.id.etAddress);
        saveButton = findViewById(R.id.saveButton);



        // Set click listener for the save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("insertemp");

                String name = etEmployeeName.getText().toString();
                String email = etEmail.getText().toString();
                String mobileNumber = etMobileNumber.getText().toString();
                String age = etAge.getText().toString();
                String role = etRole.getText().toString();
                String qualification = etQualification.getText().toString();
                String experience = etExperience.getText().toString();
                String address = etAddress.getText().toString();

                employclass helperClass = new employclass(name, email, mobileNumber, age, role,qualification,experience, address ,"Activate");
                reference.child(name).setValue(helperClass);

                Toast.makeText(insertemp.this, "New Employee Added successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(insertemp.this, home.class);
                startActivity(intent);
            }
        });
    }


}
