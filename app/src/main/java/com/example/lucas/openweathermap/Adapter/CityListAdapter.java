package com.example.lucas.openweathermap.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lucas.openweathermap.Models.CityInfo;
import com.example.lucas.openweathermap.R;
import com.example.lucas.openweathermap.Utils.Utils;

import java.util.ArrayList;

public class CityListAdapter extends ArrayAdapter<CityInfo> {

    private ArrayList<CityInfo> objects;

    public CityListAdapter(Context context, int resource, ArrayList<CityInfo> objects) {
        super(context, resource, objects);

        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
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

            viewHolder.iconView.setImageResource(Utils.getWeatherIcon(cityInfo.getWeatherId()));
            viewHolder.cityView.setText(cityInfo.getName());
            viewHolder.tempView.setText(Utils.formatTemperature(getContext(), cityInfo.getTemp(), true));
        }

        return view;
    }

    //Cache of the children views
    private static class ViewHolder {

        private final ImageView iconView;
        private final TextView cityView;
        private final TextView tempView;

        private ViewHolder(View view) {
            iconView = (ImageView) view.findViewById(R.id.list_item_icon);
            cityView = (TextView) view.findViewById(R.id.list_item_city_textview);
            tempView = (TextView) view.findViewById(R.id.list_item_temp_textview);
        }

    }

}
