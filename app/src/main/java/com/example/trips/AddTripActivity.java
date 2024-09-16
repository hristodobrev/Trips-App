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
    Button btnPost, btnCancel, btnDelete, btnTakePicture;
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
        btnCancel = findViewById(R.id.btnCancel);
        btnDelete = findViewById(R.id.btnDelete);
        btnTakePicture = findViewById(R.id.btnTakePicture);


        final Bundle b = getIntent().getExtras();
        if (b != null && b.containsKey("Id")) {
            this.id = Integer.parseInt(b.getString("Id"));
            editTitle.setText(b.getString("Title"));
            editDescription.setText(b.getString("Description"));
            editPicture.setText(b.getString("Picture"));
            if (!b.getString("Picture").isEmpty()) {
                currentPicturePath = b.getString("Picture");
                showPicture();
            }
            editLocation.setText(b.getString("Location"));
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            editPicture.setText(currentPicturePath);
            showPicture();
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

    private void showPicture() {
        Bitmap bitmap = BitmapFactory.decodeFile(currentPicturePath);

        ExifInterface ei = null;
        try {
            ei = new ExifInterface(currentPicturePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        Bitmap rotatedBitmap = null;
        switch(orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                rotatedBitmap = rotateImage(bitmap, 90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                rotatedBitmap = rotateImage(bitmap, 180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                rotatedBitmap = rotateImage(bitmap, 270);
                break;
            case ExifInterface.ORIENTATION_NORMAL:
            default:
                rotatedBitmap = bitmap;
        }

        imageView.setImageBitmap(rotatedBitmap);
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
}