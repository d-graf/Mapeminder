package ch.sario.mapeminder;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
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
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener listener;
    private Marker myMarker;
    private Note note = new Note();
    ArrayList<Circle> circlePoint = new ArrayList<>();

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

                circlePoint.clear();

                addNoteMarker();

                for (int i = 0; i < circlePoint.size(); i++) {
                    Circle circle = circlePoint.get(i);

                    float[] distance = new float[2];

                    double pLong = location.getLongitude();
                    double pLat = location.getLatitude();

                    Location.distanceBetween(pLat, pLong, circle.getCenter().latitude, circle.getCenter().longitude, distance);

                    if(distance[0] > circle.getRadius()){
                        return;
                    } else {
                        callNotification(i);
                    }
                }
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

            int countNote = i + 1;

            mMap.addMarker(new MarkerOptions().position(new LatLng(CoordY, CoordX)).icon(getMarkerIcon("#088A29")).title("Notiz: " + countNote));

            circlePoint.add(mMap.addCircle(new CircleOptions()
                    .center(new LatLng(CoordY, CoordX))
                    .radius(500)
                    .strokeColor(Color.TRANSPARENT)
            )
            );
        }
        note.closeNote();
    }

    private BitmapDescriptor getMarkerIcon(String color) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }

    private void callNotification(int countNote){

        int countNotes = countNote + 1;

        Intent intent = new Intent();
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Notification noti = new Notification.Builder(this)
                .setTicker("Notiz in Reichweite")
                .setContentTitle("Notiz: " + countNotes + " in Reichweite")
                .setContentText("Notiz: " + countNotes + " in Reichweite")
                .setSmallIcon(R.drawable.notification_icon)
                .setContentIntent(pIntent).getNotification();
        noti.flags =Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, noti);
    }
}
