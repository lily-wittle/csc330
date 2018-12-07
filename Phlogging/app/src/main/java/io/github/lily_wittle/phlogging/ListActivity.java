package io.github.lily_wittle.phlogging;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

public class ListActivity extends AppCompatActivity implements
        SimpleAdapter.ViewBinder,
        AdapterView.OnItemClickListener {

    private static final int ACTIVITY_CREATE = 1;
    private static final String DATABASE_NAME = "Phlogging.db";
    private DataRoomDB phloggingDB;
    private List<DataRoomEntity> globalListOfEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // set content view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // get database
        phloggingDB = Room.databaseBuilder(getApplicationContext(),
                DataRoomDB.class,DATABASE_NAME).allowMainThreadQueries().build();

        // get entries to fill view
        globalListOfEntity = phloggingDB.daoAccess().fetchAll();
        fillListView();
    }

    @Override
    public void onDestroy() {
        // when the app is destroyed, need to close database as well

        super.onDestroy();
        phloggingDB.close();
    }

    public void myClickHandler(View view) {
        // deal with click to create button
        switch (view.getId()) {
            case R.id.create:
                Intent createActivity = new Intent();
                createActivity.setClassName("io.github.lily_wittle.phlogging",
                        "io.github.lily_wittle.phlogging.CreateActivity");
                startActivityForResult(createActivity, ACTIVITY_CREATE);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode, Intent returnedIntent) {
        super.onActivityResult(requestCode, resultCode, returnedIntent);
        switch (requestCode) {
            case ACTIVITY_CREATE:
                // get data for and add new phlog entry
                if (resultCode == Activity.RESULT_OK) {
                    // get extras from intent
                    String title = returnedIntent.getStringExtra("title");
                    long time = returnedIntent.getLongExtra("time", System.currentTimeMillis());
                    String text = returnedIntent.getStringExtra("text");
                    String photo_uri = returnedIntent.getStringExtra("photo_uri");
                    double latitude = returnedIntent.getDoubleExtra("latitude", 0);
                    double longitude = returnedIntent.getDoubleExtra("longitude", 0);
                    float orientation = returnedIntent.getFloatExtra("orientation", 0);
                    // make new entry and insert into db
                    DataRoomEntity newPhlog = new DataRoomEntity();
                    newPhlog.setTitle(title);
                    newPhlog.setTime(time);
                    newPhlog.setText(text);
                    newPhlog.setPhoto(photo_uri);
                    newPhlog.setLatitude(latitude);
                    newPhlog.setLongitude(longitude);
                    newPhlog.setOrientation(orientation);
                    phloggingDB.daoAccess().addPhlog(newPhlog);
                    // display in list
                    globalListOfEntity.add(newPhlog);
                    fillListView();
                }
                break;
            default:
                break;
        }
    }

    private void fillListView() {
        // use adapter to fill list view

        String[] displayFields = {
                "photo",
                "title",
                "date"
        };
        int[] displayViews = {
                R.id.item_photo,
                R.id.item_title,
                R.id.item_date
        };
        ListView theList = findViewById(R.id.the_list);
        SimpleAdapter listAdapter = new SimpleAdapter(this, fetchAllEntries(),
                R.layout.list_item, displayFields, displayViews);
        // use view binder to set thumbnails and descriptions
        listAdapter.setViewBinder(this);
        // set click listeners and adapters
        theList.setOnItemClickListener(this);
        theList.setAdapter(listAdapter);
    }

    private ArrayList<HashMap<String,Object>> fetchAllEntries() {
        // get arraylist of all phlog entries with their thumbnails, titles, and dates

        // convert globalListOfEntity to ArrayList
        ArrayList<HashMap<String,Object>> listItems = new ArrayList<>();
        for (DataRoomEntity oneEntry : globalListOfEntity) {
            HashMap<String,Object> oneItem = new HashMap<>();
            // put title in item
            oneItem.put("title", oneEntry.getTitle());
            // put formatted date in item
            long time = oneEntry.getTime();
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTimeInMillis(time);
            String formattedTime = calendar.getTime().toString();
            oneItem.put("date", formattedTime);
            // put photo uri in item
            Uri photoUri = Uri.parse(oneEntry.getPhoto());
            oneItem.put("photo", photoUri);
            // add item to list
            listItems.add(oneItem);
        }
        return(listItems);
    }

    @Override
    public boolean setViewValue(View view, Object data, String asText) {
        // for use in list view adapter, setting image thumbnail, title and date

        switch(view.getId()) {
            // set title
            case R.id.item_title:
                ((TextView)view).setText((String)data);
                break;
            // set date
            case R.id.item_date:
                ((TextView)view).setText((String)data);
                break;
            // set photo
            case R.id.item_photo:
                ((ImageView)view).setImageURI((Uri)data);
                break;
        }
        return(true);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // TODO: open display activity (make display activity)
    }
}
