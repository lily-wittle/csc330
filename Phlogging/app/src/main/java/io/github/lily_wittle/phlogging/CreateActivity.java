package io.github.lily_wittle.phlogging;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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

public class CreateActivity extends AppCompatActivity {

    private static final int ACTIVITY_TAKE_PICTURE = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private Location currentLocation;
    private LocationRequest locationRequest;
    private ImageView currentPhoto;

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

        //TODO: sensors
    }

    public void myCreateClickHandler(View view) {
        // deal with clicks to take photo and save buttons
        switch (view.getId()) {
            case R.id.take_photo:
                // open camera to take photo
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, ACTIVITY_TAKE_PICTURE);
                break;
            case R.id.save:
                // return intent to list activity
                Intent returnIntent = new Intent();
                // put title, text, photo, location, orientation in intent
                String title = ((EditText)findViewById(R.id.enter_title)).getText().toString();
                returnIntent.putExtra("title", title);
                String text = ((EditText)findViewById(R.id.enter_text)).getText().toString();
                returnIntent.putExtra("text", text);
                Bitmap photoToSave = ((BitmapDrawable)currentPhoto.getDrawable()).getBitmap();
                returnIntent.putExtra("photo_bitmap", photoToSave);
                // stop location updates
                fusedLocationClient.removeLocationUpdates(myLocationCallback);
                returnIntent.putExtra("location", currentLocation);
                // TODO: orientation

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
                    currentPhoto.setImageBitmap((Bitmap) returnedIntent.getExtras().get("data"));
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

}
