package lily_wittle.github.io.chooseanicephoto;

import android.app.Activity;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.content.Intent;
import android.widget.ImageView;
import android.net.Uri;
import android.graphics.Bitmap;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity {

    private static final int ACTIVITY_CHOOSE_PICTURE = 1;
    private static final int ACTIVITY_RATE_PICTURE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("IN onCreate", "Created OK");
        goChoose();
    }

    public void goChoose() {
        Intent galleryIntent;
        galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent,ACTIVITY_CHOOSE_PICTURE);
        Log.i("IN goChoose", "Started gallery");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri chosenURI;
        Boolean likedPicture;
        Intent rateActivity;

        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case ACTIVITY_CHOOSE_PICTURE:
                if(resultCode == Activity.RESULT_OK) {
                    rateActivity = new Intent();
                    rateActivity.setClassName("lily_wittle.github.io.chooseanicephoto", "lily_wittle.github.io.chooseanicephoto.EvaluatePhoto");
                    chosenURI = data.getData();
                    rateActivity.putExtra("lily_wittle.github.io.chooseanicephoto.imageUri", chosenURI);
                    startActivityForResult(rateActivity,ACTIVITY_RATE_PICTURE);
                    Log.i("IN onResult Choose", "Started rate photo activity");
                }
                else {
                    Log.i("IN onResult Choose", "No picture chosen");
                    finish();
                }
                break;

            case ACTIVITY_RATE_PICTURE:
                if(resultCode == Activity.RESULT_OK) {
                    likedPicture = data.getBooleanExtra(
                            "lily_wittle.github.io.chooseanicephoto.reaction",Boolean.FALSE);
                    if(likedPicture == Boolean.TRUE) {
                        Toast.makeText(this, "You liked the photo!", Toast.LENGTH_LONG).show();
                        Log.i("IN onResult Rate", "You liked the photo");
                        finish();
                    }
                    else {
                        goChoose();
                        Log.i("IN onResult Rate", "You did not like the photo");
                    }
                }
                else {
                    goChoose();
                    Log.i("IN onResult Rate", "No evaluation returned");
                }
                break;
            default:
                break;
        }
    }

}


