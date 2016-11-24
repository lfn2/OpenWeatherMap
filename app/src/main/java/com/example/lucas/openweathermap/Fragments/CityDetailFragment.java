package com.example.lucas.openweathermap.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.lucas.openweathermap.Adapters.CityDetailAdapter;
import com.example.lucas.openweathermap.Models.CityInfo;
import com.example.lucas.openweathermap.Models.Forecast;
import com.example.lucas.openweathermap.R;
import com.example.lucas.openweathermap.Tasks.FetchCityForecastTask;
import com.example.lucas.openweathermap.Utils.Utils;

import java.util.ArrayList;

/**
 * Fragment for the detailed screen of a city forecast
 */
public class CityDetailFragment extends Fragment {

    private LinearLayout progressBar;
    private ListView listView;

    private CityInfo cityInfo;
    private ArrayList<Forecast> forecast;
    private CityDetailAdapter cityDetailAdapter;

    public CityDetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_city_detail, container, false);

        progressBar = (LinearLayout) rootView.findViewById(R.id.listview_progressBar);

        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(getString(R.string.intent_extra_cityInfo))) {
            cityInfo = intent.getParcelableExtra(getString(R.string.intent_extra_cityInfo));

            forecast = new ArrayList<>();

            cityDetailAdapter = new CityDetailAdapter(this.getActivity(), R.layout.list_item_city_detail, forecast);

            listView = (ListView) rootView.findViewById(R.id.listview_cityForecast);
            listView.setAdapter(cityDetailAdapter);
        }

        forecast.add(cityInfo.getForecast());

        fetchWeekForecast();

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_city_detail, menu);

        MenuItem menuItem = menu.findItem(R.id.action_share);

        ShareActionProvider shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        if (shareActionProvider != null )
            shareActionProvider.setShareIntent(createShareForecastIntent());
    }

    /**
     * Creates the forecast sharing intent
     * @return the forecast sharing intent
     */
    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        shareIntent.setType("text/plain");

        String cityName = cityInfo.getName();

        Forecast forecast = cityInfo.getForecast();

        String weather = forecast.getWeather();
        Context context = getContext();
        String minTemp = Utils.formatTemperature(context, forecast.getMinTemp(), Utils.isMetric(context));
        String maxTemp = Utils.formatTemperature(context, forecast.getMaxTemp(), Utils.isMetric(context));

        String shareString = context.getString(R.string.share_string);

        shareIntent.putExtra(Intent.EXTRA_TEXT,
                String.format(shareString, cityName, weather, minTemp, maxTemp));

        return shareIntent;
    }

    private void fetchWeekForecast() {
        FetchCityForecastTask fetchCityForecast = new FetchCityForecastTask(getContext(), cityDetailAdapter, progressBar, listView);

        fetchCityForecast.execute(cityInfo.getName());
    }
}
