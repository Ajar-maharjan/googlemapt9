package com.pepo.googlemapt9;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private AutoCompleteTextView etCity;
    private Button btnSearch;
    private List<LatitudeLongitude> latitudeLongitudesList;
    Marker markerName;
    CameraUpdate center, zoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        com.google.android.gms.maps.SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        etCity = findViewById(R.id.etCity);
        btnSearch = findViewById(R.id.btnSearch);

        fillArrayListAndSetAdapter();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etCity.getText().toString())) {
                    etCity.setError("PlEaSe EnTeR PlAcE NaMe");
                    return;
                }
                int position = SearchArrayList(etCity.getText().toString());
                if (position > -1)
                    loadMap(position);
                else
                    Toast.makeText(MainActivity.this, "Location not found nibba", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fillArrayListAndSetAdapter() {
        latitudeLongitudesList = new ArrayList<>();
        latitudeLongitudesList.add(new LatitudeLongitude(27.7062581, 85.3300012, "My college Block E"));
        latitudeLongitudesList.add(new LatitudeLongitude(27.706195, 85.3300396, "My college"));
        latitudeLongitudesList.add(new LatitudeLongitude(27.7138696, 85.3179393, "Narayanhiti Palace Museum North Gate Road, Kathmandu"));
        latitudeLongitudesList.add(new LatitudeLongitude(27.7076992, 85.3120061, "Ason"));
        latitudeLongitudesList.add(new LatitudeLongitude(27.6710221, 85.4298197, "Bhaktapur"));

        String[] data = new String[latitudeLongitudesList.size()];

        for (int i = 0; i < data.length; i++) {
            data[i] = latitudeLongitudesList.get(i).getMarker();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                MainActivity.this, android.R.layout.simple_list_item_1, data
        );
        etCity.setAdapter(adapter);
        etCity.setThreshold(1);
    }

    public int SearchArrayList(String name) {
        for (int i = 0; i < latitudeLongitudesList.size(); i++) {
            if (latitudeLongitudesList.get(i).getMarker().contains(name)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        center = CameraUpdateFactory.newLatLng(new LatLng(27.7172455, 85.3239602));
        zoom = CameraUpdateFactory.zoomTo(17);
        mMap.moveCamera(center);
        mMap.animateCamera(zoom);
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }

    public void loadMap(int position) {
        if (markerName != null) {
            markerName.remove();
        }
        double latitude = latitudeLongitudesList.get(position).getLat();
        double longitude = latitudeLongitudesList.get(position).getLon();
        String marker = latitudeLongitudesList.get(position).getMarker();
        center = CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude));
        zoom = CameraUpdateFactory.zoomTo(17);
        markerName = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(marker));
        mMap.moveCamera(center);
        mMap.animateCamera(zoom);
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }
}
