package io.github.lily_wittle.talkingpicturelist;

import android.arch.persistence.room.Room;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private Cursor audioCursor;
    private MediaPlayer myPlayer;
    private boolean myPlayerIsPaused;
    private static final String DATABASE_NAME = "ImageAndDescription.db";
    private DataRoomDB imageAndDescriptionDB;
    private List<DataRoomEntity> globalListOfEntity;
    private TextToSpeech mySpeaker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // set content view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set up media player
        myPlayer = new MediaPlayer();
        myPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        myPlayer.setLooping(true);
        myPlayerIsPaused = false;

        // get audio content and play a random song
        String[] audioQueryFields = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA
        };
        audioCursor = getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, audioQueryFields, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if (audioCursor != null) {
            int numSongs = audioCursor.getCount();
            if (numSongs > 0) {
                // play random song
                int songPosition = (int) (Math.random() * numSongs);
                audioCursor.moveToPosition(songPosition);
                int audioDataIndex = audioCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
                String audioFilename = audioCursor.getString(audioDataIndex);
                try {
                    myPlayer.setDataSource(audioFilename);
                    myPlayer.prepare();
                    myPlayer.start();
                } catch (IOException e) {
                    Log.i("IN onCreate audio", "Exception: " + e.getMessage());
                }
            } else {
                Log.i("IN onCreate audio", "No songs found");
            }
        } else {
            Log.i("IN onCreate audio", "Cannot fetch songs");
        }

        // get database
        imageAndDescriptionDB = Room.databaseBuilder(getApplicationContext(),
                DataRoomDB.class,DATABASE_NAME).allowMainThreadQueries().build();

        // TODO: update database from media store

        // get global list of entities
        globalListOfEntity = imageAndDescriptionDB.daoAccess().fetchAll();

        // TODO: fill list view

        // make a TTS
        mySpeaker = new TextToSpeech(this,this);
        mySpeaker.setOnUtteranceProgressListener(myListener);
    }

    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {
            Toast.makeText(this,R.string.talk_prompt,Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this,"You need to install TextToSpeech",
                    Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private UtteranceProgressListener myListener =
            new UtteranceProgressListener() {
                @Override
                public void onStart(String utteranceId) {
                }

                @Override
                public void onDone(String utteranceId) {
                }

                @Override
                public void onError(String utteranceId) {
                }
            };

}
