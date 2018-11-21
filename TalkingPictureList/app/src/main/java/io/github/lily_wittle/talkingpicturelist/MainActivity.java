package io.github.lily_wittle.talkingpicturelist;

import android.arch.persistence.room.Room;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener,
        SimpleAdapter.ViewBinder, AdapterView.OnItemClickListener, MediaPlayer.OnCompletionListener,
        ImageDialogFragment.StopTalking {

    private Cursor globalMediaStoreCursor;
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

        // play a random song
        startRandomSong();

        // get database
        imageAndDescriptionDB = Room.databaseBuilder(getApplicationContext(),
                DataRoomDB.class,DATABASE_NAME).allowMainThreadQueries().build();

        // update database from media store
        updateDBFromMediaStore();

        // get global list of entities
        globalListOfEntity = imageAndDescriptionDB.daoAccess().fetchAll();

        // fill list view
        fillListView();

        // make a TTS
        mySpeaker = new TextToSpeech(this,this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // close database
        imageAndDescriptionDB.close();
        // close media store cursor
        globalMediaStoreCursor.close();
        // release media player
        myPlayer.release();
        // shut down TTS
        mySpeaker.shutdown();
    }

    private void startRandomSong() {
        // want id, title, and data for each song
        String[] audioQueryFields = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA
        };
        // query the media store
        Cursor audioCursor = getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, audioQueryFields, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        // get songs from audio cursor
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
                    myPlayer.setOnCompletionListener(this);
                } catch (IOException e) {
                    Log.i("IN onCreate audio", "Exception: " + e.getMessage());
                }
            } else {
                Log.i("IN onCreate audio", "No songs found");
            }
            audioCursor.close();
        } else {
            Log.i("IN onCreate audio", "Cannot fetch songs");
        }
    }

    private void updateDBFromMediaStore() {
        // want id and data for each image
        String[] imageQueryFields = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA,
        };
        // query the media store
        globalMediaStoreCursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,imageQueryFields,null,null,
                MediaStore.Images.Media.DEFAULT_SORT_ORDER);
        // get images from image cursor
        if (globalMediaStoreCursor != null & globalMediaStoreCursor.getCount() > 0) {
            globalMediaStoreCursor.moveToFirst();
            do {
                long imageId = globalMediaStoreCursor.getLong(
                        globalMediaStoreCursor.getColumnIndex(MediaStore.Images.Media._ID));
                // if not in db, add to db
                if (imageAndDescriptionDB.daoAccess().getEntryByImageId(imageId) == null) {
                    DataRoomEntity imageData = new DataRoomEntity();
                    imageData.setImageId(imageId);
                    imageAndDescriptionDB.daoAccess().addEntry(imageData);
                }
            } while (globalMediaStoreCursor.moveToNext());
        }
        else {
            Log.i("IN onCreate image", "Cannot fetch images");
        }
    }

    private void fillListView() {
        // use adapter to fill list view
        String[] displayFields = {
                "thumbnail",
                "description"
        };
        int[] displayViews = {
                R.id.item_image,
                R.id.item_text
        };
        ListView theList = findViewById(R.id.the_list);
        SimpleAdapter listAdapter = new SimpleAdapter(this, fetchAllTalkingPictures(),
                R.layout.list_item, displayFields, displayViews);
        // use view binder to set thumbnails and descriptions
        listAdapter.setViewBinder(this);
        // set click listeners
        theList.setOnItemClickListener(this);
        // TODO: long click
        theList.setAdapter(listAdapter);
    }

    private ArrayList<HashMap<String,Object>> fetchAllTalkingPictures() {
        // convert globalListOfEntity to ArrayList
        ArrayList<HashMap<String,Object>> listItems = new ArrayList<>();
        for (DataRoomEntity oneImage : globalListOfEntity) {
            HashMap<String,Object> oneItem = new HashMap<>();
            // put description in item
            oneItem.put("description",oneImage.getDescription());
            // put thumbnail in item
            long imageId = oneImage.getImageId();
            Bitmap thumbnailBitmap = MediaStore.Images.Thumbnails.getThumbnail(
                    getContentResolver(),imageId,
                    MediaStore.Images.Thumbnails.MICRO_KIND,null);
            oneItem.put("thumbnail", thumbnailBitmap);
            listItems.add(oneItem);
        }
        return(listItems);
    }

    @Override
    public boolean setViewValue(View view, Object data, String asText) {
        switch(view.getId()) {
            // set thumbnail
            case R.id.item_image:
                ((ImageView)view).setImageBitmap((Bitmap)data);
                break;
            // set description
            case R.id.item_text:
                if (data != null) {
                    ((TextView)view).setText((String)data);
                }
                else {
                    ((TextView)view).setText(getResources().getString(R.string.no_known_description_text));
                }
                break;
        }
        return(true);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // get image URI
        globalMediaStoreCursor.moveToPosition(position);
        int dataIndex = globalMediaStoreCursor.getColumnIndex(MediaStore.Images.Media.DATA);
        Uri fullImageURI = Uri.parse(globalMediaStoreCursor.getString(dataIndex));

        // create and show dialog
        ImageDialogFragment thePhotoDialogFragment = new ImageDialogFragment();
        Bundle bundleToFragment = new Bundle();
        bundleToFragment.putString("image_uri_as_string", fullImageURI.toString());
        thePhotoDialogFragment.setArguments(bundleToFragment);
        thePhotoDialogFragment.show(getFragmentManager(),"my_fragment");

        // pause song
        myPlayer.pause();
        myPlayerIsPaused = true;

        // speak description
        String whatToSay = ((TextView)view.findViewById(R.id.item_text)).getText().toString();
        if (whatToSay.length() > 0) {
            mySpeaker.speak(whatToSay,TextToSpeech.QUEUE_ADD,null, null);
        } else {
            Toast.makeText(this,"Nothing to say",Toast.LENGTH_SHORT).show();
        }
    }

    public void stopTalking() {
        // stop TTS
        mySpeaker.speak("", TextToSpeech.QUEUE_FLUSH, null, null);
        // resume song
        myPlayer.start();
        myPlayerIsPaused = false;
    }

    public void onInit(int status) {
        // initialize TTS
        if (status == TextToSpeech.SUCCESS) {
            Toast.makeText(this,R.string.talk_prompt,Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this,R.string.install_required, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // play the music when app is resumed
        if (myPlayerIsPaused) {
            myPlayer.start();
            myPlayerIsPaused = false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // pause the music when app is paused
        if (myPlayer.isPlaying()) {
            myPlayer.pause();
            myPlayerIsPaused = true;
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        // start a new random song once song is over
        startRandomSong();
    }
}
