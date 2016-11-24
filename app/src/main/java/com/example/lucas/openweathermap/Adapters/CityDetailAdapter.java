package com.example.lucas.openweathermap.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lucas.openweathermap.Models.Forecast;
import com.example.lucas.openweathermap.R;
import com.example.lucas.openweathermap.Utils.Utils;

import java.util.ArrayList;

/**
 * Exposes a list forecasts from an ArrayAdapter to a ListView
 */
public class CityDetailAdapter extends ArrayAdapter<Forecast> {

    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_LIST = 1;
    private static final int VIEW_TYPE_COUNT = 2;

    private ArrayList<Forecast> objects;

    public CityDetailAdapter(Context context, int resource, ArrayList<Forecast> objects) {
        super(context, resource, objects);

        this.objects = objects;
    }

    @Override
    public int getItemViewType(int position) {
        int viewType;

        if (position == 0)
            viewType = VIEW_TYPE_TODAY;
        else
            viewType = VIEW_TYPE_LIST;

        return viewType;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //select view layout based on position
            int viewType = getItemViewType(position);
            if (viewType == VIEW_TYPE_TODAY)
                view = inflater.inflate(R.layout.list_item_city_detail_today, parent, false);
            else
                view = inflater.inflate(R.layout.list_item_city_detail, parent, false);

            ViewHolder viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }

        Forecast forecast = objects.get(position);
        if (forecast != null) {
            Context context = getContext();

            ViewHolder viewHolder = (ViewHolder) view.getTag();

            //select icon type based on position
            int viewType = getItemViewType(position);
            if (viewType == VIEW_TYPE_TODAY)
                viewHolder.iconView.setImageResource(Utils.getWeatherArt(forecast.getWeatherId()));
            else
                viewHolder.iconView.setImageResource(Utils.getWeatherIcon(forecast.getWeatherId()));

            viewHolder.dateView.setText(Utils.formatDate(forecast.getDateTime()));
            viewHolder.weatherView.setText(forecast.getWeather());

            boolean isMetric = Utils.isMetric(context);

            String maxTemp = Utils.formatTemperature(context, forecast.getMaxTemp(), isMetric);
            viewHolder.maxTempView.setText(maxTemp);

            String minTemp = Utils.formatTemperature(context, forecast.getMinTemp(), isMetric);
            viewHolder.minTempView.setText(minTemp);
        }

        return view;
    }

    /**
     * Cache of the children views for the cities list
     */
    private static class ViewHolder {

        private final ImageView iconView;
        private final TextView dateView;
        private final TextView weatherView;
        private final TextView maxTempView;
        private final TextView minTempView;

        private ViewHolder(View view) {
            iconView = (ImageView) view.findViewById(R.id.list_item_icon);
            dateView = (TextView) view.findViewById(R.id.list_item_date_textview);
            weatherView = (TextView) view.findViewById(R.id.list_item_weather_textview);
            maxTempView = (TextView) view.findViewById(R.id.list_item_maxTemp_textview);
            minTempView = (TextView) view.findViewById(R.id.list_item_minTemp_textview);
        }

    }

}
