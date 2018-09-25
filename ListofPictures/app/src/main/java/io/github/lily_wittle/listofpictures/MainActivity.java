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
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements OnItemClickListener {

    private int currentColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView theList;
        theList = findViewById(R.id.the_list);
        theList.setOnItemClickListener(this);

        registerForContextMenu(findViewById(R.id.the_image));
    }
    //-----------------------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater;

        inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);

        return(true);
    }

    //-----------------------------------------------------------------------------
    public void onCreateContextMenu(ContextMenu menu,View view,
                                    ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu,view,menuInfo);
        getMenuInflater().inflate(R.menu.context_menu,menu);
    }

    //-----------------------------------------------------------------------------
    public boolean onContextItemSelected(MenuItem item) {

        LinearLayout theLL;

        theLL = findViewById(R.id.ll_background);

        currentColor = item.getItemId();
        switch (currentColor) {
            case R.id.black:
                theLL.setBackgroundColor(Color.BLACK);
                return(true);
            case R.id.red:
                theLL.setBackgroundColor(Color.RED);
                return(true);
            case R.id.green:
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

        theList = findViewById(R.id.the_list);
        theImage = findViewById(R.id.the_image);
                String pictureName = (String) theList.getItemAtPosition(position);
                switch (pictureName) {
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
