package io.github.lily_wittle.picturesong;

import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.net.Uri;
import java.io.IOException;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {

    private Cursor audioCursor;
    private Cursor imagesCursor;
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
        myPlayerIsPaused = false;

        Log.i("IN onCreate", "Layout and media player created ok");

        // get image content
        String[] imageQueryFields = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA
        };
        imagesCursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,imageQueryFields,null,null,
                MediaStore.Images.Media.DEFAULT_SORT_ORDER);
        if (imagesCursor != null) {
            int numImages = imagesCursor.getCount();
            if (numImages > 0) {
                // set random image
                int imagePosition = (int) (Math.random() * numImages);
                imagesCursor.moveToPosition(imagePosition);
                ImageView theImage = findViewById(R.id.picture);
                int imageDataIndex = imagesCursor.getColumnIndex(MediaStore.Images.Media.DATA);
                Uri imageURI = Uri.parse(imagesCursor.getString(imageDataIndex));
                theImage.setImageURI(imageURI);
            }
            else {
                Log.i("IN onCreate image", "No images found");
            }
        }
        else {
            Log.i("IN onCreate image", "Cannot fetch images");
        }

        // get audio content
        String[] audioQueryFields = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA
        };
        audioCursor = getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,audioQueryFields,null,null,
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
            }
            else {
                Log.i("IN onCreate audio", "No songs found");
            }
        }
        else {
            Log.i("IN onCreate audio", "Cannot fetch songs");
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        // resume song where it left off if the app comes back into focus
        if (myPlayerIsPaused) {
            myPlayer.start();
            myPlayerIsPaused = false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // pause song if the app goes out of focus
        if (myPlayer.isPlaying()) {
            myPlayer.pause();
            myPlayerIsPaused = true;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        audioCursor.close();
        imagesCursor.close();
        myPlayer.release();
    }

    public void onCompletion(MediaPlayer mediaPlayer) {
        // close app once the song ends
        finish();
    }

}
