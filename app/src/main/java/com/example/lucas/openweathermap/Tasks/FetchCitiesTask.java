package com.example.lucas.openweathermap.Tasks;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.lucas.openweathermap.Activities.MainActivity;
import com.example.lucas.openweathermap.BuildConfig;
import com.example.lucas.openweathermap.Models.CityInfo;
import com.example.lucas.openweathermap.Utils.Utils;
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
 * Class to fetch the nearby cities weather information from the OWM API
 */
public class FetchCitiesTask extends AsyncTask<LatLng, Void, List<CityInfo>> {

    private final String LOG_TAG = FetchCitiesTask.class.getSimpleName();

    private Context context;
    private LinearLayout progressBar;
    private ListView listView;
    private ArrayAdapter<CityInfo> cityListAdapter;

    public FetchCitiesTask(Context context, ArrayAdapter<CityInfo> cityListAdapter, LinearLayout progressBar, ListView listView) {
        this.context = context;
        this.cityListAdapter = cityListAdapter;
        this.progressBar = progressBar;
        this.listView = listView;
    }

    @Override
    protected void onPreExecute() {
        progressBar.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);

        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(List<CityInfo> result) {
        if (result == null || result.size() == 0) {
            showErrorDialog();
        }
        else {
            cityListAdapter.clear();
            cityListAdapter.addAll(result);

            progressBar.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected List<CityInfo> doInBackground(LatLng... coordinates) {
        double latitude = coordinates[0].latitude;
        double longitude = coordinates[0].longitude;

        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String citiesForecastJSON = null;

        try {
            URL url = createQuery(latitude, longitude);

            connection = createConnection(url);

            InputStream inputStream = connection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            citiesForecastJSON = buffer.toString();
        } catch (Exception e) {
            return errorHandling(e);
        } finally {
            if (connection != null)
                connection.disconnect();

            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    return errorHandling(e);
                }
        }

        List<CityInfo> citiesInfo = null;
        try {
            citiesInfo = getCitiesDataFromJSON(citiesForecastJSON);
        } catch (JSONException e) {
            return errorHandling(e);
        }

        return citiesInfo;
    }

    private List<CityInfo> errorHandling(Exception e) {
        Log.e(LOG_TAG, e.getMessage(), e);

        return null;
    }

    private void showErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Couldn't find cities");
        builder.setPositiveButton("Back", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int w) {
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
            }
        });

        builder.create().show();
    }

    /**
     * Parses the JSON information and returns a list of CityInfo
     */
    private List<CityInfo> getCitiesDataFromJSON(String jsonStr) throws JSONException {
        final String JSON_LIST = "list";
        final String JSON_NAME = "name";
        final String JSON_MAIN = "main";
        final String JSON_TEMP = "temp";
        final String JSON_MAX = "temp_max";
        final String JSON_MIN = "temp_min";
        final String JSON_WEATHER = "weather";
        final String JSON_DESCRIPTION = "description";
        final String JSON_ID = "id";
        final String JSON_HUMIDITY = "humidity";
        final String JSON_WIND_SPEED = "speed";
        final String JSON_WIND = "wind";

        JSONObject json = new JSONObject(jsonStr);
        JSONArray citiesForecast = json.getJSONArray(JSON_LIST);

        List<CityInfo> citiesInfo = new ArrayList<>();

        for (int i = 0; i < citiesForecast.length(); i++) {
            JSONObject cityForecast = citiesForecast.getJSONObject(i);

            String name = cityForecast.getString(JSON_NAME);

            JSONObject weatherObject = cityForecast.getJSONArray(JSON_WEATHER).getJSONObject(0);
            String weatherDescription = weatherObject.getString(JSON_DESCRIPTION);
            int weatherId = weatherObject.getInt(JSON_ID);

            JSONObject windObject = cityForecast.getJSONObject(JSON_WIND);
            double windSpeed = windObject.getDouble(JSON_WIND_SPEED);

            JSONObject tempObject = cityForecast.getJSONObject(JSON_MAIN);
            double temp = tempObject.getDouble(JSON_TEMP);
            double maxTemp = tempObject.getDouble(JSON_MAX);
            double minTemp = tempObject.getDouble(JSON_MIN);
            int humidity = tempObject.getInt(JSON_HUMIDITY);

            citiesInfo.add(new CityInfo(name, temp, maxTemp, minTemp, weatherDescription, weatherId, windSpeed, humidity));
        }

        return citiesInfo;
    }

    /**
     * Creates a connection from an url
     */
    private HttpURLConnection createConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setReadTimeout(10000);
        connection.connect();

        return connection;
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
