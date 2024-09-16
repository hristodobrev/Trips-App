package com.example.trips;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddTripActivity extends AppCompatActivity {
    public static final int REQUEST_IMAGE_CAPTURE = 3;

    EditText editTitle, editPicture, editDescription, editLocation;
    Button btnPost, btnCancel, btnDelete, btnTakePicture, btnShowMap;
    ImageView imageView;

    String currentPicturePath;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_trip);

        editTitle = findViewById(R.id.editTitle);
        editDescription = findViewById(R.id.editDescription);
        editPicture = findViewById(R.id.editPicture);
        editLocation = findViewById(R.id.editLocation);
        imageView = findViewById(R.id.imageView);
        btnPost = findViewById(R.id.btnPost);
        btnShowMap = findViewById(R.id.btnShowMap);
        btnCancel = findViewById(R.id.btnCancel);
        btnDelete = findViewById(R.id.btnDelete);
        btnTakePicture = findViewById(R.id.btnTakePicture);

        final Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("Id")) {
            this.id = Integer.parseInt(bundle.getString("Id"));
            editTitle.setText(bundle.getString("Title"));
            editDescription.setText(bundle.getString("Description"));
            editPicture.setText(bundle.getString("Picture"));
            if (!bundle.getString("Picture").isEmpty()) {
                currentPicturePath = bundle.getString("Picture");
                PictureHelper.showPicture(currentPicturePath, imageView);
            }
            editLocation.setText(bundle.getString("Location"));
            btnPost.setText("Edit");
            btnDelete.setVisibility(View.VISIBLE);
        }

        btnTakePicture.setOnClickListener(view -> {
            takePicture();
        });

        btnPost.setOnClickListener(view -> {
            if (id != 0) {
                try(DbHelper db = new DbHelper(getApplicationContext())){
                    db.update(new Trip(
                            id,
                            editTitle.getText().toString(),
                            editDescription.getText().toString(),
                            editPicture.getText().toString(),
                            editLocation.getText().toString(),
                            null
                    ));
                    Toast.makeText(getApplicationContext(), "Trip successfully edited!",
                            Toast.LENGTH_LONG).show();
                } catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Error occurred while editing trip! Message: " + e.getLocalizedMessage(),
                            Toast.LENGTH_LONG).show();
                }
            } else {
                try(DbHelper db = new DbHelper(getApplicationContext())){
                    db.insert(new Trip(
                            0,
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
            }

            setResult(RESULT_OK);
            finish();
        });

        btnCancel.setOnClickListener(view -> {
            setResult(RESULT_OK);
            finish();
        });

        btnDelete.setOnClickListener(view -> {
            try(DbHelper db = new DbHelper(getApplicationContext())){
                db.delete(id);
                Toast.makeText(getApplicationContext(), "Trip successfully deleted!",
                        Toast.LENGTH_LONG).show();
            } catch (Exception e){
                Toast.makeText(getApplicationContext(), "Error occurred while deleting trip! Message: " + e.getLocalizedMessage(),
                        Toast.LENGTH_LONG).show();
            }

            setResult(RESULT_OK);
            finish();
        });

        btnShowMap.setOnClickListener(view -> {
            Bundle mapBundle = new Bundle();
            mapBundle.putString("Location", editLocation.getText().toString());
            Intent intent = new Intent(AddTripActivity.this, MapsFragment.class);
            intent.putExtras(mapBundle);
            startActivity(intent, mapBundle);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            editPicture.setText(currentPicturePath);
            PictureHelper.showPicture(currentPicturePath, imageView);
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        currentPicturePath = image.getAbsolutePath();
        return image;
    }

    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(getApplicationContext(), "Image capture failed! Message: " + ex.getLocalizedMessage(),
                        Toast.LENGTH_LONG).show();
            }

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }
}