package com.example.lucas.openweathermap.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lucas.openweathermap.Models.CityInfo;
import com.example.lucas.openweathermap.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class CityDetailActivityFragment extends Fragment {

    public CityDetailActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_city_detail, container, false);

        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(getString(R.string.intent_extra_cityInfo))) {
            CityInfo cityInfo = intent.getParcelableExtra(getString(R.string.intent_extra_cityInfo));
            TextView textView = (TextView) rootView.findViewById(R.id.textview_cityDetail);

            fillTextView(cityInfo, textView);
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
