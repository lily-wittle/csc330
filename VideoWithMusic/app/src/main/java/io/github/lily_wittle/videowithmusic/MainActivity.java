package io.github.lily_wittle.videowithmusic;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.VideoView;
import android.util.Log;


public class MainActivity extends AppCompatActivity
        implements MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener{

    private MediaPlayer musicPlayer = null;
    private VideoView videoScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize music
        musicPlayer = MediaPlayer.create(this, R.raw.supercut);
        musicPlayer.start();
        musicPlayer.setLooping(true);

        // initialize video
        videoScreen = findViewById(R.id.video_screen);
        videoScreen.setOnPreparedListener(this);
        videoScreen.setOnCompletionListener(this);

        Log.i("IN onCreate", "Created OK");
    }

    public void myClickListener(View view) {
        switch(view.getId()) {

            case R.id.video_play:
                // pause music player
                musicPlayer.pause();

                // play video from beginning
                videoScreen.setVideoURI(Uri.parse(getString(R.string.mp4_url)));
                videoScreen.setVisibility(View.VISIBLE);
                videoScreen.start();

                Log.i("IN myClickListener", "Played video, paused music");
                break;

            case R.id.video_pause:
                // pause video
                videoScreen.pause();

                // resume music player
                if (musicPlayer != null && !musicPlayer.isPlaying()) {
                    musicPlayer.start();
                }

                Log.i("IN myClickListener", "Paused video, resumed music");
                break;

            case R.id.video_resume:
                // pause music player
                musicPlayer.pause();

                // resume video
                videoScreen.start();

                Log.i("IN myClickListener", "Resumed video, paused music");
                break;

            case R.id.video_stop:
                // exit app
                videoScreen.stopPlayback();
                musicPlayer.stop();
                musicPlayer.release();
                Log.i("IN myClickListener", "Stopped video and exited");
                finish();
        }
    }

    public void onPrepared(MediaPlayer mediaPlayer) {

        mediaPlayer.start();
    }

    public void onCompletion(MediaPlayer mediaPlayer) {

        finish();
    }

    public boolean onError(MediaPlayer mediaPlayer,int whatHappened,int extra) {

        mediaPlayer.stop();
        mediaPlayer.release();
        musicPlayer = null;
        return(true);
    }

}
