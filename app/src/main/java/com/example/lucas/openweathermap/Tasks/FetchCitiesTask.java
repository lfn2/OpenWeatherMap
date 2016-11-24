package com.example.lucas.openweathermap.Tasks;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.lucas.openweathermap.BuildConfig;
import com.example.lucas.openweathermap.Models.CityInfo;
import com.example.lucas.openweathermap.Models.Forecast;
import com.example.lucas.openweathermap.Utils.Utils;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to fetch the nearby cities weather information from the OWM API
 */
public class FetchCitiesTask extends FetchTask<LatLng, CityInfo> {

    private final String LOG_TAG = FetchCitiesTask.class.getSimpleName();

    public FetchCitiesTask(Context context, ArrayAdapter<CityInfo> cityListAdapter, LinearLayout progressBar, ListView listView) {
        super(context, cityListAdapter, progressBar, listView);
    }

    @Override
    protected List<CityInfo> doInBackground(LatLng... coordinates) {
        double latitude = coordinates[0].latitude;
        double longitude = coordinates[0].longitude;

        HttpURLConnection connection = null;
        String citiesForecastJSON = null;

        try {
            URL url = createQuery(latitude, longitude);

            connection = createConnection(url);

            citiesForecastJSON = Utils.getJSON(connection);
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage(), e);

            return null;
        } finally {
            if (connection != null)
                connection.disconnect();
        }

        List<CityInfo> citiesInfo;
        try {
            citiesInfo = parseDataFromJSON(citiesForecastJSON);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);

            return null;
        }

        return citiesInfo;
    }

    @Override
    protected List<CityInfo> parseDataFromJSON(String jsonStr) throws JSONException {
        final String JSON_LIST = "list";
        final String JSON_NAME = "name";
        final String JSON_MAIN = "main";
        final String JSON_TEMP = "temp";
        final String JSON_MAX = "temp_max";
        final String JSON_MIN = "temp_min";
        final String JSON_WEATHER = "weather";
        final String JSON_DESCRIPTION = "description";
        final String JSON_ID = "id";

        JSONObject json = new JSONObject(jsonStr);
        JSONArray citiesForecast = json.getJSONArray(JSON_LIST);

        List<CityInfo> citiesInfo = new ArrayList<>();

        for (int i = 0; i < citiesForecast.length(); i++) {
            JSONObject cityForecast = citiesForecast.getJSONObject(i);

            String name = cityForecast.getString(JSON_NAME);

            JSONObject weatherObject = cityForecast.getJSONArray(JSON_WEATHER).getJSONObject(0);
            String weatherDescription = weatherObject.getString(JSON_DESCRIPTION);
            int weatherId = weatherObject.getInt(JSON_ID);

            JSONObject tempObject = cityForecast.getJSONObject(JSON_MAIN);
            double temp = tempObject.getDouble(JSON_TEMP);
            double maxTemp = tempObject.getDouble(JSON_MAX);
            double minTemp = tempObject.getDouble(JSON_MIN);

            Forecast forecast = new Forecast(maxTemp, minTemp, weatherDescription, weatherId, 0);

            citiesInfo.add(new CityInfo(name, temp, forecast));
        }

        return citiesInfo;
    }

    /**
     * Creates the query for the OWM API
     */
    private URL createQuery(double latitude, double longitude) throws MalformedURLException {
        final String QUERY_BASE_URL = "http://api.openweathermap.org/data/2.5/find?";
        final String QUERY_LAT_PARAM = "lat";
        final String QUERY_LON_PARAM = "lon";
        final String QUERY_CNT_PARAM = "cnt";
        final String QUERY_APPID_PARAM = "APPID";

        final int citiesCount = Utils.getCitiesCount(context);

        Uri uri = Uri.parse(QUERY_BASE_URL).buildUpon()
                .appendQueryParameter(QUERY_LAT_PARAM, Double.toString(latitude))
                .appendQueryParameter(QUERY_LON_PARAM, Double.toString(longitude))
                .appendQueryParameter(QUERY_CNT_PARAM, Integer.toString(citiesCount))
                .appendQueryParameter(QUERY_APPID_PARAM, BuildConfig.OPEN_WEATHER_MAP_API_KEY)
                .build();

        return new URL(uri.toString());
    }
}
