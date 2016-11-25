package com.example.lucas.openweathermap.Tasks;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.lucas.openweathermap.Activities.MainActivity;
import com.example.lucas.openweathermap.R;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Abstract task for fetching weather information
 */
public abstract class FetchTask<T1, T2> extends AsyncTask<T1, Void, List<T2>> {

    private final int TIMEOUT_MILLIS = 12000;

    protected Context context;
    protected LinearLayout progressBar;
    protected ListView listView;
    protected ArrayAdapter<T2> arrayAdapter;

    public FetchTask(Context context, ArrayAdapter<T2> arrayAdapter, LinearLayout progressBar, ListView listView) {
        this.context = context;
        this.arrayAdapter = arrayAdapter;
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
    protected void onPostExecute(List<T2> result) {
        if (result == null || result.size() == 0) {
            showTimeoutDialog();
        }
        else {
            arrayAdapter.clear();
            arrayAdapter.addAll(result);

            progressBar.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Dialog for when the task's background work timeout
     */
    protected void showTimeoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getString(R.string.error_fetch_forecast));
        builder.setPositiveButton(context.getString(R.string.string_back), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int w) {
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
            }
        });

        builder.create().show();
    }

    /**
     * Creates an URL connection
     */
    protected HttpURLConnection createConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setReadTimeout(this.TIMEOUT_MILLIS);
        connection.connect();

        return connection;
    }

    public static String readJSON(HttpURLConnection connection) throws IOException {
        InputStream inputStream = connection.getInputStream();
        StringBuilder sb = new StringBuilder();

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }

        return sb.toString();
    }

    /**
     * Parses the JSON and return its information
     */
    protected abstract List<T2> parseDataFromJSON(String jsonStr) throws JSONException;
}


