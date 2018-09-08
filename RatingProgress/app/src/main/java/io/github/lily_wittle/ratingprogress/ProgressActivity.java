package io.github.lily_wittle.ratingprogress;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.os.Handler;
import android.util.Log;
import android.content.Intent;

public class ProgressActivity extends Activity {

    private ProgressBar myProgressBar;
    private int barClickTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        myProgressBar = findViewById(R.id.time_left);
        myProgressBar.setProgress(myProgressBar.getMax());
        barClickTime = getResources().getInteger(R.integer.bar_click_time);
        myProgresser.run();
        Log.i("IN Progress onCreate", "Created OK");
    }

    private final Runnable myProgresser = new Runnable() {

        private Handler myHandler = new Handler();

        public void run() {
            Intent returnIntent;

            myProgressBar.setProgress(myProgressBar.getProgress()-barClickTime);

            if (myProgressBar.getProgress() == 0) {
                returnIntent = new Intent();
                setResult(RESULT_OK, returnIntent);
                finish();
            }

            if (!myHandler.postDelayed(myProgresser,barClickTime)) {
                Log.e("ERROR","Cannot postDelayed");
            }
        }
    };
}
