package com.example.lucas.openweathermap.Tasks;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.example.lucas.openweathermap.BuildConfig;
import com.example.lucas.openweathermap.Models.CityInfo;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lucas on 22/11/2016.
 */

public class FetchCitiesTask extends AsyncTask<LatLng, Void, List<CityInfo>> {

    private final String LOG_TAG = FetchCitiesTask.class.getSimpleName();

    private ArrayAdapter<CityInfo> cityListAdapter;

    public FetchCitiesTask(ArrayAdapter<CityInfo> cityListAdapter) {
        this.cityListAdapter = cityListAdapter;
    }

    @Override
    protected void onPostExecute(List<CityInfo> result) {
        if (result != null) {
            cityListAdapter.clear();
            cityListAdapter.addAll(result);
        }
    }

    @Override
    protected List<CityInfo> doInBackground(LatLng... coordinates) {
        if (coordinates.length != 1)
            return null; //something wrong is not right

        double latitude = coordinates[0].latitude;
        double longitude = coordinates[0].longitude;

        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String citiesForecastJSON = null;

        try {
            URL url = createQuery(latitude, longitude);

            Log.e(LOG_TAG, "Requesting API...");

            connection = createConnection(url);

            InputStream inputStream = connection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null)
                return null;

            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0)
                return null;

            citiesForecastJSON = buffer.toString();
            Log.e(LOG_TAG, "Request Finished");
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            return null;
        } finally {
            if (connection != null)
                connection.disconnect();

            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, e.getMessage(), e);
                }
        }

        List<CityInfo> citiesInfo = null;
        try {
            citiesInfo = getCitiesDataFromJSON(citiesForecastJSON);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return citiesInfo;
    }

    private List<CityInfo> getCitiesDataFromJSON(String jsonStr) throws JSONException {
        final String JSON_LIST = "list";
        final String JSON_NAME = "name";
        final String JSON_MAIN = "main";
        final String JSON_MAX = "temp_max";
        final String JSON_MIN = "temp_min";
        final String JSON_WEATHER = "weather";
        final String JSON_DESCRIPTION = "description";

        JSONObject json = new JSONObject(jsonStr);
        JSONArray citiesForecast = json.getJSONArray(JSON_LIST);

        List<CityInfo> citiesInfo = new ArrayList<>();

        for (int i = 0; i < citiesForecast.length(); i++) {
            JSONObject cityForecast = citiesForecast.getJSONObject(i);

            String name = cityForecast.getString(JSON_NAME);

            JSONObject weatherObject = cityForecast.getJSONArray(JSON_WEATHER).getJSONObject(0);
            String description = weatherObject.getString(JSON_DESCRIPTION);

            JSONObject tempObject = cityForecast.getJSONObject(JSON_MAIN);
            double maxTemp = tempObject.getDouble(JSON_MAX);
            double minTemp = tempObject.getDouble(JSON_MIN);

            citiesInfo.add(new CityInfo(name, maxTemp, minTemp, description));
        }

        return citiesInfo;
    }

    private HttpURLConnection createConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        return connection;
    }

    private URL createQuery(double latitude, double longitude) throws MalformedURLException {
        final String QUERY_BASE_URL = "http://api.openweathermap.org/data/2.5/find?";
        final String QUERY_LAT_PARAM = "lat";
        final String QUERY_LON_PARAM = "lon";
        final String QUERY_CNT_PARAM = "cnt";
        final String QUERY_APPID_PARAM = "APPID";
        final int CNT = 15;

        Uri uri = Uri.parse(QUERY_BASE_URL).buildUpon()
                .appendQueryParameter(QUERY_LAT_PARAM, Double.toString(latitude))
                .appendQueryParameter(QUERY_LON_PARAM, Double.toString(longitude))
                .appendQueryParameter(QUERY_CNT_PARAM, Integer.toString(CNT))
                .appendQueryParameter(QUERY_APPID_PARAM, BuildConfig.OPEN_WEATHER_MAP_API_KEY)
                .build();

        return new URL(uri.toString());
    }
}
