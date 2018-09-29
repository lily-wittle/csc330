package io.github.lily_wittle.tictactoc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

public class PlayActivity extends AppCompatActivity
        implements OnItemClickListener {

    private static int turn;
    private MySimpleAdapter gridSimpleAdapter;

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

        Log.i("IN play onCreate", "Play activity started, turn = " + turn);
    }

    public void playClickHandler(View view) {
        Log.i("IN play clickHandler", "Play button clicked");
        if (turn == 1) {

        }
        else {
            int a = 1;
        }
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

        Log.i("IN onItemClick", "Made a move");
    }

}
