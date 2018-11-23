package io.github.lily_wittle.talkingpicturelist;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Intent;
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

public class MainActivity extends AppCompatActivity implements
        TextToSpeech.OnInitListener,
        SimpleAdapter.ViewBinder,
        AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener,
        MediaPlayer.OnCompletionListener,
        ImageDialogFragment.StopTalking {

    private static final int ACTIVITY_EDIT = 1;
    private Cursor globalMediaStoreCursor;
    private MediaPlayer myPlayer;
    private boolean myPlayerIsPaused;
    private static final String DATABASE_NAME = "ImageAndDescription.db";
    private DataRoomDB imageAndDescriptionDB;
    private List<DataRoomEntity> globalListOfEntity;
    private TextToSpeech mySpeaker;
    private int globalIJustEdited;

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

        // make a tts
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
        // shut down tts
        mySpeaker.shutdown();
    }

    private void startRandomSong() {
        // start playing a random song from the media store

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
        // update database from media store images

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
                // get image id
                long imageId = globalMediaStoreCursor.getLong(
                        globalMediaStoreCursor.getColumnIndex(MediaStore.Images.Media._ID));

                // if not in database, add new entry to database
                if (imageAndDescriptionDB.daoAccess().getEntryByImageId(imageId) == null) {
                    DataRoomEntity imageData = new DataRoomEntity();
                    imageData.setImageId(imageId);
                    imageAndDescriptionDB.daoAccess().addEntry(imageData);
                }
            } while (globalMediaStoreCursor.moveToNext());
        }
        else {
            Log.i("IN onCreate image", "No images fetched");
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
        theList.setOnItemLongClickListener(this);
        theList.setAdapter(listAdapter);
    }

    private ArrayList<HashMap<String,Object>> fetchAllTalkingPictures() {
        // refresh globalListOfEntity
        globalListOfEntity = imageAndDescriptionDB.daoAccess().fetchAll();
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
            if (thumbnailBitmap!= null) {
                oneItem.put("thumbnail", thumbnailBitmap);
                listItems.add(oneItem);
            }
            else {
                // null thumbnail, need to delete from db
                imageAndDescriptionDB.daoAccess().deleteEntry(oneImage);
                globalListOfEntity.remove(oneImage);
            }
        }
        return(listItems);
    }

    @Override
    public boolean setViewValue(View view, Object data, String asText) {
        // for use in list view adapter
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
                    // if description not set yet, use default
                    ((TextView)view).setText(getResources().getString(R.string.no_known_description_text));
                }
                break;
        }
        return(true);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // when list view item is clicked, speak description and show image dialog

        // get db entry
        DataRoomEntity dbEntry = globalListOfEntity.get(position);

        // get image uri for image in db entry
        Uri fullImageURI = getImageURI(dbEntry);
        if(fullImageURI != null) {

            // create and show dialog
            ImageDialogFragment thePhotoDialogFragment = new ImageDialogFragment();
            Bundle bundleToFragment = new Bundle();
            bundleToFragment.putString("image_uri_as_string", fullImageURI.toString());
            thePhotoDialogFragment.setArguments(bundleToFragment);
            thePhotoDialogFragment.show(getFragmentManager(), "my_fragment");

            // pause song
            pauseMusic();

            // speak description
            String whatToSay = ((TextView) view.findViewById(R.id.item_text)).getText().toString();
            if (whatToSay.length() > 0) {
                mySpeaker.speak(whatToSay, TextToSpeech.QUEUE_ADD, null, null);
            } else {
                Toast.makeText(this, "Nothing to say", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        // when list view item is long clicked, open edit activity for the image

        // get db entry and save id in globalIJustEdited
        DataRoomEntity dbEntry = globalListOfEntity.get(position);
        globalIJustEdited = dbEntry.getId();

        // get image uri for image in db entry
        Uri fullImageURI = getImageURI(dbEntry);
        if(fullImageURI != null) {

            // pause song
            pauseMusic();

            // start edit activity
            Intent editIntent = new Intent();
            editIntent.setClassName("io.github.lily_wittle.talkingpicturelist",
                    "io.github.lily_wittle.talkingpicturelist.EditActivity");
            editIntent.putExtra("image_uri_as_string", fullImageURI.toString());
            String description = ((TextView) view.findViewById(R.id.item_text)).getText().toString();
            editIntent.putExtra("image_description", description);
            startActivityForResult(editIntent, ACTIVITY_EDIT);
        }

        return true;
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        // resume song
        resumeMusic();

        switch(requestCode) {
            // on edit activity result
            case ACTIVITY_EDIT:
                if (resultCode == Activity.RESULT_OK) {
                    // get new data from edit activity
                    String new_description = data.getStringExtra("new_description");
                    // update database
                    DataRoomEntity theEntry = imageAndDescriptionDB.daoAccess().getEntryById(globalIJustEdited);
                    DataRoomEntity newEntry = new DataRoomEntity();
                    newEntry.setId(globalIJustEdited);
                    newEntry.setImageId(theEntry.getImageId());
                    newEntry.setDescription(new_description);
                    imageAndDescriptionDB.daoAccess().updateEntry(newEntry);
                    // update list view
                    fillListView();
                }
                break;
        }

    }

    public void stopTalking() {
        // stop tts
        mySpeaker.speak("", TextToSpeech.QUEUE_FLUSH, null, null);
        // resume song
        resumeMusic();
    }

    public void onInit(int status) {
        // initialize tts
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
        resumeMusic();
        // update list view
        updateDBFromMediaStore();
        fillListView();
    }

    @Override
    public void onPause() {
        super.onPause();
        // pause the music when app is paused
        pauseMusic();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        // start a new random song once song is over
        startRandomSong();
    }

    private void resumeMusic() {
        // if player is paused, start music
        if (myPlayerIsPaused) {
            myPlayer.start();
            myPlayerIsPaused = false;
        }
    }

    private void pauseMusic() {
        // if player is playing, pause music
        if (myPlayer.isPlaying()) {
            myPlayer.pause();
        }
        myPlayerIsPaused = true;
    }

    private Uri getImageURI(DataRoomEntity dbEntry) {
        // return image uri for the image corresponding to db entry
        long desiredImageId = dbEntry.getImageId();
        boolean imageFound;
        if (globalMediaStoreCursor != null & globalMediaStoreCursor.getCount() > 0) {
            // we will move cursor until image found or there are no more images left in media store
            globalMediaStoreCursor.moveToFirst();
            int idIndex = globalMediaStoreCursor.getColumnIndex(MediaStore.Images.Media._ID);
            do {
                // loop through media store until image found with matching image id
                imageFound = (desiredImageId == globalMediaStoreCursor.getLong(idIndex));
            } while (!imageFound && globalMediaStoreCursor.moveToNext());

            // if the image is found, return its uri
            if(imageFound) {
                int dataIndex = globalMediaStoreCursor.getColumnIndex(MediaStore.Images.Media.DATA);
                Log.i("IN getImageURIAsString", "MATCH FOUND");
                return Uri.parse(globalMediaStoreCursor.getString(dataIndex));
            }
            // no more images in media store and no match has been found
            else {
                Log.i("IN getImageURIAsString", "No match in media store for " + desiredImageId);
            }
        }
        else {
            Log.i("IN getImageURIAsString", "No images found from globalMediaStoreCursor");
        }
        // delete image from database since it has no match in the media store
        imageAndDescriptionDB.daoAccess().deleteEntry(dbEntry);
        fillListView();
        return null;
    }

}
