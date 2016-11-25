package com.example.lucas.openweathermap.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.lucas.openweathermap.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng mMarkerCoordinates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //fab that calls CityListActivity, to search for the nearby cities
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMarkerCoordinates != null) {
                    Intent intent = new Intent(view.getContext(), CityListActivity.class)
                            .putExtra(getString(R.string.intent_extra_latLng), mMarkerCoordinates);

                    startActivity(intent);
                }
                else
                    showNoMarkerDialog();
            }
        });

        //Set default preferences in case of first app usage
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        if (mMap == null) {
            MapFragment mapFragment = (MapFragment) getFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //Open settings activity
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng coordinates) {
                mMarkerCoordinates = coordinates;
                addMarker(coordinates);
            }
        });
    }

    private void showNoMarkerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.error_no_marker));
        builder.setNeutralButton(getString(R.string.string_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int w) {
            }
        });

        builder.create().show();
    }

    /**
     * Add or reposition a marker, and center the screen on its location
     */
    private void addMarker(LatLng coordinates) {
        mMap.clear();

        mMap.addMarker(new MarkerOptions().position(coordinates));

        CameraPosition cameraPosition = new CameraPosition.Builder().target(coordinates).zoom(5).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}
