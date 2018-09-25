package io.github.lily_wittle.listofpictures;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.view.ContextMenu;
import android.widget.ImageView;
import android.util.Log;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView theList;
        theList = findViewById(R.id.the_list);
        theList.setOnItemClickListener(this);

        registerForContextMenu(findViewById(R.id.the_image));
        Log.i("IN onCreate", "Created OK");
    }
    //-----------------------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i("IN onCreateOptionsMenu", "Created options menu");
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu,menu);

        return(true);
    }

    //-----------------------------------------------------------------------------
    public void onCreateContextMenu(ContextMenu menu,View view,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        Log.i("IN onCreateContextMenu", "Created context menu");
        super.onCreateContextMenu(menu,view,menuInfo);
        getMenuInflater().inflate(R.menu.context_menu,menu);
    }

    //-----------------------------------------------------------------------------
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        ImageView theImage;
        ListView theList;
        View imageName;

        theImage = findViewById(R.id.the_image);
        theList = findViewById(R.id.the_list);

        switch (item.getItemId()) {
            case R.id.hide:
                Log.i("IN onOptionsItemSelect", "Hid image");
                theImage.setVisibility(View.INVISIBLE);
                return(true);
            case R.id.show:
                Log.i("IN onOptionsItemSelect", "Showed image");
                theImage.setVisibility(View.VISIBLE);
                return(true);
            case R.id.happy:
                imageName = theList.getChildAt(0);
                break;
            case R.id.sad:
                imageName = theList.getChildAt(1);
                break;
            case R.id.crazy:
                imageName = theList.getChildAt(2);
                break;
            case R.id.angry:
                imageName = theList.getChildAt(3);
                break;
            case R.id.crying:
                imageName = theList.getChildAt(4);
                break;
            default:
                return(super.onOptionsItemSelected(item));
        }

        if (item.isChecked()) {
            Log.i("IN onOptionsItemSelect", "Disabled option");
            imageName.setEnabled(false);
            item.setChecked(false);
        }
        else {
            Log.i("IN onOptionsItemSelect", "Enabled option");
            imageName.setEnabled(true);
            item.setChecked(true);
        }
        return(true);
    }

    //-----------------------------------------------------------------------------
    public boolean onContextItemSelected(MenuItem item) {

        LinearLayout theLL;

        theLL = findViewById(R.id.ll_background);

        int currentColor = item.getItemId();
        switch (currentColor) {
            case R.id.black:
                Log.i("IN onContextItemSelect", "Set color to black");
                theLL.setBackgroundColor(Color.BLACK);
                return(true);
            case R.id.red:
                Log.i("IN onContextItemSelect", "Set color to red");
                theLL.setBackgroundColor(Color.RED);
                return(true);
            case R.id.green:
                Log.i("IN onContextItemSelect", "Set color to green");
                theLL.setBackgroundColor(Color.GREEN);
                return(true);
            default:
                return(super.onOptionsItemSelected(item));
        }
    }
//-----------------------------------------------------------------------------
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long rowId) {

        ListView theList;
        ImageView theImage;
        View imageNameView;
        String name;

        theList = findViewById(R.id.the_list);
        theImage = findViewById(R.id.the_image);
        imageNameView = theList.getChildAt(position);
        name = (String) theList.getItemAtPosition(position);

            if (imageNameView.isEnabled()) {
                switch (name) {
                    case "Happy":
                        Log.i("IN onItemClick", "Clicked happy");
                        theImage.setImageResource(R.drawable.happy);
                        break;
                    case "Sad":
                        Log.i("IN onItemClick", "Clicked sad");
                        theImage.setImageResource(R.drawable.sad);
                        break;
                    case "Crazy":
                        Log.i("IN onItemClick", "Clicked crazy");
                        theImage.setImageResource(R.drawable.crazy);
                        break;
                    case "Angry":
                        Log.i("IN onItemClick", "Clicked angry");
                        theImage.setImageResource(R.drawable.angry);
                        break;
                    case "Crying":
                        Log.i("IN onItemClick", "Clicked crying");
                        theImage.setImageResource(R.drawable.crying);
                        break;
                }
            }

    }
}
