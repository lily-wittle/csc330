package io.github.lily_wittle.tictactoc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.util.Log;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import android.content.Intent;

import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

public class PlayActivity extends AppCompatActivity
        implements OnItemClickListener {

    private static int turn;
    private MySimpleAdapter gridSimpleAdapter;
    private int[] play_cell_content;
    private HashMap<Integer, List<int[]>> winningCombos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        turn = (this.getIntent().getIntExtra(
                "io.github.lily_wittle.tictactoc.who_starts",-1));

        View indicator;

        if (turn == 1) {
            indicator = findViewById(R.id.p2_turn_indicator);
        }
        else {
            indicator = findViewById(R.id.p1_turn_indicator);
        }
        indicator.setVisibility(View.INVISIBLE);

        makeGridSimpleAdapter();
        ((GridView)findViewById(R.id.play_grid)).setAdapter(gridSimpleAdapter);
        ((GridView)findViewById(R.id.play_grid)).setOnItemClickListener(this);

        makeComboMap();
        play_cell_content = new int[9];
        Arrays.fill(play_cell_content,0);

        Log.i("IN play onCreate", "Play activity started, turn = " + turn);
    }

    private void makeGridSimpleAdapter() {

        ArrayList<HashMap<String,Integer>> listItems;
        HashMap<String,Integer> oneItem;
        String[] fromHashMapFieldNames = {"index"};
        int[] toListRowFieldIds = {R.id.blank_button};
        int index;

        listItems = new ArrayList<>();
        for (index = 0; index < 9; index++) {
            oneItem = new HashMap<>();
            oneItem.put("index",index);
            listItems.add(oneItem);
        }

        gridSimpleAdapter = new MySimpleAdapter(this,listItems,
                R.layout.grid_cell,fromHashMapFieldNames,toListRowFieldIds);
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (play_cell_content[position] == 0) {
            View blank;
            View color;

            blank = view.findViewById(R.id.blank_button);
            if (turn == 1) {
                color = view.findViewById(R.id.red_button);
            }
            else {
                color = view.findViewById(R.id.green_button);
            }

            blank.setVisibility(View.INVISIBLE);
            color.setVisibility(View.VISIBLE);

            gridSimpleAdapter.play(position, turn);
            play(position);
            switchTurn();
        }
    }

    public void play(int position) {
        // set play_cell_content and check for win or tie
        play_cell_content[position] = turn;
        List<int[]> combos = winningCombos.get(position);

        // check for win
        for (int i=0; i < combos.size(); i++) {
            if (play_cell_content[combos.get(i)[0]] == turn &&
                    play_cell_content[combos.get(i)[1]] == turn) {

                Intent returnIntent;

                returnIntent = new Intent();
                returnIntent.putExtra(
                        "io.github.lily_wittle.tictactoc.game_result", turn);
                setResult(RESULT_OK, returnIntent);
                finish();

                Log.i("IN play", turn + " won!");
            }
        }

        // check for tie
        boolean isTie = true;
        for (int i=0; i<9; i++) {
            if (play_cell_content[i] == 0) {
                isTie = false;
            }
        }
        if (isTie) {
            Intent returnIntent;

            returnIntent = new Intent();
            returnIntent.putExtra(
                    "io.github.lily_wittle.tictactoc.game_result", 0);
            setResult(RESULT_OK, returnIntent);
            finish();

            Log.i("IN play", "Tie");
        }

        Log.i("IN play", "Played " + turn);
    }


    public void switchTurn() {
        View red;
        View green;

        red = findViewById(R.id.p1_turn_indicator);
        green = findViewById(R.id.p2_turn_indicator);

        if (turn == 1) {
            turn = 2;
            red.setVisibility(View.INVISIBLE);
            green.setVisibility(View.VISIBLE);
        }
        else {
            turn = 1;
            green.setVisibility(View.INVISIBLE);
            red.setVisibility(View.VISIBLE);
        }
        Log.i("IN switchTurn", "Switched turn");
    }

    public void makeComboMap() {
        // initialize map that has winning combos for each move in it
        winningCombos = new HashMap<>();

        for (int pos = 0; pos < 9; pos++) {
            winningCombos.put(pos, getComboList(pos));
        }

    }

    public List<int[]> getComboList(int position) {
        // get list of complementary positions for a win given a position

        List<int[]> comboArray = new ArrayList<>();
        int[] currPair = new int[2];

        switch (position) {
            case 0:
                int[] zeroArray = {1, 2, 3, 6, 4, 8};
                for (int i=0; i < zeroArray.length-1; i+=2) {
                    int[] thePair = {zeroArray[i], zeroArray[i+1]};
                    comboArray.add(thePair);
                }
                break;
            case 1:
                int[] oneArray = {0, 2, 4, 7};
                for (int i=0; i < oneArray.length-1; i+=2) {
                    int[] thePair = {oneArray[i], oneArray[i+1]};
                    comboArray.add(thePair);
                }
                break;
            case 2:
                int[] twoArray = {0, 1, 4, 6, 5, 8};
                for (int i=0; i < twoArray.length-1; i+=2) {
                    int[] thePair = {twoArray[i], twoArray[i+1]};
                    comboArray.add(thePair);
                }
                break;
            case 3:
                int[] threeArray = {0, 6, 4, 5};
                for (int i=0; i < threeArray.length-1; i+=2) {
                    int[] thePair = {threeArray[i], threeArray[i+1]};
                    comboArray.add(thePair);
                }
                break;
            case 4:
                int[] fourArray = {0, 8, 2, 6, 1, 7, 3, 5};
                for (int i=0; i < fourArray.length-1; i+=2) {
                    int[] thePair = {fourArray[i], fourArray[i+1]};
                    comboArray.add(thePair);
                }
                break;
            case 5:
                int[] fiveArray = {2, 8, 3, 4};
                for (int i=0; i < fiveArray.length-1; i+=2) {
                    int[] thePair = {fiveArray[i], fiveArray[i+1]};
                    comboArray.add(thePair);
                }
                break;
            case 6:
                int[] sixArray = {0, 3, 2, 4, 7, 8};
                for (int i=0; i < sixArray.length-1; i+=2) {
                    int[] thePair = {sixArray[i], sixArray[i+1]};
                    comboArray.add(thePair);
                }
                break;
            case 7:
                int[] sevenArray = {1, 4, 6, 8};
                for (int i=0; i < sevenArray.length-1; i+=2) {
                    int[] thePair = {sevenArray[i], sevenArray[i+1]};
                    comboArray.add(thePair);
                }
                break;
            case 8:
                int[] eightArray = {0, 4, 2, 5, 6, 7};
                for (int i=0; i < eightArray.length-1; i+=2) {
                    int[] thePair = {eightArray[i], eightArray[i+1]};
                    comboArray.add(thePair);
                }
                break;
        }

        return comboArray;
    }

}
