package io.github.lily_wittle.killingcountries;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.HashMap;
import java.util.ArrayList;
import android.widget.GridView;
import android.widget.AdapterView;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements OnItemClickListener {

    private MySimpleAdapter gridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("IN onCreate", "Created OK");
        makeGridAdapter();
        ((GridView)findViewById(R.id.the_grid)).setAdapter(gridAdapter);
        ((GridView)findViewById(R.id.the_grid)).setOnItemClickListener(this);
    }

    private void makeGridAdapter() {
        String[] countries;
        ArrayList<HashMap<String, String>> gridItems;
        HashMap<String, String> oneItem;
        String[] hashMapFieldNames = {"name"};
        int[] toGridFieldIds = {R.id.country_name};
        int i;

        countries = getResources().getStringArray(R.array.countries_array);
        gridItems = new ArrayList<>();
        for (i = 0; i < countries.length; i++) {
            oneItem = new HashMap<>();
            oneItem.put("name", countries[i]);
            gridItems.add(oneItem);
        }
        Log.i("IN fillGrid", "Filled items into grid");
        gridAdapter = new MySimpleAdapter(this, gridItems, R.layout.grid_cell, hashMapFieldNames, toGridFieldIds);
    }

    public void onItemClick(AdapterView<?> parent,View view,int position, long id) {
        TextView name;
        ImageView smiley;

        name = view.findViewById(R.id.country_name);
        smiley = view.findViewById(R.id.country_picture);
        name.setVisibility(View.INVISIBLE);
        smiley.setVisibility(View.VISIBLE);
        Log.i("IN onItemClick", "Made image visible");
        gridAdapter.setDisplayImage(position);
    }

}
