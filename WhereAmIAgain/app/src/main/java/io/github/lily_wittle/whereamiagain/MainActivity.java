package io.github.lily_wittle.whereamiagain;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.List;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private FusedLocationProviderClient fusedLocationClient;
    private Location currentLocation;
    private LocationRequest locationRequest;
    private TextToSpeech mySpeaker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // set layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // build fused location provider
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        // make location request
        locationRequest = new LocationRequest();
        locationRequest.setInterval(getResources().getInteger(
                R.integer.time_between_location_updates_ms));
        locationRequest.setFastestInterval(getResources().getInteger(
                R.integer.time_between_location_updates_ms) / 2);

        // create tts
        mySpeaker = new TextToSpeech(this,this);
    }

    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {
            Toast.makeText(this, R.string.talk_prompt, Toast.LENGTH_SHORT).show();
        } else {
            // if tts is not installed, notify user and exit app
            Toast.makeText(this, R.string.no_tts, Toast.LENGTH_LONG).show();
            finish();
        }
        // start locating
        startLocating(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // shut down tts
        mySpeaker.shutdown();
        // remove location updates
        fusedLocationClient.removeLocationUpdates(myLocationCallback);
    }

    private void startLocating(int accuracy) {

        locationRequest.setPriority(accuracy);
        try {
            fusedLocationClient.requestLocationUpdates(locationRequest,
                    myLocationCallback,Looper.myLooper());
        } catch (SecurityException e) {
            Toast.makeText(this,"Permission denied",Toast.LENGTH_SHORT).show();
        }
    }

    LocationCallback myLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            onLocationChanged(locationResult.getLastLocation());
        }
    };

    public void onLocationChanged(Location newLocation) {
        Log.i("IN onLocationChanged", "NEW LOCATION: " + newLocation.toString());
        // check if location has changed by more than 10 m
        if (currentLocation == null || currentLocation.distanceTo(newLocation) > 10) {
            // if so, update current location
            currentLocation = newLocation;

            // execute sld
            new SensorLocatorDecode(getApplicationContext(),this).execute(
                    currentLocation);
        }
        // if haven't moved
        else {
            speak(getResources().getString(R.string.not_moved));
        }
    }

    public void speak(String whatToSay) {
        // tts speak
        mySpeaker.speak(whatToSay, TextToSpeech.QUEUE_ADD, null, null);
    }

    public void setTextField(String whatToShow) {
        // set text
        TextView theTextView = findViewById(R.id.the_text);
        theTextView.setText(whatToShow);
    }

}
