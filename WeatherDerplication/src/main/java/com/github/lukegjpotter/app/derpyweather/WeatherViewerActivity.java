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

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.HashMap;
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
    private Map<String, String> favouriteCitiesMap;
    private CityListFragment cityListFragment;
    private Handler weatherHandler;

    /**
     * This method sets up various variables.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_viewer);

        // Get the CityListFragment
        cityListFragment = (CityListFragment) getFragmentManager().findFragmentById(R.id.cities);

        // Set the CitiesListChangeListener
        cityListFragment.setCitiesListChangedListener(citiesListChangeListener);

        // Create HashMap storing city names and corresponding ZIP codes.
        favouriteCitiesMap = new HashMap<String, String>();

        weatherHandler = new Handler();

        weatherSharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);

        setupTabs();
    }

    /**
     * Save this Activity's state.
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putInt(CURRENT_TAB_KEY, currentTab);
        outState.putString(LAST_SELECTED_KEY, lastSelectedCity);

        super.onSaveInstanceState(outState);
    }

    /**
     * Restores this Activity's saved state.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);

        currentTab = savedInstanceState.getInt(CURRENT_TAB_KEY);
        lastSelectedCity = savedInstanceState.getString(LAST_SELECTED_KEY);
    }

    /**
     * Called when this Activity resumes.
     */
    @Override
    protected void onResume() {

        super.onResume();

        if (favouriteCitiesMap.isEmpty()) {

            loadSavedCities();
        }

        if (favouriteCitiesMap.isEmpty()) {

            addSampleCities();
        }

        getActionBar().selectTab(getActionBar().getTabAt(currentTab));
        loadSelectedForecast();
    }

    /**
     * Listens for changes to the CityListFragment.
     */
    private CitiesListChangeListener citiesListChangeListener = new CitiesListChangeListener() {

        /**
         * Called when the selected city is changed.
         *
         * @param cityName
         */
        @Override
        public void onSelectedCityChanged(String cityName) {

            // Show the given city's forecast.
            selectForecast(cityName);
        }

        /**
         * Called when the preferred city is changed.
         *
         * @param cityName
         */
        @Override
        public void onPreferredCityChanged(String cityName) {

            // Update the preferences with the new preferred city.
            setPreferred(cityName);
        }
    };

    /**
     * Load the previously selected forecast.
     */
    private void loadSelectedForecast() {

        if (lastSelectedCity != null) {

            selectForecast(lastSelectedCity);
        } else {

            String cityName = weatherSharedPreferences.getString(PREFERRED_CITY_NAME_KEY, getResources().getString(R.string.default_zipcode));
            selectForecast(cityName);
        }
    }

    /**
     * Set the preferred city.
     *
     * @param cityName
     */
    public void setPreferred(String cityName) {

        String cityZipcode = favouriteCitiesMap.get(cityName);

        Editor preferredCityEditor = weatherSharedPreferences.edit();
        preferredCityEditor.putString(PREFERRED_CITY_NAME_KEY, cityName);
        preferredCityEditor.putString(PREFERRED_CITY_ZIPCODE_KEY, cityZipcode);
        preferredCityEditor.apply();

        lastSelectedCity = null;
        loadSelectedForecast();

        // Update the widget to show the new preferred city's weather.
        final Intent updateWidgetIntent = new Intent(WIDGET_UPDATE_BROADCAST_ACTION);

        weatherHandler.postDelayed(new Runnable() {

            @Override
            public void run() {

                sendBroadcast(updateWidgetIntent);
            }
        }, BROADCAST_DELAY);
    }

    /**
     * Reads the previously saved cities from SharedPreferences.
     */
    private void loadSavedCities() {

        Map<String, ?> citiesMap = weatherSharedPreferences.getAll();

        for (String city : citiesMap) {

            if (!(city.equals(PREFERRED_CITY_NAME_KEY) || city.equals(PREFERRED_CITY_ZIPCODE_KEY))) {

                addCity(city, (String) citiesMap.get(city), false);
            }
        }
    }

    /**
     * Add the sample cities.
     */
    private void addSampleCities() {

        // Load the array of city names from resources.
        String[] sampleCityNamesArray = getResources().getStringArray(R.array.default_city_names);

        // Load the array of ZIP codes from resources.
        String[] sampleZipCodesArray = getResources().getStringArray(R.array.default_city_zipcodes);

        for (int i = 0; i < sampleCityNamesArray.length; i++) {

            if (i == 0) setPreferred(sampleCityNamesArray[i]);

            addCity(sampleCityNamesArray[i], sampleZipCodesArray[i], false);
        }
    }

    /**
     * Add a city to the CityListFragment ListFragment and
     * record it in the SharedPreferences.
     *
     * @param city
     * @param zipCode
     * @param select
     */
    public void addCity(String city, String zipCode, boolean select) {

        favouriteCitiesMap.put(city, zipCode);
        cityListFragment.addCity(city, select);

        Editor prefsEditor = weatherSharedPreferences.edit();
        prefsEditor.putString(city, zipCode);
        prefsEditor.apply();
    }

    /**
     * Display forecast information for the given city.
     *
     * @param city
     */
    public void selectForecast(String city) {

        lastSelectedCity = city; // Save the city name.
        String zipCode = favouriteCitiesMap.get(city);

        if (zipCode == null) {
            return;
        }

        ForecastFragment currentForecastFragment = (ForecastFragment) getFragmentManager().findFragmentById(R.id.forecast_replacer);

        if (currentForecastFragment == null || !(currentForecastFragment.getZipcode().equals(zipCode) && correctTab(currentForecastFragment))) {

            // If the selected tab is "Current Conditions".
            if (currentTab == CURRENT_CONDITIONS_TAB) {

                // Create a new ForecastFragment using the given ZIP code.
                currentForecastFragment = SingleDayForecastFragment.newInstance(zipCode);
            } else {

                // Create a new ForecastFragment using the given ZIP code.
                currentForecastFragment = FiveDayForecastFragment.newInstance(zipCode);
            }

            // Create a new FragmentTransaction.
            FragmentTransaction forecastFragmentTransaction = getFragmentManager().beginTransaction();

            // Set transition animation to fade.
            forecastFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

            // Replace the fragment (or View) at the given ID with our new Fragment.
            forecastFragmentTransaction.replace(R.id.forecast_replacer, currentForecastFragment);

            // Begin the transaction.
            forecastFragmentTransaction.commit();
        }
    }

    /**
     * Is this the proper ForecastFragment for the currently selected tab.
     *
     * @param forecastFragment
     * @return
     */
    private boolean correctTab(ForecastFragment forecastFragment) {

        // If the "Current Conditions" tab is selected.
        if (currentTab == CURRENT_CONDITIONS_TAB) {

            // Return true if the given ForecastFragment is a Single Day forecast.
            return (forecastFragment instanceof SingleDayForecastFragment);
        } else {

            // Return false if the given ForecastFragment is a Five Day Forecast.
            return (forecastFragment instanceof FiveDayForecastFragment);
        }
    }

    /**
     * Select the tab at the given position.
     *
     * @param position
     */
    private void selectTab(int position) {

        currentTab = position; // Save the position tab.
        loadSelectedForecast();
    }

    /**
     * The method that creates the options menu in the action bar.
     *
     * @param menu
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.weather_viewer, menu);
        return true;
    }

    /**
     * When one of the items was clicked.
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // If the item selected was the "Add City" item.
        if (item.getItemId() == R.id.add_city_item) {

            showAddCityDialog();
            return true;
        }

        return false;
    }

    /**
     * Display FragmentDialog allowing the user to add a new city.
     */
    private void showAddCityDialog() {

        // Create a new AddCityDialogFragment.
        AddCityDialogFragment newAddCityDialogFragment = new AddCityDialogFragment();

        // Get instance of the FragmentManager.
        FragmentManager fragmentManager = getFragmentManager();

        // Begin a FragmentTransaction.
        FragmentTransaction addCityFragmentTransaction = fragmentManager.beginTransaction();

        // Shew the DialogFragment.
        newAddCityDialogFragment.show(addCityFragmentTransaction, "");
    }

    /**
     * Called when the FragmentDialog is dismissed.
     *
     * @param zipCode
     * @param preferred
     */
    @Override
    public void onDialogFinished(String zipCode, boolean preferred) {

        // Convert ZIP code to city.
        getCityNameFromZipcode(zipCode, preferred);
    }

    /**
     * Read city name from ZIP code.
     *
     * @param zipCode
     * @param preferred
     */
    private void getCityNameFromZipcode(String zipCode, boolean preferred) {

        // If this ZIP code is already added.
        if (favouriteCitiesMap.containsValue(zipCode)) {

            // Create a Toast displaying error information.
            Toast errorToast = Toast.makeText(
                    WeatherViewerActivity.this,
                    WeatherViewerActivity.this.getResources().getString(R.string.duplicate_zipcode_error),
                    Toast.LENGTH_LONG);

            errorToast.setGravity(Gravity.CENTER, 0, 0);
            errorToast.show();

        } else {

            // Load the location information in a background thread.
            new ReadLocationTask(zipCode, this, new CityNameLocationLoadedListener(zipCode, preferred)).execute();
        }
    }

    /**
     * Listens for city information loaded in background task.
     */
    private class CityNameLocationLoadedListener implements LocationLoadedListener {

        private String  zipCode;
        private boolean preferred;

        public CityNameLocationLoadedListener(String zipCodeString, boolean isPreferred) {

            zipCode = zipCodeString;
            preferred = isPreferred;
        }

        @Override
        public void onLocationLoaded(String city, String state, String country) {

            // If a city was found to match the given ZIP code.
            if (city != null) {

                addCity(city, state, !preferred);

                if (preferred) {

                    setPreferred(city);
                }
            } else {

                // Display text saying that the location could not be found.
                Toast zipCodeToast = Toast.makeText(
                        WeatherViewerActivity.this,
                        WeatherViewerActivity.this.getResources().getString(R.string.invalid_zipcode_error),
                        Toast.LENGTH_LONG);

                zipCodeToast.setGravity(Gravity.CENTER, 0, 0);
                zipCodeToast.show();
            }
        }
    }

    /**
     * Set up the ActionBar's tabs
     */
    private void setupTabs() {

        // Get the Action Bar
        ActionBar weatherActionBar = getActionBar();

        // Set Action Bar's navigation mode to use tabs.
        weatherActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the "Current Conditions" tab.
        ActionBar.Tab currentConditionsTab = weatherActionBar.newTab();

        // Set the Tab's title.
        currentConditionsTab.setText(getResources().getString(R.string.current_conditions));

        // Set the Tab's Listener.
        currentConditionsTab.setTabListener(weatherTabListener);
        weatherActionBar.addTab(currentConditionsTab);

        // Create the "Five Day Forecast" tab.
        ActionBar.Tab fiveDayForecastTab = weatherActionBar.newTab();
        fiveDayForecastTab.setText(getResources().getString(R.string.five_day_forecast));
        fiveDayForecastTab.setTabListener(weatherTabListener);
        weatherActionBar.addTab(fiveDayForecastTab);

        // Select "Current Conditions" Tab by default.
        currentTab = CURRENT_CONDITIONS_TAB;
    }

    /**
     * Listen for events generated by the ActionBar Tabs.
     */
    ActionBar.TabListener weatherTabListener = new ActionBar.TabListener() {

        /**
         * Called when the selected tab is selected for the first time.
         *
         * @param tab
         * @param fragmentTransaction
         */
        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

            selectTab(tab.getPosition());
        }

        /**
         * Called when a tab is unselected.
         *
         * @param tab
         * @param fragmentTransaction
         */
        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        }

        /**
         * Called when the selected tab is re-accessed.
         *
         * @param tab
         * @param fragmentTransaction
         */
        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        }
    };
}
