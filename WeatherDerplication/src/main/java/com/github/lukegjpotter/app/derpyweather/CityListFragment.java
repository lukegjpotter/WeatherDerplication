package com.github.lukegjpotter.app.derpyweather;

/**
 * CityListFragment.java
 *
 * @author Luke GJ Potter - lukegjpotter
 * @date 07/Jul/13
 * @version 1.0
 *
 * Description:
 *     Fragment displaying list of favourite cities.
 */

import android.app.Activity;
import android.app.ListFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.lukegjpotter.app.derpyweather.dummy.DummyContent;

import java.util.ArrayList;

/**
 * A list fragment representing a list of Cities. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link CityDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class CityListFragment extends ListFragment {

    // Key used to save list selection in a Bundle.
    private static final String CURRENT_CITY_KEY = "current_city";

    private int                   currentCityIndex;      // The currently selected list position.
    public  ArrayList<String>     citiesArrayList;       // List of city names.
    private CitiesChangedListener citiesChangedListener;
    private ArrayAdapter<String>  citiesArrayAdapter;

    /**
     * An interface describing listener for changes to selected city and preferred city.
     */
    public interface CitiesChangedListener {

        /**
         * The selected city is changed.
         *
         * @param city
         */
        public void onSelectedCityChanged(String city);

        /**
         * The preferred city is changed.
         *
         * @param city
         */
        public void onPreferredCityChanged(String city);
    }

    /**
     * Called when the parent activity is created.
     *
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        // The given Bundle has state information.
        if (savedInstanceState != null) {

            // Get the last selected city from the Bundle.
            currentCityIndex = savedInstanceState.getInt(CURRENT_CITY_KEY);
        }

        // Create ArrayList to save city names.
        citiesArrayList = new ArrayList<String>();

        // Set the Fragment's ListView adapter.
        setListAdapter(new CitiesArrayAdapter<String>(getActivity(), R.layout.city_list_item, citiesArrayList));

        ListView thisListView = getListView(); // Get the Fragment's ListView.
        citiesArrayAdapter = (ArrayAdapter<String>) getListAdapter();

        // Allow only one city to be selected at a time.
        thisListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        thisListView.setBackgroundColor(Color.WHITE);
        thisListView.setOnItemLongClickListener(citiesOnItemLongClickListener);
    }

    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks = sDummyCallbacks;

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(String id);
    }

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(String id) {
        }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CityListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: replace with a real list adapter.
        setListAdapter(new ArrayAdapter<DummyContent.DummyItem>(
                getActivity(),
                android.R.layout.simple_list_item_activated_1,
                android.R.id.text1,
                DummyContent.ITEMS));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        mCallbacks.onItemSelected(DummyContent.ITEMS.get(position).id);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }
}
