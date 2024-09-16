package com.example.trips;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int CREATE_TRIP = 1;
    public static final int UPDATE_TRIP = 2;

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
            Intent intent = new Intent(MainActivity.this, AddTripActivity.class);
            startActivityForResult(intent, CREATE_TRIP);
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView listViewId = view.findViewById(R.id.listViewId);
                TextView listViewTitle = view.findViewById(R.id.listViewTitle);
                TextView listViewDescription = view.findViewById(R.id.listViewDescription);
                TextView listViewPicture = view.findViewById(R.id.listViewPicture);
                TextView listViewLocation = view.findViewById(R.id.listViewLocation);

                Bundle bundle = new Bundle();
                bundle.putString("Id", listViewId.getText().toString());
                bundle.putString("Title", listViewTitle.getText().toString());
                bundle.putString("Description", listViewDescription.getText().toString());
                bundle.putString("Picture", listViewPicture.getText().toString());
                bundle.putString("Location", listViewLocation.getText().toString());

                Intent intent = new Intent(MainActivity.this, AddTripActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, UPDATE_TRIP, bundle);
            }
        });

        fillListView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && (requestCode == UPDATE_TRIP || requestCode == CREATE_TRIP)) {
            fillListView();
        }
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