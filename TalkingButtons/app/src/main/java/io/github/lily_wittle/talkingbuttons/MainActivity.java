package io.github.lily_wittle.talkingbuttons;

import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private TextToSpeech mySpeaker;
    private int numberUtterances;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mySpeaker = new TextToSpeech(this,this);
        mySpeaker.setOnUtteranceProgressListener(myListener);

        Log.i("IN onCreate", "Created OK");
    }

    public void onInit(int status) {
        numberUtterances = 0;
        myHandler.postDelayed(myUtteranceCompleted, 5000);
    }

    public void myClickListener(View view) {
        myHandler.removeCallbacks(myUtteranceCompleted);
        String whatToSay = ((Button)view).getText().toString();
        numberUtterances++;
        mySpeaker.speak(whatToSay,TextToSpeech.QUEUE_ADD,null, "USER_SPEAK");
        Log.i("IN myClickListener", "Clicked " + whatToSay);
    }

    private UtteranceProgressListener myListener =
            new UtteranceProgressListener() {
                @Override
                public void onStart(String utteranceId) {
                }

                @Override
                public void onDone(String utteranceId) {
                    if (utteranceId.equals("USER_SPEAK")) {
                        numberUtterances--;
                        if (numberUtterances == 0) {
                            myHandler.postDelayed(myUtteranceCompleted, 5000);
                            Log.i("IN onDone", "All speaking done, starting post delay");
                        }
                    }
                }

                @Override
                public void onError(String utteranceId) {
                }
            };

    private Handler myHandler = new Handler();

    private final Runnable myUtteranceCompleted = new Runnable() {

        public void run() {
            mySpeaker.speak("I have nothing to say",TextToSpeech.QUEUE_FLUSH,
                    null, "NOTHING");
            myHandler.postDelayed(myUtteranceCompleted, 5000);
            Log.i("IN run", "I have nothing to say");
        }
    };

}
