package io.github.lily_wittle.tictactoc;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.util.Log;
import android.widget.RatingBar;


public class MainActivity extends AppCompatActivity {

    private static final int ACTIVITY_PLAY = 1;
    private double dividing_line = 0.5;
    private RatingBar RatingOne;
    private RatingBar RatingTwo;
    private int playTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // rating bars to keep track of tournament
        RatingOne = findViewById(R.id.p1_rate);
        RatingTwo = findViewById(R.id.p2_rate);

        Log.i("IN welcome onCreate", "Welcome screen created");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflate options menu
        MenuInflater inflater;

        inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);

        return(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.reset:
                // reset tournament progress
                RatingOne.setRating(0);
                RatingTwo.setRating(0);
                findViewById(R.id.start_button).setVisibility(View.VISIBLE);
                return(true);
            // set play time
            case R.id.one:
                playTime = 1000;
                return(true);
            case R.id.two:
                playTime = 2000;
                return(true);
            case R.id.five:
                playTime = 5000;
                return(true);
            case R.id.ten:
                playTime = 10000;
                return(true);
            default:
                return(super.onOptionsItemSelected(item));
        }
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
                // start play activity
                nextActivity = new Intent();
                nextActivity.setClassName("io.github.lily_wittle.tictactoc",
                        "io.github.lily_wittle.tictactoc.PlayActivity");
                nextActivity.putExtra(
                        "io.github.lily_wittle.tictactoc.who_starts",who_starts);
                nextActivity.putExtra("io.github.lily_wittle.tictactoc.play_time",playTime);
                startActivityForResult(nextActivity,ACTIVITY_PLAY);
                Log.i("IN welcome clickHandler", "Play activity started");
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        int whoWon;

        switch (requestCode) {
            case ACTIVITY_PLAY:
                if (resultCode == Activity.RESULT_OK) {
                    whoWon = data.getIntExtra(
                            "io.github.lily_wittle.tictactoc.game_result", 0);
                    if (whoWon == 1 || whoWon == 2) {
                        RatingBar winner;
                        if (whoWon == 1) {
                            winner = RatingOne;
                        } else {
                            winner = RatingTwo;
                        }
                        // increment rating bar based on winner
                        winner.setRating(winner.getRating() + 1);
                        if (winner.getRating() == 5) {
                            // if tournament was won
                            findViewById(R.id.start_button).setVisibility(View.INVISIBLE);
                        }
                    }
                    // don't do anything if tie
                }
                break;
        }

    }

}
