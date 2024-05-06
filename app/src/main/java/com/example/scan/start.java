package com.example.scan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class start extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_start);

            Button bt1 = findViewById(R.id.bt1);

            bt1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Start the UserActivity when the USER button is clicked
                    Intent intent = new Intent(start.this, usertype.class);
                    startActivity(intent);
                }
            });




    }
}