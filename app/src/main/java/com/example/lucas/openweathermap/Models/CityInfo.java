package com.example.lucas.openweathermap.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Model for a city's information
 */
public class CityInfo implements Parcelable {

    private String name;
    private double temperature;
    private Forecast forecast;

    public CityInfo(String name, double temperature, Forecast forecast) {
        this.name = name;
        this.temperature = temperature;
        this.forecast = forecast;
    }

    public String getName() {
        return name;
    }

    public double getTemperature() {
        return temperature;
    }

    public Forecast getForecast() {
        return forecast;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    //Create CityInfo reading its parcel
    public CityInfo(Parcel parcel) {
        this.name = parcel.readString();
        this.temperature = parcel.readDouble();
        this.forecast = parcel.readParcelable(Forecast.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(this.name);
        parcel.writeDouble(this.temperature);
        parcel.writeParcelable(this.forecast, flags);
    }

    public static final Parcelable.Creator<CityInfo> CREATOR = new Parcelable.Creator<CityInfo>() {
        public CityInfo createFromParcel(Parcel parcel) {
            return new CityInfo(parcel);
        }

        public CityInfo[] newArray(int size) {
            return new CityInfo[size];
        }
    };

}