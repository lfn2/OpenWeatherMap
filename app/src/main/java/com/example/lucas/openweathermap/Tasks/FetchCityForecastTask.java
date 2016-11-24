package com.example.lucas.openweathermap.Tasks;

import android.content.Context;
import android.net.Uri;
import android.text.format.Time;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.lucas.openweathermap.Adapters.CityDetailAdapter;
import com.example.lucas.openweathermap.BuildConfig;
import com.example.lucas.openweathermap.Models.Forecast;
import com.example.lucas.openweathermap.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FetchCityForecastTask extends FetchTask<String, Forecast> {

    private final String LOG_TAG = FetchCityForecastTask.class.getSimpleName();

    private static final int DAYS_FORECAST = 7;

    public FetchCityForecastTask(Context context, CityDetailAdapter cityDetailAdapter, LinearLayout progressBar, ListView listView) {
        super (context, cityDetailAdapter, progressBar, listView);
    }

    @Override
    protected List<Forecast> doInBackground(String... strings) {
        if (strings.length != 1)
            return null;

        String cityName = strings[0];

        HttpURLConnection connection = null;
        String citiesForecastJSON = null;

        try {
            URL url = createQuery(cityName);

            connection = createConnection(url);

            citiesForecastJSON = Utils.getJSON(connection);

        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage(), e);

            return null;
        } finally {
            if (connection != null)
                connection.disconnect();
        }

        List<Forecast> forecasts;
        try {
            forecasts = parseDataFromJSON(citiesForecastJSON);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);

            return null;
        }

        return forecasts;
    }

    @Override
    protected List<Forecast> parseDataFromJSON(String jsonStr) throws JSONException {
        final String JSON_LIST = "list";
        final String JSON_TEMP = "temp";
        final String JSON_MAX = "max";
        final String JSON_MIN = "min";
        final String JSON_WEATHER = "weather";
        final String JSON_DESCRIPTION = "description";
        final String JSON_ID = "id";

        JSONObject json = new JSONObject(jsonStr);
        JSONArray forecastArray = json.getJSONArray(JSON_LIST);

        List<Forecast> forecast = new ArrayList<>();

        //Here we also get the day of the forecast.
        //We start with the current day and increment by one on each forecast
        Time dayTime = new Time();
        dayTime.setToNow();
        int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);
        dayTime = new Time();

        for (int i = 0; i < forecastArray.length(); i++) {
            JSONObject dayForecast = forecastArray.getJSONObject(i);

            JSONObject weatherObject = dayForecast.getJSONArray(JSON_WEATHER).getJSONObject(0);
            String weatherDescription = weatherObject.getString(JSON_DESCRIPTION);
            int weatherId = weatherObject.getInt(JSON_ID);

            JSONObject tempObject = dayForecast.getJSONObject(JSON_TEMP);
            double maxTemp = tempObject.getDouble(JSON_MAX);
            double minTemp = tempObject.getDouble(JSON_MIN);

            // Cheating to convert this to UTC time, which is what we want anyhow
            long dateTime = dayTime.setJulianDay(julianStartDay+i);

            forecast.add(new Forecast(maxTemp, minTemp, weatherDescription, weatherId, dateTime));
        }

        return forecast;
    }

    private URL createQuery(String cityName) throws MalformedURLException {
        final String FORECAST_BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?";
        final String QUERY_PARAM = "q";
        final String DAYS_PARAM = "cnt";
        final String APPID_PARAM = "APPID";

        Uri uri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAM, cityName)
                .appendQueryParameter(DAYS_PARAM, Integer.toString(DAYS_FORECAST))
                .appendQueryParameter(APPID_PARAM, BuildConfig.OPEN_WEATHER_MAP_API_KEY)
                .build();

        return new URL(uri.toString());
    }
}
