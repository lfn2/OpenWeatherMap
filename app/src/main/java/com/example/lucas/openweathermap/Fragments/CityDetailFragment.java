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
import android.widget.TextView;

import com.example.lucas.openweathermap.Models.CityInfo;
import com.example.lucas.openweathermap.R;
import com.example.lucas.openweathermap.Utils.Utils;

/**
 * A placeholder fragment containing a simple view.
 */
public class CityDetailFragment extends Fragment {

    private TextView cityNameView;
    private TextView maxTempView;
    private TextView minTempView;
    private ImageView iconView;
    private TextView weatherView;
    private TextView humidityView;
    private TextView windView;

    private CityInfo cityInfo;

    public CityDetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_city_detail, container, false);

        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(getString(R.string.intent_extra_cityInfo))) {
            cityNameView = (TextView) rootView.findViewById(R.id.detail_cityName_textview);
            maxTempView = (TextView) rootView.findViewById(R.id.detail_maxTemp_textview);
            minTempView = (TextView) rootView.findViewById(R.id.detail_minTemp_textview);
            iconView = (ImageView) rootView.findViewById(R.id.detail_icon);
            weatherView = (TextView) rootView.findViewById(R.id.detail_weather_textview);
            humidityView = (TextView) rootView.findViewById(R.id.detail_humidity_textview);
            windView = (TextView) rootView.findViewById(R.id.detail_wind_textview);

            cityInfo = intent.getParcelableExtra(getString(R.string.intent_extra_cityInfo));

            boolean isMetric = Utils.isMetric(getContext());

            cityNameView.setText(cityInfo.getName());
            maxTempView.setText(Utils.formatTemperature(getActivity(), cityInfo.getMaxTemp(), isMetric));
            minTempView.setText(Utils.formatTemperature(getActivity(), cityInfo.getMinTemp(), isMetric));
            iconView.setImageResource(Utils.getWeatherArt(cityInfo.getWeatherId()));
            weatherView.setText(cityInfo.getWeather());
            humidityView.setText(getActivity().getString(R.string.format_humidity, (float) cityInfo.getHumidity()));
            windView.setText(Utils.formatWind(getActivity(), cityInfo.getWindSpeed(), isMetric));
        }

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
        String weather = cityInfo.getWeather();
        Context context = getContext();
        String minTemp = Utils.formatTemperature(context, cityInfo.getMinTemp(), Utils.isMetric(context));
        String maxTemp = Utils.formatTemperature(context, cityInfo.getMaxTemp(), Utils.isMetric(context));

        String shareString = context.getString(R.string.share_string);

        shareIntent.putExtra(Intent.EXTRA_TEXT,
                String.format(shareString, cityName, weather, minTemp, maxTemp));

        return shareIntent;
    }

}
