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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lucas.openweathermap.Adapters.CityDetailAdapter;
import com.example.lucas.openweathermap.Listeners.OnTaskCompleteListener;
import com.example.lucas.openweathermap.Models.CityInfo;
import com.example.lucas.openweathermap.Models.Forecast;
import com.example.lucas.openweathermap.R;
import com.example.lucas.openweathermap.Tasks.FetchCityWeeklyForecastTask;
import com.example.lucas.openweathermap.Utils.Utils;

import java.util.ArrayList;

/**
 * Fragment for the detailed screen of a city forecast
 */
public class CityDetailFragment extends Fragment implements OnTaskCompleteListener {

    private LinearLayout mProgressBar;
    private ListView mListView;

    private CityInfo mCityInfo;
    private ArrayList<Forecast> mForecast;
    private CityDetailAdapter mCityDetailAdapter;

    private ShareActionProvider mShareActionProvider;

    public CityDetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(getString(R.string.intent_extra_cityInfo)))
            mCityInfo = intent.getParcelableExtra(getString(R.string.intent_extra_cityInfo));

        mForecast = new ArrayList<>();

        if (Utils.isWeeklyForecast(getContext()))
            return getWeeklyForecastView(inflater, container);
        else
            return getSingleDayView(inflater, container);
    }

    private View getSingleDayView(LayoutInflater inflater, ViewGroup container) {
        View rootView = inflater.inflate(R.layout.fragment_city_detail_single, container, false);

        if (mCityInfo != null) {
            TextView dateView = (TextView) rootView.findViewById(R.id.detail_single_date_textview);
            TextView maxTempView = (TextView) rootView.findViewById(R.id.detail_single_maxTemp_textview);
            TextView minTempView = (TextView) rootView.findViewById(R.id.detail_single_minTemp_textview);
            ImageView iconView = (ImageView) rootView.findViewById(R.id.detail_single_item_icon);
            TextView weatherView = (TextView) rootView.findViewById(R.id.detail_single_weather_textview);

            boolean isMetric = Utils.isMetric(getContext());

            Forecast forecast = mCityInfo.getForecast();

            dateView.setText(Utils.formatDate(forecast.getDateTime()));
            maxTempView.setText(Utils.formatTemperature(getActivity(), forecast.getMaxTemp(), isMetric));
            minTempView.setText(Utils.formatTemperature(getActivity(), forecast.getMinTemp(), isMetric));
            iconView.setImageResource(Utils.getWeatherArt(forecast.getWeatherId()));
            weatherView.setText(forecast.getWeather());
        }

        mForecast.add(mCityInfo.getForecast());

        return rootView;
    }

    private View getWeeklyForecastView(LayoutInflater inflater, ViewGroup container) {
        View rootView = inflater.inflate(R.layout.fragment_city_detail_weekly, container, false);

        if (mCityInfo != null) {
            mCityDetailAdapter = new CityDetailAdapter(getActivity(), R.layout.list_item_city_detail, mForecast);

            mListView = (ListView) rootView.findViewById(R.id.listview_cityForecast);
            mListView.setAdapter(mCityDetailAdapter);
        }

        mProgressBar = (LinearLayout) rootView.findViewById(R.id.listview_progressBar);

        fetchWeeklyForecast();

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_city_detail, menu);

        MenuItem menuItem = menu.findItem(R.id.action_share);

        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        //The share intent can only be created here if we have a single day forecast
        if (mForecast.size() == 1)
            mShareActionProvider.setShareIntent(createShareForecastIntent());
    }

    @Override
    public void OnTaskComplete() {
        mShareActionProvider.setShareIntent(createShareForecastIntent());
    }

    /**
     * Creates the forecast sharing intent
     * @return the forecast sharing intent
     */
    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        shareIntent.setType("text/plain");

        Context context = getContext();
        boolean isMetric = Utils.isMetric(context);

        String cityName = mCityInfo.getName();
        Forecast forecastToday = mForecast.get(0);
        String weather = forecastToday.getWeather();
        String minTemp = Utils.formatTemperature(context, forecastToday.getMinTemp(), isMetric);
        String maxTemp = Utils.formatTemperature(context, forecastToday.getMaxTemp(), isMetric);

        String shareString = context.getString(R.string.share_string);

        shareIntent.putExtra(Intent.EXTRA_TEXT,
                String.format(shareString, cityName, weather, minTemp, maxTemp));

        return shareIntent;
    }

    private void fetchWeeklyForecast() {
        FetchCityWeeklyForecastTask fetchCityForecast =
                new FetchCityWeeklyForecastTask(getContext(), mCityDetailAdapter, mProgressBar, mListView, this);

        fetchCityForecast.execute(mCityInfo.getName());
    }
}
