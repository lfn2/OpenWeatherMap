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

import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public abstract class FetchTask<T1, T2> extends AsyncTask<T1, Void, List<T2>> {

    private final int TIMEOUT_MILLIS = 10000;

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
     * Dialog for when the task's work timeout
     */
    protected void showTimeoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Couldn't find forecast");
        builder.setPositiveButton("Back", new DialogInterface.OnClickListener() {
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

    /**
     * Parses the JSON and return its information
     */
    protected abstract List<T2> parseDataFromJSON(String jsonStr) throws JSONException;
}


