package io.github.lily_wittle.ratingprogress;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.RatingBar;
import android.util.Log;

public class RatingActivity extends AppCompatActivity {

    private static final int ACTIVITY_PROGRESS = 1;
    private RatingBar theRatingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        theRatingBar = findViewById(R.id.rate);
        Log.i("IN Rating onCreate", "Created OK");
    }

    public void myClickHandler(View view) {
        Intent nextActivity;

        switch (view.getId()) {
            case R.id.start_button:
                Log.i("IN myClickHandler", "Start button clicked");
                findViewById(R.id.start_button).setClickable(false);
                nextActivity = new Intent();
                nextActivity.setClassName("io.github.lily_wittle.ratingprogress",
                        "io.github.lily_wittle.ratingprogress.ProgressActivity");
                startActivityForResult(nextActivity, ACTIVITY_PROGRESS);
                Log.i("IN myClickHandler", "Progress activity started");
                break;
            default:
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case ACTIVITY_PROGRESS:
                Log.i("IN onActivityResult", "Progress activity returned");
                if (resultCode == Activity.RESULT_OK) {
                    Log.i("IN onActivityResult", "Result returned OK");
                    theRatingBar.setRating(theRatingBar.getRating()+1);
                    Log.i("IN onActivityResult", "Added a star to the rating bar");
                    if (theRatingBar.getRating() >= theRatingBar.getNumStars()) {
                        Log.i("IN onActivityResult", "Rating bar full, exiting");
                        finish();
                    }
                    else {
                        findViewById(R.id.start_button).setClickable(true);
                    }
                }
                else {
                    Log.i("IN onActivityResult", "Result cancelled before progress bar was done");
                    findViewById(R.id.start_button).setClickable(true);
                }
                break;
            default:
                break;
        }
    }
}
