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
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
        outState.putParcelable(IMAGE_KEY, conditionsBitmap);
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

    /**
     * Inflates this Fragment's layout from XML.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Use the given LayoutInflater to inflate layout stored in forecast_fragment_layout.xml.
        View rootView = inflater.inflate(R.layout.forecast_fragment_layout, null);

        // Get the TextView in the Fragment's layout hierarchy.
        forecastView                  = rootView.findViewById(R.id.forecast_layout);
        loadingTextView               = (TextView)  rootView.findViewById(R.id.loading_message);
        locationTextView              = (TextView)  rootView.findViewById(R.id.location);
        temperatureTextView           = (TextView)  rootView.findViewById(R.id.temperature);
        feelsLikeTextView             = (TextView)  rootView.findViewById(R.id.feels_like);
        humidityTextView              = (TextView)  rootView.findViewById(R.id.humidity);
        chanceOfPrecipitationTextView = (TextView)  rootView.findViewById(R.id.chance_of_precipitation);
        conditionImageView            = (ImageView) rootView.findViewById(R.id.forecast_image);

        context = rootView.getContext();

        return rootView;
    }

    /**
     * Called when the parent Activity is created.
     *
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        // If there is no save information.
        if (savedInstanceState == null) {

            // Hide the forecast and show the loading message.
            forecastView.setVisibility(View.GONE);
            loadingTextView.setVisibility(View.VISIBLE);

            // Load the location information in a background thread.
            new ReadLocationTask(zipCode, context, new WeatherLocationLoadedListener(zipCode)).execute();

        } else {

            // Display information in the saved state Bundle using the Fragment's Views.
            conditionImageView.setImageBitmap((Bitmap) savedInstanceState.getParcelable(IMAGE_KEY));
            locationTextView.setText(savedInstanceState.getString(LOCATION_KEY));
            temperatureTextView.setText(savedInstanceState.getString(TEMPERATURE_KEY));
            feelsLikeTextView.setText(savedInstanceState.getString(FEELS_LIKE_KEY));
            humidityTextView.setText(savedInstanceState.getString(HUMIDITY_KEY));
            chanceOfPrecipitationTextView.setText(savedInstanceState.getString(PRECIPITATION_KEY));
        }
    }

    /**
     * Receives weather information from AsyncTask.
     */
    ForecastListener weatherForecastListener = new ForecastListener() {

        /**
         * Displays the forecast information.
         *
         * @param image
         * @param temperature
         * @param feelsLike
         * @param humidity
         * @param precipitation
         */
        public void onForecastLoaded(Bitmap image, String temperature, String feelsLike, String humidity, String precipitation) {

            // If this Fragment was detected while the background process ran.
            if (!SingleDayForecastFragment.this.isAdded()) {

                return; // Leave the method.

            } else if (image == null) {

                Toast errorToast = Toast.makeText(context, context.getResources().getString(R.string.null_data_toast), Toast.LENGTH_LONG);
                errorToast.setGravity(Gravity.CENTER, 0, 0);
                errorToast.show();

                return; // Exit before updating the forecast.
            }

            Resources resources = SingleDayForecastFragment.this.getResources();

            // Display the loaded information.
            conditionImageView.setImageBitmap(image);
            conditionsBitmap = image;
            temperatureTextView.setText(temperature + (char) 0x00B0 + resources.getString(R.string.temperature_unit));
            feelsLikeTextView.setText(feelsLike + (char) 0x00B0 + resources.getString(R.string.temperature_unit));
            humidityTextView.setText(humidity + (char) 0x0025);
            chanceOfPrecipitationTextView.setText(precipitation + (char) 0x0025);

            // Hide the loading mask and show teh forecast.
            loadingTextView.setVisibility(View.GONE);
            forecastView.setVisibility(View.VISIBLE);
        }
    };
}
