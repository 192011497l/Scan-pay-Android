package com.example.scan;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity2 extends AppCompatActivity {

    EditText signupName, signupEmail, signupUsername, signupPassword,signupAddress;
    TextView loginRedirectText;
    Button signupButton;
    FirebaseDatabase database;
    DatabaseReference reference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);

        signupName = findViewById(R.id.signup_shopname1);
        signupEmail = findViewById(R.id.signup_email);
        signupUsername = findViewById(R.id.signup_username);
        signupAddress = findViewById(R.id.signup_address1);
        signupPassword = findViewById(R.id.signup_password);
        signupButton = findViewById(R.id.signup_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("admin");

                String username = signupUsername.getText().toString();
                String email = signupEmail.getText().toString();
                String shopname = signupName.getText().toString();
                String address = signupAddress.getText().toString();
                String password = signupPassword.getText().toString();

                // Check if any of the fields is empty
                if (username.isEmpty() || email.isEmpty() || shopname.isEmpty() || address.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SignupActivity2.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    HelperClass2 helperClass = new HelperClass2(username, email, shopname, address, password);
                    reference.child(username).setValue(helperClass);

                    Toast.makeText(SignupActivity2.this, "You have signup successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignupActivity2.this, LoginActivity2.class);
                    startActivity(intent);
                }
            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity2.this, LoginActivity2.class);
                startActivity(intent);
            }
        });
    }
}