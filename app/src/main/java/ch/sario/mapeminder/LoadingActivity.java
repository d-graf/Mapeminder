package ch.sario.mapeminder;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LoadingActivity extends AppCompatActivity {
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        if (!isLocationEnabled()){
            mDialog = ProgressDialog.show(this, "GPS nicht aktiviert", "Bitte aktivieren und neustarten...");
        }else{
            procceedToNextActivity();
        }
    }

    public void procceedToNextActivity()
    {
        final Intent intent = new Intent(getApplicationContext(), MapsActivity.class);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                startActivity(intent);
                finish();
            }
        });

        thread.start();
    }

    protected boolean isLocationEnabled(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            return false;
        } else {
            return true;
        }
    }
}
