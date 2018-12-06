package io.github.lily_wittle.phlogging;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ListActivity extends AppCompatActivity {

    private static final int ACTIVITY_CREATE = 1;
    private static final String DATABASE_NAME = "Phlogging.db";
    private DataRoomDB phloggingDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // set content view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // get database
        phloggingDB = Room.databaseBuilder(getApplicationContext(),
                DataRoomDB.class,DATABASE_NAME).allowMainThreadQueries().build();
    }

    public void myClickHandler(View view) {
        // deal with click to create button
        switch (view.getId()) {
            case R.id.create:
                Intent createActivity = new Intent();
                createActivity.setClassName("io.github.lily_wittle.phlogging",
                        "io.github.lily_wittle.phlogging.CreateActivity");
                startActivityForResult(createActivity, ACTIVITY_CREATE);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode, Intent returnedIntent) {
        // on result of create activity
        super.onActivityResult(requestCode, resultCode, returnedIntent);
        switch (requestCode) {
            case ACTIVITY_CREATE:
                if (resultCode == Activity.RESULT_OK) {
                    // get extras from intent
                    String title = returnedIntent.getStringExtra("title");
                    long time = returnedIntent.getLongExtra("time", System.currentTimeMillis());
                    String text = returnedIntent.getStringExtra("text");
                    String photo_uri = returnedIntent.getStringExtra("photo_uri");
                    double latitude = returnedIntent.getDoubleExtra("latitude", 0);
                    double longitude = returnedIntent.getDoubleExtra("longitude", 0);
                    float orientation = returnedIntent.getFloatExtra("orientation", 0);
                    // make new entry
                    DataRoomEntity newPhlog = new DataRoomEntity();
                    newPhlog.setTitle(title);
                    newPhlog.setTime(time);
                    newPhlog.setText(text);
                    newPhlog.setPhoto(photo_uri);
                    newPhlog.setLatitude(latitude);
                    newPhlog.setLongitude(longitude);
                    newPhlog.setOrientation(orientation);
                    // insert into db
                    phloggingDB.daoAccess().addPhlog(newPhlog);
                }
                break;
            default:
                break;
        }
    }
}
