package io.github.lily_wittle.phlogging;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.GregorianCalendar;

public class DetailActivity extends AppCompatActivity  {

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
        TextView orientationView = findViewById(R.id.current_orientation);
        String currentOrientation = "Orientation: " + orientation + " degrees";
        orientationView.setText(currentOrientation);
    }

    public void myDetailClickHandler(View view) {
        switch (view.getId()) {
            case R.id.back:
                // return to list view when back button is clicked
                finish();
                break;
        }
    }

}
