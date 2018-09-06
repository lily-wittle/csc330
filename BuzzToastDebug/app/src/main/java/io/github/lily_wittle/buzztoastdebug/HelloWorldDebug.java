package io.github.lily_wittle.buzztoastdebug;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class HelloWorldDebug extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_world_debug);
        Log.i("IN onCreate of HWDebug", "Created OK");
    }
}
