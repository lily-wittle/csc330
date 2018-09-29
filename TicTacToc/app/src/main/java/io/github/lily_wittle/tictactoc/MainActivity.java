package io.github.lily_wittle.tictactoc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private static final int ACTIVITY_PLAY = 1;
    private static double dividing_line = 0.5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("IN welcome onCreate", "Welcome screen created");
    }

    public void myClickHandler(View view) {

        Intent nextActivity;
        int who_starts;

        // biased randomize who starts the game
        double rand = Math.random();
        if (rand < dividing_line) {
            who_starts = 2; // player 2 starts
            dividing_line -= 0.1;
            Log.i("IN welcome clickHandler", "Player 2 start selected randomly, " +
                    "dividing line is now " + dividing_line);
        }
        else {
            who_starts = 1; // player 1 starts
            dividing_line += 0.1;
            Log.i("IN welcome clickHandler", "Player 1 start selected randomly, " +
                    "dividing line is now " + dividing_line);
        }

        switch (view.getId()) {
            case R.id.start_button:
                nextActivity = new Intent();
                nextActivity.setClassName("io.github.lily_wittle.tictactoc",
                        "io.github.lily_wittle.tictactoc.PlayActivity");
                nextActivity.putExtra(
                        "io.github.lily_wittle.tictactoc.who_starts",who_starts);
                startActivityForResult(nextActivity,ACTIVITY_PLAY);
                Log.i("IN welcome clickHandler", "Play activity started");
                break;
            default:
                break;
        }
    }

}
