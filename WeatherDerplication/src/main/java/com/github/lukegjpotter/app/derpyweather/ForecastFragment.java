package com.github.lukegjpotter.app.derpyweather;

/**
 * @author Luke GJ Potter - lukegjpotter
 * @version 1.0
 *
 * Date: 29/06/2013.
 *
 * Description:
 *     An abstract class defining a Fragment capable of providing a ZIP code.
 */
import android.app.Fragment;

public abstract class ForecastFragment extends Fragment {

    public abstract String getZipcode();
}
