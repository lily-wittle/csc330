package io.github.lily_wittle.phlogging;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.net.Uri;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.File;
import java.sql.Time;

public class CreateActivity extends AppCompatActivity implements SensorEventListener {

    private static final int ACTIVITY_TAKE_PICTURE = 1;
    private ImageView currentPhoto;
    private String cameraFileName;
    private long currentTime;

    private FusedLocationProviderClient fusedLocationClient;
    private Location currentLocation;
    private LocationRequest locationRequest;

    private SensorManager sensorManager;
    private float[] gravity = new float[3];
    private float[] magneticField = new float[3];
    private float[] orientation = new float[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // set content view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        // get location every minute
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(
                this);
        locationRequest = new LocationRequest();
        locationRequest.setInterval(getResources().getInteger(
                R.integer.time_between_location_updates_ms));
        startLocating(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // get imageview to display photo for phlog entry
        currentPhoto = findViewById(R.id.display_photo);

        // get current time in milliseconds and use for photo filename
        currentTime = System.currentTimeMillis();
        cameraFileName = "Phlogging_Photo_" + currentTime;

        // get sensor manager and start
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        // start sensors
        startSensor(Sensor.TYPE_MAGNETIC_FIELD);
        startSensor(Sensor.TYPE_ACCELEROMETER);
    }

    public void myCreateClickHandler(View view) {
        // deal with clicks to take photo and save buttons
        switch (view.getId()) {
            case R.id.take_photo:
                // open camera to take photo
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // store photo in file
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File(cameraFileName)));
                startActivityForResult(cameraIntent, ACTIVITY_TAKE_PICTURE);
                break;
            case R.id.save:
                // return intent to list activity
                Intent returnIntent = new Intent();
                // put title, time, text, photo in intent
                String title = ((EditText)findViewById(R.id.enter_title)).getText().toString();
                returnIntent.putExtra("title", title);
                returnIntent.putExtra("time", currentTime);
                String text = ((EditText)findViewById(R.id.enter_text)).getText().toString();
                returnIntent.putExtra("text", text);
                String photoUriAsString = Uri.fromFile(new File(cameraFileName)).toString();
                returnIntent.putExtra("photo_uri", photoUriAsString);
                // stop location updates and put location in intent
                fusedLocationClient.removeLocationUpdates(myLocationCallback);
                returnIntent.putExtra("latitude", currentLocation.getLatitude());
                returnIntent.putExtra("longitude", currentLocation.getLongitude());
                // turn off sensor listening and put orientation in intent
                sensorManager.unregisterListener(this);
                returnIntent.putExtra("orientation", orientation[0]);
                setResult(RESULT_OK, returnIntent);
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode, Intent returnedIntent) {
        // on result of camera activity
        super.onActivityResult(requestCode,resultCode,returnedIntent);
        switch (requestCode) {
            case ACTIVITY_TAKE_PICTURE:
                if (resultCode == Activity.RESULT_OK) {
                    // display the photo that was taken
                    currentPhoto.setImageURI(Uri.fromFile(new File(cameraFileName)));
                }
                break;
        }
    }

    private void startLocating(int accuracy) {
        // start getting location updates
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
            // update current location on callback
            currentLocation = locationResult.getLastLocation();
        }
    };

    private boolean startSensor(int sensorType) {
        // start sensing
        if (sensorManager.getSensorList(sensorType).isEmpty()) {
            return(false);
        } else {
            sensorManager.registerListener(this,
                    sensorManager.getDefaultSensor(sensorType),SensorManager.SENSOR_DELAY_NORMAL);
            return(true);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override
    public void onConfigurationChanged(Configuration newConfig) {}

    public void onSensorChanged(SensorEvent event) {
        boolean gravityChanged, magneticFieldChanged;
        float R[] = new float[9];
        float I[] = new float[9];
        float newOrientation[] = new float[3];

        // compute gravity and magnetic fields
        gravityChanged = magneticFieldChanged = false;
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                gravityChanged = arrayCopyChangeTest(event.values,gravity,1.0f);
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                magneticFieldChanged = arrayCopyChangeTest(event.values,
                        magneticField,1.0f);
                break;
            default:
                break;
        }

        // compute orientation
        if ((gravityChanged || magneticFieldChanged) &&
                SensorManager.getRotationMatrix(R,I,gravity,magneticField)) {
            SensorManager.getOrientation(R,newOrientation);
            newOrientation[0] = (float)Math.toDegrees(newOrientation[0]);
            newOrientation[1] = (float)Math.toDegrees(newOrientation[1]);
            newOrientation[2] = (float)Math.toDegrees(newOrientation[2]);
            arrayCopyChangeTest(newOrientation, orientation,2.0f);
        }
    }

    private boolean arrayCopyChangeTest(float[] from,float[] to, float amountForChange) {
        // copy new values into array and check for change
        int copyIndex;
        boolean changed = false;

        for (copyIndex=0;copyIndex < to.length;copyIndex++) {
            if (Math.abs(from[copyIndex] - to[copyIndex]) > amountForChange) {
                to[copyIndex] = from[copyIndex];
                changed = true;
            }
        }
        return(changed);
    }

}
