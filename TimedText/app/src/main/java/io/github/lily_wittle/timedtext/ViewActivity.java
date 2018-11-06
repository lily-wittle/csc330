package io.github.lily_wittle.timedtext;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.util.Log;
import android.widget.SimpleAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.GregorianCalendar;

public class ViewActivity extends Activity {

    private static final String DATABASE_NAME = "SavedText.db";
    private DataRoomDB savedTextDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        // get database
        savedTextDB = Room.databaseBuilder(getApplicationContext(),
                DataRoomDB.class,DATABASE_NAME).allowMainThreadQueries().build();

        // populate entries of the list
        fillList();

        Log.i("IN view onCreate", "Created ok");
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        // close database
        savedTextDB.close();
        Log.i("IN view onDestroy", "Closed database");

    }

    private void fillList() {

        String[] displayFields = {"note", "time"};
        int[] displayViews = {R.id.note, R.id.time};

        // use simple adapter for the list view
        ListView theList = findViewById(R.id.note_list);
        SimpleAdapter listAdapter = new SimpleAdapter(this,fetchAllNotes(),
                R.layout.list_item, displayFields,displayViews);
        theList.setAdapter(listAdapter);

        Log.i("IN fillList", "Set adapter ok");

    }

    private ArrayList<HashMap<String, Object>> fetchAllNotes() {

        // get all notes from the database
        List<DataRoomEntity> dbEntities = savedTextDB.daoAccess().fetchAllNotes();
        ArrayList<HashMap<String,Object>> listItems = new ArrayList<>();
        HashMap<String,Object> oneItem;

        // add each note with its time to the list
        for (DataRoomEntity oneNote : dbEntities) {
            oneItem = new HashMap<>();
            oneItem.put("note", oneNote.getNote());

            // format time
            long time = oneNote.getTime();
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTimeInMillis(time);
            String formattedTime = calendar.getTime().toString();
            oneItem.put("time", formattedTime);

            listItems.add(oneItem);
        }

        return (listItems);

    }
}
