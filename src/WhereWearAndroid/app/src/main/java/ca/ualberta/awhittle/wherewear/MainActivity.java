package ca.ualberta.awhittle.wherewear;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

// Location code based off of https://developer.android.com/guide/topics/location/strategies.html

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_CODE_FINE = 1;
    private String tag = "MainActivity";
    private TextView locTextView;
    private EditText destTextView;
    private Button goButton;
    private String destination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locTextView = (TextView) findViewById(R.id.locText);
        destTextView = (EditText) findViewById(R.id.destText);

        goButton = (Button)findViewById(R.id.buttonGo);
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                destination = destTextView.getText().toString();
                //TODO: Use location
            }
        });

        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

// Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                makeUseOfNewLocation(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

// Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {

            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_FINE);

            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }

    private void makeUseOfNewLocation(Location location) {
        String s = "Latitude: " + location.getLatitude() + "\n" +
                "Longitude: " + location.getLongitude();
        locTextView.setText(s);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                             int[] grantResults){
        if(requestCode == REQUEST_CODE_FINE){
            for(int i = 0; i < permissions.length; i++){
                if(permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION)
                        && grantResults[i] == PackageManager.PERMISSION_GRANTED){
                    return;
                }
            }

            Log.e(tag, "Error: Permission for fine location denied");
        }
    }
}
