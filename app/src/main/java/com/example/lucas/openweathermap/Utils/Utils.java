package com.example.lucas.openweathermap.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.lucas.openweathermap.R;

import java.text.SimpleDateFormat;

public class Utils {

    public static boolean isWeeklyForecast(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        return prefs.getBoolean(context.getString(R.string.preference_key_weeklyForecast), false);
    }

    public static boolean isMetric(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        String unitPreference = prefs.getString(context.getString(R.string.preference_key_unit),
                context.getString(R.string.default_value_preference_unit));

        return unitPreference.equals(context.getString(R.string.unit_metric_value));
    }

    /**
     * Returns the number of cities to be searched from the shared preferences
     */
    public static int getCitiesCount(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        int citiesCount = prefs.getInt(context.getString(R.string.preference_key_citiesCount),
                Integer.parseInt(context.getString(R.string.preference_default_citiesCount)));

        return citiesCount;
    }

    /**
     * Formats the temperature to the preferred unit
     */
    public static String formatTemperature(Context context, double temperature, boolean isMetric) {
        double temp;

        //default temperature unit is Kelvins
        if (isMetric) {
            temp = temperature - 273.15;
        }
        else {
            temp = (temperature * ((double)9/5)) - 459.67;
        }

        //Prevent temperature from being -0
        if (temp < 0 && temp > -1)
            temp = 0;

        return context.getString(R.string.format_temperature, temp);
    }

    /**
     * Formats the date from milliseconds
     */
    public static String formatDate(long dateInMillis) {
        SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEEE, MMM dd");

        return shortenedDateFormat.format(dateInMillis);
    }

    /**
     * Returns an weather icon based on the weatherId from the OpenWeatherMap API
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
     * Returns an weather art based on the weatherId from the OpenWeatherMap API
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
