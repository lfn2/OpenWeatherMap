package com.example.lucas.openweathermap.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.lucas.openweathermap.Models.CityInfo;
import com.example.lucas.openweathermap.R;
import com.example.lucas.openweathermap.Utils.Utils;

/**
 * A placeholder fragment containing a simple view.
 */
public class CityDetailActivityFragment extends Fragment {

    private ScrollView scrollView;
    private TextView cityNameView;
    private TextView maxTempView;
    private TextView minTempView;
    private ImageView iconView;
    private TextView weatherView;
    private TextView humidityView;
    private TextView windView;

    public CityDetailActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_city_detail, container, false);

        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(getString(R.string.intent_extra_cityInfo))) {
            scrollView = (ScrollView) rootView.findViewById(R.id.detail_scrollView);
            cityNameView = (TextView) rootView.findViewById(R.id.detail_cityName_textview);
            maxTempView = (TextView) rootView.findViewById(R.id.detail_maxTemp_textview);
            minTempView = (TextView) rootView.findViewById(R.id.detail_minTemp_textview);
            iconView = (ImageView) rootView.findViewById(R.id.detail_icon);
            weatherView = (TextView) rootView.findViewById(R.id.detail_weather_textview);
            humidityView = (TextView) rootView.findViewById(R.id.detail_humidity_textview);
            windView = (TextView) rootView.findViewById(R.id.detail_wind_textview);

            CityInfo cityInfo = intent.getParcelableExtra(getString(R.string.intent_extra_cityInfo));

            cityNameView.setText(cityInfo.getName());
            maxTempView.setText(Utils.formatTemperature(getActivity(), cityInfo.getMaxTemp(), true));
            minTempView.setText(Utils.formatTemperature(getActivity(), cityInfo.getMinTemp(), true));
            iconView.setImageResource(Utils.getWeatherArt(cityInfo.getWeatherId()));
            weatherView.setText(cityInfo.getWeather());
            humidityView.setText(getActivity().getString(R.string.format_humidity, (float) cityInfo.getHumidity()));
            windView.setText(Utils.formatWind(getActivity(), cityInfo.getWindSpeed(), true));
        }

        return rootView;
    }

    private void fillTextView(CityInfo cityInfo, TextView textView) {
        StringBuilder sb = new StringBuilder();
        sb.append("City: " + cityInfo.getName() + "\n");
        sb.append("Max temp: " + cityInfo.getMaxTemp() + " \n");
        sb.append("Min temp: " + cityInfo.getMinTemp() + "\n");
        sb.append("Weather: " + cityInfo.getWeather() + "\n");

        textView.setText(sb.toString());
    }
}
