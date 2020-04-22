package com.example.googlemaptest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ParkLocation extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Button imgBtn;

    String str_lat, str_lng;
    Float lat, lng;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_park_location);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        imgBtn = (Button)findViewById(R.id.imageBtn);

        Intent intent = getIntent();
        str_lat = intent.getStringExtra("lat");
        lat = Float.parseFloat(str_lat);
        str_lng = intent.getStringExtra("url");
        lng = Float.parseFloat(str_lng);

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap=googleMap;

        LatLng SEOUL = new LatLng(lat, lng);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(SEOUL);
        markerOptions.title("서울");
        markerOptions.snippet("한국의 수도");
        mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
    }

    public void front_image(View v){

        Intent intent = new Intent(ParkLocation.this, ImagePop.class);
        //startActivityForResult(intent,1);
        startActivity(intent);
    }

}
