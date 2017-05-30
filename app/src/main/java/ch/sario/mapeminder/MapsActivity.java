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
import android.util.Log;
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
import java.util.TreeMap;

/**
 * Activity to load Map and all markers and cycles.
 *
 * @version 1.0
 */
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final static int REQUEST_CODE_ADD_MARKER = 1;

    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener listener;
    private Marker myMarker;
    private Marker locationMarker;
    private NoteContract noteContract = new NoteContract();
    ArrayList<Circle> circlePoint = new ArrayList<>();
    TreeMap<String, Note> markerNotes = new TreeMap<>();
    private Circle circles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }

    /**
     * integrate the menu menu.xml.
     * @param menu, menu xml to go to overview.
     * @return true.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }
    /**
     * on item click call clickOverview().
     * @param item, item of the menu in the right corner.
     * @return true.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        clickOverview();
        return true;
    }

    /**
     * Go to the OverviewActivity.
     */
    private void clickOverview() {
        Intent intent = new Intent(getApplicationContext(), OverviewActivity.class);
        startActivity(intent);
    }

    /**
     * On map ready locate the user's position and set a marker.
     * Check all marker if one is in radius, callNotification().
     * All 20000ms the listener refresh.
     * @param googleMap, Map.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        loadNoteMarkers();

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (myMarker == null) {
                    myMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("Mein Standort"));
                    circles = mMap.addCircle(new CircleOptions()
                                    .center(new LatLng(location.getLatitude(), location.getLongitude()))
                                    .radius(500)
                                    .strokeColor(Color.BLACK)
                                    .fillColor(Color.argb(50,137,250,114))
                            );
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myMarker.getPosition(), 14));
                } else {
                    myMarker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
                    circles.setCenter(new LatLng(location.getLatitude(), location.getLongitude()));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myMarker.getPosition(), 14));
                }

                int counter = 0;

                Log.e("App", "gis" + circlePoint.size());
                for (int i = 0; i < circlePoint.size(); i++) {
                    Circle circle = circlePoint.get(i);


                    Location circleLocation = new Location("Circle Location");

                    circleLocation.setLatitude(circle.getCenter().latitude);
                    circleLocation.setLongitude(circle.getCenter().longitude);


                    float distance = location.distanceTo(circleLocation);

                    Log.e("App", "dis" + Float.toString(distance) + " < " + circle.getRadius());
                    Log.e("App", (circle.getCenter().latitude + "    " + circle.getCenter().longitude));

                    if(distance < circle.getRadius()) {
                        counter++;
                        callNotification(counter);
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
        locationManager.requestLocationUpdates("gps", 15000, 0, listener);

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {

                Intent intent = new Intent(getApplicationContext(), AddActivity.class);
                intent.putExtra("x", point.longitude);
                intent.putExtra("y", point.latitude);
                startActivityForResult(intent, REQUEST_CODE_ADD_MARKER);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker locationMarker) {

                if (markerNotes.containsKey(locationMarker.getId())) {
                    Note note = markerNotes.get(locationMarker.getId());


                    Intent intent = new Intent(getApplicationContext(), DetailActivity.class);

                    intent.putExtra("id", note.getId());
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }


                return true;
            }
        });
    }

    /**
     * Check in the AddActivity if save button was clicked and return result.
     * Call function addNoteMarker() if save button was clicked.
     * @param requestCode, code to addactivity.
     * @param resultCode, code from addactivity
     * @param data, intent.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == REQUEST_CODE_ADD_MARKER) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                addNoteMarker(data.getDoubleExtra("x", 0.0), data.getDoubleExtra("y", 0.0));
            }
        }
    }

    /**
     * Select all notes from db and set marker on map.
     */
    private void loadNoteMarkers() {
        noteContract.createOpenDB(this);

        ArrayList<Note> notes = noteContract.getAllNotes();

        for(int i = 0; i < notes.size(); i++) {

            Note note = notes.get(i);

            Double CoordX = Double.parseDouble(note.getLongitude());
            Double CoordY = Double.parseDouble(note.getLatitude());


            locationMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(CoordY, CoordX)).icon(getMarkerIcon("#088A29")).title("Notiz: " + note.getId()));
            markerNotes.put(locationMarker.getId(), note);

            circlePoint.add(mMap.addCircle(new CircleOptions()
                            .center(new LatLng(CoordY, CoordX))
                            .radius(500)
                            .strokeColor(Color.TRANSPARENT)
                    )
            );
        }
        noteContract.closeNote();
    }

    /**
     * Add a marker on map.
     * @param CoordY, Y Coord from new marker
     * @param CoordX, X Coord from new marker
     */
    public void addNoteMarker(double CoordY, double CoordX){
        circlePoint.add(mMap.addCircle(new CircleOptions()
                        .center(new LatLng(CoordY, CoordX))
                        .radius(500)
                        .strokeColor(Color.TRANSPARENT)
                )
        );
    }

    /**
     * function to change the color of a marker with hex color.
     * @param color, hex color code, #088A29.
     */
    private BitmapDescriptor getMarkerIcon(String color) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }

    /**
     * send a notification with the note which is in reach.
     * @param counter, current note.
     */
    private void callNotification(int counter){
        Intent intent = new Intent();
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Notification noti = new Notification.Builder(this)
                .setTicker("Notizen sind in Reichweite")
                .setContentTitle("Notizen sind in Reichweite")
                .setContentText("Notiz/en in Reichweite")
                .setSmallIcon(R.drawable.notification_icon)
                .setContentIntent(pIntent).getNotification();
        noti.flags =Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, noti);
    }
}
