package io.github.lily_wittle.talkingpicturelist;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ImageDialogFragment extends android.app.DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // get image to display
        String uri_as_string = this.getArguments().getString("image_uri_as_string");
        // inflate the dialog view
        View dialogView = inflater.inflate(R.layout.dialog, container);
        // set click listener for dismiss button
        (dialogView.findViewById(R.id.dismiss)).setOnClickListener(myClickHandler);
        // set the image
        ImageView theImage = dialogView.findViewById(R.id.the_image);
        theImage.setImageURI(Uri.parse(uri_as_string));

        return(dialogView);
    }

    private View.OnClickListener myClickHandler = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()) {
                // dismiss dialog if dismiss button is clicked
                case R.id.dismiss:
                    dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    public interface StopTalking {
        void stopTalking();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        // stop talking in activity
        ((StopTalking)getActivity()).stopTalking();
    }

}
