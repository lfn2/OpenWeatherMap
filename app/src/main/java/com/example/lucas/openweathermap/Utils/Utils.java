package com.example.lucas.openweathermap.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.lucas.openweathermap.R;

public class Utils {

    public static boolean isMetric(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        String unitPreference = prefs.getString(context.getString(R.string.key_preference_unit),
                                                context.getString(R.string.default_unit_preference_value));

        return unitPreference.equals(context.getString(R.string.unit_metric_value));
    }

    public static String formatTemperature(Context context, double temperature, boolean isMetric) {
        double temp;

        //default temperature unit is Kelvins
        if (isMetric) {
            temp = temperature - 273.15;
        }
        else {
            temp = (temperature * ((double)9/5)) - 459.67;
        }

        return context.getString(R.string.format_temperature, temp);
    }

    public static String formatWind(Context context, double windSpeed, boolean isMetric) {
        int windFormat;

        if (isMetric)
            windFormat = R.string.format_wind_kmh;
        else {
            windFormat = R.string.format_wind_mph;
            windSpeed = windSpeed / 1.609344d;
        }

        return String.format(context.getString(windFormat), windSpeed);
    }

    /**
     * returns an weather icon based on the weatherId from the OpenWeatherMap API
     * @param weatherId from OWM API
     * @return corresponding resource id
     */
    public static int getWeatherIcon(int weatherId) {
        if (weatherId >= 200 && weatherId <= 232) {
            return R.drawable.ic_storm;
        } else if (weatherId >= 300 && weatherId <= 321) {
            return R.drawable.ic_light_rain;
        } else if (weatherId >= 500 && weatherId <= 504) {
            return R.drawable.ic_rain;
        } else if (weatherId == 511) {
            return R.drawable.ic_snow;
        } else if (weatherId >= 520 && weatherId <= 531) {
            return R.drawable.ic_rain;
        } else if (weatherId >= 600 && weatherId <= 622) {
            return R.drawable.ic_snow;
        } else if (weatherId >= 701 && weatherId <= 761) {
            return R.drawable.ic_fog;
        } else if (weatherId == 761 || weatherId == 781) {
            return R.drawable.ic_storm;
        } else if (weatherId == 800) {
            return R.drawable.ic_clear;
        } else if (weatherId == 801) {
            return R.drawable.ic_light_clouds;
        } else if (weatherId >= 802 && weatherId <= 804) {
            return R.drawable.ic_cloudy;
        }

        return -1;
    }

    /**
     * returns an weather art based on the weatherId from the OpenWeatherMap API
     * @param weatherId from OWM API
     * @return corresponding resource id
     */
    public static int getWeatherArt(int weatherId) {
        if (weatherId >= 200 && weatherId <= 232) {
            return R.drawable.art_storm;
        } else if (weatherId >= 300 && weatherId <= 321) {
            return R.drawable.art_light_rain;
        } else if (weatherId >= 500 && weatherId <= 504) {
            return R.drawable.art_rain;
        } else if (weatherId == 511) {
            return R.drawable.art_snow;
        } else if (weatherId >= 520 && weatherId <= 531) {
            return R.drawable.art_rain;
        } else if (weatherId >= 600 && weatherId <= 622) {
            return R.drawable.art_snow;
        } else if (weatherId >= 701 && weatherId <= 761) {
            return R.drawable.art_fog;
        } else if (weatherId == 761 || weatherId == 781) {
            return R.drawable.art_storm;
        } else if (weatherId == 800) {
            return R.drawable.art_clear;
        } else if (weatherId == 801) {
            return R.drawable.art_light_clouds;
        } else if (weatherId >= 802 && weatherId <= 804) {
            return R.drawable.art_clouds;
        }
        return -1;
    }

}
