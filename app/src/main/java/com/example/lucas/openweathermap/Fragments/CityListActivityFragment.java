package com.example.lucas.openweathermap.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.lucas.openweathermap.Activities.CityDetailActivity;
import com.example.lucas.openweathermap.Models.CityInfo;
import com.example.lucas.openweathermap.R;
import com.example.lucas.openweathermap.Tasks.FetchCitiesTask;
import com.google.android.gms.maps.model.LatLng;

/**
 * A placeholder fragment containing a simple view.
 */
public class CityListActivityFragment extends Fragment {

    private ArrayAdapter<CityInfo> cityListAdapter;

    private LatLng coordinates;

    public CityListActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_city_list, container, false);

        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(getString(R.string.intent_extra_latLng))) {
            coordinates = intent.getParcelableExtra(getString(R.string.intent_extra_latLng));

            cityListAdapter = new ArrayAdapter<>(this.getActivity(), R.layout.list_item_city, R.id.list_item_city_textview);

            ListView listView = (ListView) rootView.findViewById(R.id.listview_cityList);
            listView.setAdapter(cityListAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    CityInfo cityInfo = cityListAdapter.getItem(position);

                    Intent intent = new Intent(getActivity(), CityDetailActivity.class)
                            .putExtra(getString(R.string.intent_extra_cityInfo), cityInfo);

                    startActivity(intent);
                }
            });

            fetchCitiesInfo();
        }

        return rootView;
    }

    private void fetchCitiesInfo() {
        FetchCitiesTask fetchCitiesTask = new FetchCitiesTask(this.cityListAdapter);

        fetchCitiesTask.execute(coordinates);
    }
}
