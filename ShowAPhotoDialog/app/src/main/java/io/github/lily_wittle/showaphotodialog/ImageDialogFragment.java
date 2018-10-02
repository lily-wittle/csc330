package io.github.lily_wittle.showaphotodialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ImageDialogFragment extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // get the name of the animal that was clicked
        int animal_index = this.getArguments().getInt("animal_index");
        String[] animals = getResources().getStringArray(R.array.animals);
        String animal_name = animals[animal_index];

        // set the title of the custom dialog
        getDialog().setTitle(animal_name + " Photo");
        View dialogView = inflater.inflate(R.layout.dialog, container);

        // set click listener for dismiss button
        (dialogView.findViewById(R.id.dismiss)).setOnClickListener(myClickHandler);

        // set the image according to whatever animal was chosen
        ImageView theImage = dialogView.findViewById(R.id.the_image);

        switch(animal_name) {
                    case "Sea Turtle":
                        theImage.setImageResource(R.drawable.turtle);
                        break;
                    case "Whale Shark":
                        theImage.setImageResource(R.drawable.shark);
                        break;
                    case "Dolphin":
                        theImage.setImageResource(R.drawable.dolphin);
                        break;
                    case "Eagle Ray":
                        theImage.setImageResource(R.drawable.ray);
                        break;
                    case "Sea Star":
                        theImage.setImageResource(R.drawable.sea_star);
                        break;
                    default:
                        break;
                }

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
}
