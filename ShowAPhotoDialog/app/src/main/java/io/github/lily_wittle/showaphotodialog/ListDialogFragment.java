package io.github.lily_wittle.showaphotodialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class ListDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        // set title and items in alert dialog
        dialogBuilder.setTitle(R.string.pick_photo);
        dialogBuilder.setItems(R.array.animals,listListener);

        return (dialogBuilder.create());
    }

    private DialogInterface.OnClickListener listListener =
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int indexClicked) {
                    // when one of the items is clicked, show image fragment
                    ImageDialogFragment thePhotoDialogFragment = new ImageDialogFragment();

                    // pass the index of the animal clicked in a bundle
                    Bundle bundleToFragment = new Bundle();
                    bundleToFragment.putInt("animal_index", indexClicked);
                    thePhotoDialogFragment.setArguments(bundleToFragment);
                    thePhotoDialogFragment.show(getFragmentManager(),"my_fragment");

                    // dismiss list dialog
                    dismiss();
                }
            };
}
