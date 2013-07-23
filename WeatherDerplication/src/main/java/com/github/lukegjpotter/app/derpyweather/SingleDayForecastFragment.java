package com.github.lukegjpotter.app.derpyweather;

/**
 * @author Luke GJ Potter - lukegjpotter
 * @version 1.0
 *
 * Date: 23/07/2013.
 *
 * Description:
 *     Displays forecast information for a single city for a single day.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SingleDayForecastFragment extends ForecastFragment {

    private String zipCode; // ZIP code for this forecast.

    // Look up keys for the Fragment's saved state.
    private static final String LOCATION_KEY      = "location";
    private static final String TEMPERATURE_KEY   = "temperature";
    private static final String FEELS_LIKE_KEY    = "feels_like";
    private static final String HUMIDITY_KEY      = "humidity";
    private static final String PRECIPITATION_KEY = "change_precipitation";
    private static final String IMAGE_KEY         = "image";
    private static final String ZIPCODE_KEY       = "id_key";

    // Views
    private View      forecastView;
    private TextView  temperatureTextView;
    private TextView  feelsLikeTextView;
    private TextView  humidityTextView;
    private TextView  locationTextView;
    private TextView  chanceOfPrecipitationTextView;
    private ImageView conditionImageView;
    private TextView  loadingTextView;
    private Context   context;
    private Bitmap    conditionsBitmap;

    /**
     * Create a new ForecastFragment for the given ZIP code.
     *
     * @param zipcode
     * @return
     */
    public static SingleDayForecastFragment newInstance(String zipcode) {

        // Create a new ForecastFragment.
        SingleDayForecastFragment newForecastFragment = new SingleDayForecastFragment();

        Bundle argsBundle = new Bundle();

        // Save the given String in the Bundle.
        argsBundle.putString(ZIPCODE_KEY, zipcode);

        // Set the Fragment's arguments.
        newForecastFragment.setArguments(argsBundle);
        return newForecastFragment;
    }

    /**
     * Create a new ForecastFragment for the given ZIP code.
     *
     * @param argsBundle
     * @return
     */
    public static SingleDayForecastFragment newInstance(Bundle argsBundle) {

        String zipCode = argsBundle.getString(ZIPCODE_KEY);
        return newInstance(zipCode);
    }

    /**
     * Create the Fragment from the saved state Bundle.
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setZipcode(getArguments().getString(ZIPCODE_KEY));
    }

    /**
     * Save the Fragment's state.
     *
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        // Store the View's contents into the Bundle.
        outState.putString(LOCATION_KEY,      locationTextView.getText().toString());
        outState.putString(TEMPERATURE_KEY,   temperatureTextView.getText().toString());
        outState.putString(FEELS_LIKE_KEY,    feelsLikeTextView.getText().toString());
        outState.putString(HUMIDITY_KEY,      humidityTextView.getText().toString());
        outState.putString(PRECIPITATION_KEY, chanceOfPrecipitationTextView.getText().toString());
        outState.putParcelable(IMAGE_KEY,     conditionsBitmap);
    }

    /**
     * Public access for ZIP code of this Fragment's forecast information.
     *
     * @return
     */
    @Override
    public String getZipcode() {

        return zipCode;
    }

    /**
     * Private access to set ZIP code of this Fragment's forecast information.
     *
     * @param zc
     */
    private void setZipcode(String zc) {

        this.zipCode = zc;
    }
}
