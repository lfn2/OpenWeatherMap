package com.example.lucas.openweathermap.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.lucas.openweathermap.Models.CityInfo;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private SQLiteDatabase db;

    private static final String[] DATABASE_CREATE = { CityInfoTable.CREATE_TABLE };
    private static final String[] DATABASE_DELETE = { CityInfoTable.DROP_TABLE };

    private static final String DATABASE_NAME = "OpenWeatherMap.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (String sql : DATABASE_CREATE)
            db.execSQL(sql);
    }

    public void clean() {
        this.db = this.getWritableDatabase();

        for (String sql : DATABASE_DELETE)
            this.db.execSQL(sql);

        for (String sql : DATABASE_CREATE)
            this.db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public long insertCityInfo(CityInfo cityInfo) {
        this.db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CityInfoTable.COLUMN_NAME, cityInfo.getName());
        values.put(CityInfoTable.COLUMN_MAX_TEMP, cityInfo.getMaxTemp());
        values.put(CityInfoTable.COLUMN_MIN_TEMP, cityInfo.getMinTemp());
        values.put(CityInfoTable.COLUMN_WEATHER, cityInfo.getWeather());

        return this.db.insert(CityInfoTable.TABLE_NAME, null, values);
    }

    public List<CityInfo> getAllCityInfo() {
        this.db = this.getReadableDatabase();

        List<CityInfo> citiesInfo = new ArrayList<>();

        Cursor c = this.db.query(true, CityInfoTable.TABLE_NAME, CityInfoTable.ALL_COLUMNS, null, null, null, null, null, null);

        try {
            if (c != null)
                c.moveToFirst();

            while (!c.isAfterLast()) {
                String name = c.getString(c.getColumnIndex(CityInfoTable.COLUMN_NAME));
                int maxTemp = c.getInt(c.getColumnIndex(CityInfoTable.COLUMN_MAX_TEMP));
                int minTemp = c.getInt(c.getColumnIndex(CityInfoTable.COLUMN_MIN_TEMP));
                String weather = c.getString(c.getColumnIndex(CityInfoTable.COLUMN_WEATHER));

                citiesInfo.add(new CityInfo(name, maxTemp, minTemp, weather));

                c.moveToNext();
            }

            return citiesInfo;
        } finally {
            c.close();
        }
    }

}
