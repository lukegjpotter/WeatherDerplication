package com.github.lukegjpotter.app.derpyweather;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;

/**
 * ReadLocationTask.java
 *
 * @author Luke GJ Potter - lukegjpotter
 * @date 25/Jul/13
 * @version 1.0
 *
 * Description:
 *     This class reads location information in a background thread.
 */
class ReadLocationTask extends AsyncTask<Object, Object, String> {

    private static final String TAG = "ReadLocationTask.java";

    private String zipCode;      // The ZIP code for the location.
    private Context context;     // Launching Activity's Context.
    private Resources resources; // Used to look up String from XML.

    // Strings of each type of data retrieved.
    private String city;
    private String state;
    private String country;

    // Listener for retrieved information.
    private LocationLoadedListener weatherLocationLoadedListener;



    @Override
    protected String doInBackground(Object... objects) {
        return null;
    }
}
