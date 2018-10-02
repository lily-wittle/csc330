package io.github.lily_wittle.showaphotodialog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void myClickListener(View view) {

        switch(view.getId()) {
            case R.id.main_button:
                // open list dialog when button is clicked
                ListDialogFragment theDialogFragment = new ListDialogFragment();
                theDialogFragment.show(getFragmentManager(),"my_fragment");
                break;
            default:
                break;
        }
    }
}
