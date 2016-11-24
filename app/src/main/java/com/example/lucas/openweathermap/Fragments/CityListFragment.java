package com.example.lucas.openweathermap.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.lucas.openweathermap.Activities.CityDetailActivity;
import com.example.lucas.openweathermap.Adapters.CityListAdapter;
import com.example.lucas.openweathermap.Models.CityInfo;
import com.example.lucas.openweathermap.R;
import com.example.lucas.openweathermap.Tasks.FetchCitiesTask;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class CityListFragment extends Fragment {

    private LinearLayout progressBar;
    private ListView listView;

    private CityListAdapter cityListAdapter;
    private ArrayList<CityInfo> cityInfos;

    private LatLng coordinates;

    public CityListFragment() {
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

        progressBar = (LinearLayout)  rootView.findViewById(R.id.listview_progressBar);

        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(getString(R.string.intent_extra_latLng))) {
            coordinates = intent.getParcelableExtra(getString(R.string.intent_extra_latLng));

            cityInfos = new ArrayList<>();
            cityListAdapter = new CityListAdapter(this.getActivity(), R.layout.list_item_city, cityInfos);

            listView = (ListView) rootView.findViewById(R.id.listview_cityList);
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

    /**
     * Creates an asynctask to fetch the cities info into the adapter
     */
    private void fetchCitiesInfo() {
        FetchCitiesTask fetchCitiesTask = new FetchCitiesTask(getContext(), this.cityListAdapter, progressBar, listView);

        fetchCitiesTask.execute(coordinates);
    }
}
