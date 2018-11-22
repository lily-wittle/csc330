package io.github.lily_wittle.talkingpicturelist;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class EditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // set content view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // get image URI and description from intent and put in UI
        String uri_as_string = this.getIntent().getStringExtra("image_uri_as_string");
        ImageView theImage = findViewById(R.id.edit_image);
        theImage.setImageURI(Uri.parse(uri_as_string));
        String old_description = this.getIntent().getStringExtra("image_description");
        EditText theText = findViewById(R.id.edit_text);
        theText.setText(old_description);
    }

    public void myClickHandler(View view) {
        switch (view.getId()) {
            case R.id.save:
                // return intent to main activity
                Intent returnIntent = new Intent();
                // return new description for image
                String description = ((EditText)findViewById(R.id.edit_text)).getText().toString();
                returnIntent.putExtra("new_description", description);
                setResult(RESULT_OK, returnIntent);
                finish();
                break;
        }
    }
}
