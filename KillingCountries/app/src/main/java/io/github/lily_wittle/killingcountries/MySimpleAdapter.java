package io.github.lily_wittle.killingcountries;

import android.widget.SimpleAdapter;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import android.widget.ImageView;

//=============================================================================
//----Have to do this because grid views are recycled
class MySimpleAdapter extends SimpleAdapter {
    //-----------------------------------------------------------------------------
    boolean[] displayImage;
    //-----------------------------------------------------------------------------
    public MySimpleAdapter(Context context,List<? extends Map<String,?>> data,
                           int resource,String[] keyNames,int[] fieldIds) {

        super(context,data,resource,keyNames,fieldIds);

        displayImage = new boolean[getCount()];
        Arrays.fill(displayImage,false);
        Log.i("IN mySimpleAdapter", "Constructed simple adapter");
    }
    //-----------------------------------------------------------------------------
    public void setDisplayImage(int position) {
        displayImage[position] = true;
        Log.i("IN setDisplayImage", "Set displayImage to true");
    }
    //-----------------------------------------------------------------------------
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        TextView name;
        ImageView smiley;

//----Let the superclass decide whether or not to recycle
        view = super.getView(position, convertView, parent);
        name = view.findViewById(R.id.country_name);
        smiley = view.findViewById(R.id.country_picture);
        if (displayImage[position]) {
            name.setVisibility(View.INVISIBLE);
            smiley.setVisibility(View.VISIBLE);
        } else {
            name.setVisibility(View.VISIBLE);
            smiley.setVisibility(View.INVISIBLE);
        }
        return (view);
    }
//-----------------------------------------------------------------------------
}
//=============================================================================
