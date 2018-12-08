package io.github.lily_wittle.phlogging;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.net.Uri;
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

        // get entries and fill view
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
                    String photo = returnedIntent.getStringExtra("photo");
                    double latitude = returnedIntent.getDoubleExtra("latitude", 0);
                    double longitude = returnedIntent.getDoubleExtra("longitude", 0);
                    float orientation = returnedIntent.getFloatExtra("orientation", 0);
                    // make new entry and insert into db
                    DataRoomEntity newPhlog = new DataRoomEntity();
                    newPhlog.setTitle(title);
                    newPhlog.setTime(time);
                    newPhlog.setText(text);
                    newPhlog.setPhoto(photo);
                    newPhlog.setLatitude(latitude);
                    newPhlog.setLongitude(longitude);
                    newPhlog.setOrientation(orientation);
                    phloggingDB.daoAccess().addPhlog(newPhlog);
                    // display in list
                    fillListView();
                }
                break;
            default:
                break;
        }
    }

    private void fillListView() {
        // use adapter to fill list view
        globalListOfEntity = phloggingDB.daoAccess().fetchAll();
        String[] displayFields = {
                "photo",
                "title",
                "time"
        };
        int[] displayViews = {
                R.id.item_photo,
                R.id.item_title,
                R.id.item_time
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
        // get arraylist of all phlog entries with their thumbnails, titles, and times
        ArrayList<HashMap<String,Object>> listItems = new ArrayList<>();
        for (DataRoomEntity oneEntry : globalListOfEntity) {
            HashMap<String,Object> oneItem = new HashMap<>();
            // put title in item
            oneItem.put("title", oneEntry.getTitle());
            // put formatted time in item
            long time = oneEntry.getTime();
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTimeInMillis(time);
            String formattedTime = calendar.getTime().toString();
            oneItem.put("time", formattedTime);
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
        // for use in list view adapter, setting image thumbnail, title and time
        switch(view.getId()) {
            // set title
            case R.id.item_title:
                ((TextView)view).setText((String)data);
                break;
            // set time
            case R.id.item_time:
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
        // when item is clicked, open detail activity

        DataRoomEntity dbEntry = globalListOfEntity.get(position);

        Intent detailActivity = new Intent();
        detailActivity.setClassName("io.github.lily_wittle.phlogging",
                "io.github.lily_wittle.phlogging.DisplayActivity");
        // put details as extras in intent
        detailActivity.putExtra("title", dbEntry.getTitle());
        detailActivity.putExtra("time", dbEntry.getTime());
        detailActivity.putExtra("text", dbEntry.getText());
        detailActivity.putExtra("photo", dbEntry.getPhoto());
        detailActivity.putExtra("latitude", dbEntry.getLatitude());
        detailActivity.putExtra("longitude", dbEntry.getLongitude());
        detailActivity.putExtra("orientation", dbEntry.getOrientation());

        startActivity(detailActivity);
    }

    // TODO: long click delete
}
