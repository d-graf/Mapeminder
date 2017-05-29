package ch.sario.mapeminder;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.widget.TextView;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.logging.Handler;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener listener;
    private Marker myMarker;
    private Note note = new Note();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        clickOverview();
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (myMarker == null) {
                    myMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("Mein Standort"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myMarker.getPosition(), 15));
                } else {
                    myMarker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myMarker.getPosition(), 15));
                }

                addNoteMarker();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        , 10);
            }
            return;
        }
        locationManager.requestLocationUpdates("gps", 5000, 0, listener);

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {

                System.out.println(point.longitude);
                System.out.println(point.latitude);

                Intent intent = new Intent(getApplicationContext(), AddActivity.class);
                intent.putExtra("x", Double.toString(point.longitude));
                intent.putExtra("y", Double.toString(point.latitude));
                startActivity(intent);
            }
        });
    }

    private void clickOverview() {
        Intent intent = new Intent(getApplicationContext(), OverviewActivity.class);
        startActivity(intent);
    }

    private void addNoteMarker(){
        note.createOpenDB(this);

        ArrayList<String> output = note.getAllCoords();

        for(int i = 0; i < output.size(); i++) {
            String[] selectedParts = output.get(i).split(":");
            String x = selectedParts[0];
            String y = selectedParts[1];
            Double CoordX = Double.parseDouble(x);
            Double CoordY = Double.parseDouble(y);

            mMap.addMarker(new MarkerOptions().position(new LatLng(CoordY, CoordX)).icon(getMarkerIcon("#088A29")));
        }
        note.closeNote();
    }

    private BitmapDescriptor getMarkerIcon(String color) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }
}
