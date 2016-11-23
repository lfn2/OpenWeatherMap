package com.example.lucas.openweathermap.Database;

public class CityInfoTable {

    public static final String COLUMN_ID = "ID";
    public static final String TABLE_NAME = "CITY_INFO";
    public static final String INT_PK_AUTO = " INTEGER PRIMARY KEY AUTOINCREMENT";
    public static final String COMMA = ", ";
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_MAX_TEMP = "MAX_TEMP";
    public static final String COLUMN_MIN_TEMP = "MIN_TEMP";
    public static final String COLUMN_WEATHER = "WEATHER";
    public static final String TEXT_TYPE = " TEXT_NOT_NULL";
    public static final String INT_TYPE = " INTEGER NOT NULL";

    public static final String[] ALL_COLUMNS = { COLUMN_ID, COLUMN_NAME, COLUMN_MAX_TEMP, COLUMN_MIN_TEMP, COLUMN_WEATHER };

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + INT_PK_AUTO + COMMA +
            COLUMN_NAME + TEXT_TYPE + COMMA +
            COLUMN_MAX_TEMP + INT_TYPE + COMMA +
            COLUMN_MIN_TEMP + INT_TYPE + COMMA +
            COLUMN_WEATHER + TEXT_TYPE + ");";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
}
