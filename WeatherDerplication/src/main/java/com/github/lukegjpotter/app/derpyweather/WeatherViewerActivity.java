package com.github.lukegjpotter.app.derpyweather;

/**
 * WeatherViewerActivity.java
 *
 * @author Luke GJ Potter - lukegjpotter
 * @date 23/Jun/13
 * @version 1.0
 *
 * Description:
 *     Main activity for the Weather Derplication app.
 *     This activity uses AddCityDialogFragment to add new
 *     cities. It also has once instance of the class
 *     CitiesFragment. This activity is responsible for
 *     swapping in and out the various ForecastFragments
 *     that are displayed on the screen.
 *     This class also has the ActionBar code.
 */

import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.view.Menu;

import java.util.Map;

public class WeatherViewerActivity extends Activity implements DialogFinishedListener {

    public  static final String WIDGET_UPDATE_BROADCAST_ACTION = "com.github.lukegjpotter.app.derpyweather.UPDATE_WIDGET";
    private static final int    BROADCAST_DELAY                = 10000;
    private static final int    CURRENT_CONDITIONS_TAB         = 0;
    public  static final String PREFERRED_CITY_NAME_KEY        = "preferred_city_name";
    public  static final String PREFERRED_CITY_ZIPCODE_KEY     = "preferred_city_zipcode";
    public  static final String SHARED_PREFERENCES_NAME        = "weather_viewer_shared_preferences";
    private static final String CURRENT_TAB_KEY                = "current_tab";
    private static final String LAST_SELECTED_KEY              = "last_selected";

    private int currentTab;          // Position in the current tab.
    private String lastSelectedCity; // Last city selected from the list.
    private SharedPreferences weatherSharedPreferences;

    // Stores city names and the corresponding zipcodes.
    private Map <String, String> favouriteCitiesMap;
    private CityListFragment cityListFragment;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_viewer);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.weather_viewer, menu);
        return true;
    }
    
}
