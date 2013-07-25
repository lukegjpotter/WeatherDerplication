package com.github.lukegjpotter.app.derpyweather;

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

import android.os.AsyncTask;

public class ReadForecastTask extends AsyncTask<Object, Object, String> {


    @Override
    protected String doInBackground(Object... objects) {
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
