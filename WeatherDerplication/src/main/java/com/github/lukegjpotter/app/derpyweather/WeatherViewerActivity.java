package com.github.lukegjpotter.app.derpyweather;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class WeatherViewerActivity extends Activity {

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
