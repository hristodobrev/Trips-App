package com.example.trips;

import androidx.fragment.app.FragmentActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Toast;

import com.example.trips.databinding.FragmentMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapsFragment extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap map;
    private FragmentMapsBinding binding;

    public static String location;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        if(bundle!= null){
            location = bundle.getString("Location");
        }

        binding = FragmentMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        Address address = null;
        Geocoder coder = new Geocoder(getApplicationContext());
        try {
            List<Address> addressList = coder.getFromLocationName(location, 5);
            if (addressList != null) {
                address = addressList.get(0);
            }
        } catch (Exception ex) {

        }

        if(address == null){
            Toast.makeText(this, "No such address", Toast.LENGTH_SHORT).show();
            return;
        }

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(address.getLatitude(), address.getLongitude());
        map.addMarker(new MarkerOptions().position(sydney).title("Marker in "+address.getLocality()));

        map.animateCamera(
                CameraUpdateFactory.newCameraPosition(CameraPosition.builder().zoom(15).target(sydney).build())
        );
    }
}