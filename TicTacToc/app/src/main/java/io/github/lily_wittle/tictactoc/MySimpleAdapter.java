package io.github.lily_wittle.tictactoc;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MySimpleAdapter extends SimpleAdapter {

    private int[] play_cell_content;

    public MySimpleAdapter(Context context,List<? extends Map<String, ?>> data,
                           int resource,String[] keyNames,int[] fieldIds) {

        super(context,data,resource,keyNames,fieldIds);

        play_cell_content = new int[getCount()];
        Arrays.fill(play_cell_content,0);

        Log.i("IN adapter constructor", "Created adapter");
    }

    public void play(int position, int status) {
        play_cell_content[position] = status;
        Log.i("IN adapter play", "Played " + status);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        View blank;
        View color;

        view = super.getView(position, convertView, parent);

        blank = view.findViewById(R.id.blank_button);
        if (play_cell_content[position] == 1) {
            color = view.findViewById(R.id.red_button);
            blank.setVisibility(View.INVISIBLE);
            color.setVisibility(View.VISIBLE);
        }
        else if (play_cell_content[position] == 2){
            color = view.findViewById(R.id.green_button);
            blank.setVisibility(View.INVISIBLE);
            color.setVisibility(View.VISIBLE);
        }
        else {
            blank.setVisibility(View.VISIBLE);
            view.findViewById(R.id.green_button).setVisibility(View.INVISIBLE);
            view.findViewById(R.id.red_button).setVisibility(View.INVISIBLE);
        }
        return (view);
    }

}
