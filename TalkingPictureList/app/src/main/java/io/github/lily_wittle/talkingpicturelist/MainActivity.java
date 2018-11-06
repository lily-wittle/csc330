package io.github.lily_wittle.talkingpicturelist;

import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private Cursor audioCursor;
    private MediaPlayer myPlayer;
    private boolean myPlayerIsPaused;

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

        Log.i("IN onCreate", "Layout and media player created ok");

        // get audio content
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
    }
}
