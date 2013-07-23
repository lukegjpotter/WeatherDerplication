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

    @Override
    public String getZipcode() {
        return null;
    }

    public static SingleDayForecastFragment newInstance(String zipCode) {

        return null;
    }
}
