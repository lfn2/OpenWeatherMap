package com.example.lucas.openweathermap.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lucas.openweathermap.Models.CityInfo;
import com.example.lucas.openweathermap.Models.Forecast;
import com.example.lucas.openweathermap.R;
import com.example.lucas.openweathermap.Utils.Utils;

import java.util.ArrayList;

/**
 * Exposes a list cityInfos from an ArrayAdapter to a ListView
 */
public class CityListAdapter extends ArrayAdapter<CityInfo> {

    private ArrayList<CityInfo> objects;

    public CityListAdapter(Context context, int resource, ArrayList<CityInfo> objects) {
        super(context, resource, objects);

        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_city, parent, false);

            ViewHolder viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }

        CityInfo cityInfo = objects.get(position);

        if (cityInfo != null) {
            ViewHolder viewHolder = (ViewHolder) view.getTag();

            Forecast forecast = cityInfo.getForecast();

            viewHolder.cityView.setText(cityInfo.getName());
            viewHolder.iconView.setImageResource(Utils.getWeatherIcon(forecast.getWeatherId()));
            viewHolder.tempView.setText(Utils.formatTemperature(getContext(), cityInfo.getTemperature(), Utils.isMetric(getContext())));
        }

        return view;
    }

    /**
     * Cache of the children views for the cities list
     */
    private static class ViewHolder {

        private final ImageView iconView;
        private final TextView cityView;
        private final TextView tempView;

        private ViewHolder(View view) {
            this.iconView = (ImageView) view.findViewById(R.id.list_item_icon);
            this.cityView = (TextView) view.findViewById(R.id.list_item_city_textview);
            this.tempView = (TextView) view.findViewById(R.id.list_item_temp_textview);
        }

    }

}
