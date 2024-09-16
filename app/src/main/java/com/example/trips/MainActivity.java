package com.example.trips;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    protected ListView listView;
    protected Button btnNewTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        btnNewTrip = findViewById(R.id.btnNewTrip);
        listView = findViewById(R.id.listView);
        btnNewTrip.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, AddTripActivity.class));
        });

        fillListView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        fillListView();
    }

    private void fillListView(){
        try(DbHelper db = new DbHelper(getApplicationContext())){
            List<Trip> list = db.select();
            CustomArrayAdapter adapter = new CustomArrayAdapter(
                    getApplicationContext(),
                    R.layout.activity_trip_list_view,
                    list
            );
            listView.clearChoices();
            listView.setAdapter(adapter);
        } catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }
}