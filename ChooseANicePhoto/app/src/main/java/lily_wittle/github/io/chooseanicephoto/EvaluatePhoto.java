package lily_wittle.github.io.chooseanicephoto;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.content.Intent;
import android.util.Log;
import android.net.Uri;
import android.widget.ImageView;

public class EvaluatePhoto extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate_photo);
        Log.i("IN onCreate evaluate", "Created OK");

        ImageView pictureView;
        Uri chosenURI;
        Bitmap chosenPicture;
        Intent data = getIntent();
        chosenURI = data.getParcelableExtra("lily_wittle.github.io.chooseanicephoto.imageUri");
        pictureView = findViewById(R.id.chosen_picture);

        try {
            chosenPicture = MediaStore.Images.Media.getBitmap(this.getContentResolver(),chosenURI);
            pictureView.setImageBitmap(chosenPicture);
            Log.i("IN onResult Choose", "Displayed picture");

        } catch(Exception e) {
            Log.i("ERROR", "Could not get picture from " + chosenURI + " " + e.getMessage());
        }
    }

    public void myClickHandler(View view) {
        Intent returnIntent;
        switch (view.getId()) {
            case R.id.like:
                returnIntent = new Intent();
                returnIntent.putExtra(
                        "lily_wittle.github.io.chooseanicephoto.reaction",
                        Boolean.TRUE);
                setResult(RESULT_OK,returnIntent);
                Log.i("IN myClickHandler eval", "You clicked like");
                finish();
                break;
            case R.id.dislike:
                returnIntent = new Intent();
                returnIntent.putExtra(
                        "lily_wittle.github.io.chooseanicephoto.reaction",
                        Boolean.FALSE);
                setResult(RESULT_OK,returnIntent);
                Log.i("IN myClickHandler eval", "You clicked dislike");
                finish();
                break;
            default:
                break;
        }
    }
}
