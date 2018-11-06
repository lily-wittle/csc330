package io.github.lily_wittle.timedtext;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private static final String DATABASE_NAME = "SavedText.db";
    private DataRoomDB savedTextDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get database
        savedTextDB = Room.databaseBuilder(getApplicationContext(),
                DataRoomDB.class,DATABASE_NAME).allowMainThreadQueries().build();

        Log.i("IN main onCreate", "Created ok");

    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        // close database
        savedTextDB.close();
        Log.i("IN main onDestroy", "Closed database");

    }

    public void myClickHandler(View view) {

        switch(view.getId()) {
            case R.id.save:
                Log.i("IN myClickHandler", "Clicked save");
                // save new note in database
                DataRoomEntity newNoteEntity = new DataRoomEntity();
                EditText textField = this.findViewById(R.id.text_field);
                String newText = textField.getText().toString();
                newNoteEntity.setNote(newText);
                long newTime = System.currentTimeMillis();
                newNoteEntity.setTime(newTime);
                savedTextDB.daoAccess().addNote(newNoteEntity);
                // clear field so another note can be written
                textField.setText("");
                break;
            case R.id.view:
                Log.i("IN myClickHandler", "Clicked view");
                // view all notes that have been saved in view activity
                Intent nextActivity = new Intent();
                nextActivity.setClassName("io.github.lily_wittle.timedtext",
                        "io.github.lily_wittle.timedtext.ViewActivity");
                startActivity(nextActivity);
                break;
            default:
                break;
        }

    }

}
