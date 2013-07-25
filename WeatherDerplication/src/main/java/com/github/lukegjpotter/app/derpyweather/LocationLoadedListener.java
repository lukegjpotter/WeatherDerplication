package com.github.lukegjpotter.app.derpyweather;

/**
 * LocationLoadedListener.java
 *
 * @author Luke GJ Potter - lukegjpotter
 * @date 25/Jul/13
 * @version 1.0
 *
 * Description:
 *     Interface for receiver of location information.
 */
public interface LocationLoadedListener {

    public void onLocationLoaded(String cityName, String stateName, String countryName);
}
