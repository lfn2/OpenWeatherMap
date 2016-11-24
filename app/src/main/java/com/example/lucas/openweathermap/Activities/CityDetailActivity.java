package com.example.lucas.openweathermap.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.lucas.openweathermap.Models.CityInfo;
import com.example.lucas.openweathermap.R;

public class CityDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_city_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //set toolbar title to city name
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(getString(R.string.intent_extra_cityInfo))) {
            CityInfo cityInfo = intent.getParcelableExtra(getString(R.string.intent_extra_cityInfo));

            getSupportActionBar().setTitle(cityInfo.getName());
        }

    }

}
