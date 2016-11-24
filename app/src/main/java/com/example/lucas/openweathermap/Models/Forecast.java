package com.example.lucas.openweathermap.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Forecast implements Parcelable {

    private double maxTemp;
    private double minTemp;
    private String weather;
    private int weatherId;
    private long dateTime;

    public Forecast(double maxTemp, double minTemp, String weather, int weatherId, long dateTime) {
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.weather = weather;
        this.weatherId = weatherId;
        this.dateTime = dateTime;
    }

    public double getMaxTemp() {
        return maxTemp;
    }

    public double getMinTemp() {
        return minTemp;
    }

    public String getWeather() {
        return this.weather;
    }

    public int getWeatherId() {
        return this.weatherId;
    }

    public long getDateTime() {
        return dateTime;
    }

    public static final Creator<Forecast> CREATOR = new Creator<Forecast>() {
        @Override
        public Forecast createFromParcel(Parcel in) {
            return new Forecast(in);
        }

        @Override
        public Forecast[] newArray(int size) {
            return new Forecast[size];
        }
    };

    @Override
    public int describeContents() {
        return hashCode();
    }

    protected Forecast(Parcel in) {
        maxTemp = in.readDouble();
        minTemp = in.readDouble();
        weather = in.readString();
        weatherId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(maxTemp);
        parcel.writeDouble(minTemp);
        parcel.writeString(weather);
        parcel.writeInt(weatherId);
    }
}
