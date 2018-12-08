package io.github.lily_wittle.phlogging;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.GregorianCalendar;

public class DetailActivity extends AppCompatActivity  {

    private int entryPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // set content view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // get extras
        String title = this.getIntent().getStringExtra("title");
        long time = this.getIntent().getLongExtra("time", System.currentTimeMillis());
        String text = this.getIntent().getStringExtra("text");
        String photo = this.getIntent().getStringExtra("photo");
        double latitude = this.getIntent().getDoubleExtra("latitude", 0);
        double longitude = this.getIntent().getDoubleExtra("longitude", 0);
        float orientation = this.getIntent().getFloatExtra("orientation", 0);
        entryPosition = this.getIntent().getIntExtra("entryPosition", -1);

        // display values
        TextView titleView = findViewById(R.id.title);
        titleView.setText(title);
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(time);
        String formattedTime = calendar.getTime().toString();
        TextView timeView = findViewById(R.id.time);
        timeView.setText(formattedTime);
        TextView textView = findViewById(R.id.text);
        textView.setText(text);
        ImageView photoView = findViewById(R.id.photo);
        photoView.setImageURI(Uri.parse(photo));
        TextView locationView = findViewById(R.id.location);
        String currentLatLong = "Location: (" + latitude + ", " + longitude + ")";
        locationView.setText(currentLatLong);
        TextView orientationView = findViewById(R.id.orientation);
        String currentOrientation = "Orientation: " + orientation + " degrees";
        orientationView.setText(currentOrientation);
    }

    public void myDetailClickHandler(View view) {
        // when back or delete button is clicked, return to list view

        Intent returnIntent = new Intent();
        switch (view.getId()) {
            case R.id.back:
                returnIntent.putExtra("delete", false);
                break;
            case R.id.delete:
                returnIntent.putExtra("delete", true);
                returnIntent.putExtra("entryPosition", entryPosition);
                break;
        }
        setResult(RESULT_OK, returnIntent);
        finish();
    }

}
