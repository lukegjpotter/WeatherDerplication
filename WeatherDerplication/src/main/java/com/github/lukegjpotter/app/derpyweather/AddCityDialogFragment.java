package com.github.lukegjpotter.app.derpyweather;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class AddCityDialogFragment extends DialogFragment implements OnClickListener {

    // Listens for results from the AddCityDialog.
    public interface DialogFinishedListener {

        // Called when the AddCityDialog is dismissed.
        void onDialogFinished(String zipCode, boolean preferred);
    }

    EditText addCityEditText; // the DialogFragment's EditText.
    CheckBox addCityCheckBox; // the DialogFragment's CheckBox.

    /**
     * Initializes a new DialogFragment.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        this.setCancelable(true);
    }

    /**
     * Inflates the DialogFragment's layout.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout defined in add_city_dialog.xml.
        View rootView = inflater.inflate(R.layout.add_city_dialog, container, false);

        // Get the EditText.
        addCityEditText = (EditText) rootView.findViewById(R.id.add_city_edit_text);
        // Get the CheckBox.
        addCityCheckBox = (CheckBox) rootView.findViewById(R.id.add_city_checkbox);

        // If the bundle isn't empty.
        if (savedInstanceState != null) {

            addCityEditText.setText(savedInstanceState.getString(getResources().getString(R.string.add_city_dialog_bundle_key)));
        }

        getDialog().setTitle(R.string.add_city_dialog_title);

        // Initialize the positive Button.
        Button okButton = (Button) rootView.findViewById(R.id.add_city_button);
        okButton.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view) {

        // Todo: Implement this.
    }
}
