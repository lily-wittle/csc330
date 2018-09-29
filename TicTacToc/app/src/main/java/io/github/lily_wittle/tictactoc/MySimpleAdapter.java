package io.github.lily_wittle.tictactoc;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MySimpleAdapter extends SimpleAdapter {


    public MySimpleAdapter(Context context,List<? extends Map<String, ?>> data,
                           int resource,String[] keyNames,int[] fieldIds) {

        super(context,data,resource,keyNames,fieldIds);
        Log.i("IN adapter constructor", "Created adapter");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // method needed to set cells to blank at beginning, not red

        View view;
        View blank;

        view = super.getView(position, convertView, parent);

        blank = view.findViewById(R.id.blank_button);
        blank.setVisibility(View.VISIBLE);
        view.findViewById(R.id.two_button).setVisibility(View.INVISIBLE);
        view.findViewById(R.id.one_button).setVisibility(View.INVISIBLE);

        return (view);
    }

}
