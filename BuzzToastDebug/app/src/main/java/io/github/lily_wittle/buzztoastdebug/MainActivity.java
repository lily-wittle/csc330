package io.github.lily_wittle.buzztoastdebug;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("IN onCreate", "Created OK");
    }

    public void myClickHandler(View view) {
        Vibrator buzzer;
        final long[] buttonBuzz = {0, 200, 50, 300, 100, 150, 50, 200};
        switch(view.getId()) {
            case R.id.buzz_button:
                Log.i("IN myClickHandler", "Buzz button has been clicked");
                buzzer = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                buzzer.vibrate(buttonBuzz,-1);
                break;
            case R.id.toast_button:
                Log.i("IN myClickHandler", "Toast button has been clicked");
                Toast.makeText(this, "The bee flies anyway!", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        Log.i("IN onCreateOptionsMenu", "Menu has been created");
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_hello_world:
                Log.i("IN onOptionsItemSelectd", "Hello World Debug item has been selected");
                Intent nextActivity;
                nextActivity = new Intent();
                nextActivity.setClassName("io.github.lily_wittle.buzztoastdebug","io.github.lily_wittle.buzztoastdebug.HelloWorldDebug");
                startActivity(nextActivity);
                Log.i("IN onOptionsItemSelectd", "Hello World Debug activity has been started");
                return true;
            default:
                return(super.onOptionsItemSelected(item));
        }
    }

}
