package com.github.lukegjpotter.app.derpyweather;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

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

    /**
     * Public Constructor.
     *
     * @param zc
     * @param c
     * @param listener
     */
    public ReadLocationTask(String zc, Context c, LocationLoadedListener listener) {

        zipCode = zc;
        context = c;
        resources = context.getResources();
        weatherLocationLoadedListener = listener;
    }

    /**
     * Load city name in background thread.
     *
     * @param objects
     * @return
     */
    @Override
    protected String doInBackground(Object... objects) {

        try {

            // Construct the WeatherBug API call URL.
            URL url = new URL(resources.getString(R.string.location_url_pre_zipcode) + zipCode + "&api_key=" + R.string.api_key);

            // Create an InputStreamReader using the URL.
            InputStreamReader forecastReader = new InputStreamReader(url.openStream());

            // Create a JsonReader from InputStreamReader.
            JsonReader forecastJsonReader = new JsonReader(forecastReader);
            forecastJsonReader.beginObject(); // Read the first Object.

            // Get the next name.
            String name = forecastJsonReader.nextName();

            // If the name indicates that the next item describes the ZIP code's location.
            if (name.equals(resources.getString(R.string.location))) {

                // Start reading the next JSON Object.
                forecastJsonReader.beginObject();

                String nextName;

                // While there is more information to be read.
                while (forecastJsonReader.hasNext()) {

                    nextName = forecastJsonReader.nextName();

                    // If the name indicates that the next item describes the ZIP code's corresponding city name.
                    if ((nextName).equals(resources.getString(R.string.city))) {

                        // Read the city name.
                        city = forecastJsonReader.nextString();

                    } else if ((nextName).equals(resources.getString(R.string.state))) {

                        // Read the state name.
                        state = forecastJsonReader.nextString();

                    } else if ((nextName).equals(resources.getString(R.string.country))) {

                        // Read the country name.
                        country = forecastJsonReader.nextString();

                    } else {

                        // Skip the value.
                        forecastJsonReader.skipValue();
                    }
                }

                // Close the JSON Reader.
                forecastJsonReader.close();
            }

        } catch (MalformedURLException e) {

            Log.v(TAG, e.toString());

        } catch (IOException e) {

            Log.v(TAG, e.toString());
        }

        return null;
    }
}
