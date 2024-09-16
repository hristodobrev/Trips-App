package com.example.trips;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddTripActivity extends AppCompatActivity {
    EditText editTitle, editPicture, editDescription, editLocation;
    Button btnPost, btnCancel, btnTakePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_trip);

        editTitle = findViewById(R.id.editTitle);
        editDescription = findViewById(R.id.editDescription);
        editPicture = findViewById(R.id.editPicture);
        editLocation = findViewById(R.id.editLocation);
        btnPost = findViewById(R.id.btnPost);
        btnCancel = findViewById(R.id.btnCancel);
        btnTakePicture = findViewById(R.id.btnTakePicture);

        btnPost.setOnClickListener(view -> {
            try(DbHelper db = new DbHelper(getApplicationContext())){
                db.insert(new Trip(
                        "0",
                        editTitle.getText().toString(),
                        editDescription.getText().toString(),
                        editPicture.getText().toString(),
                        editLocation.getText().toString(),
                        null
                ));
                Toast.makeText(getApplicationContext(), "Trip successfully added!",
                        Toast.LENGTH_LONG).show();
            } catch (Exception e){
                Toast.makeText(getApplicationContext(), "Error occurred while adding trip! Message: " + e.getLocalizedMessage(),
                        Toast.LENGTH_LONG).show();
            }

            super.finish();
        });

        btnCancel.setOnClickListener(view -> {
            super.finish();
        });
    }
}