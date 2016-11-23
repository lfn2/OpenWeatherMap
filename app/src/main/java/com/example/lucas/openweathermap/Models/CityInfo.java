package com.example.lucas.openweathermap.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Lucas on 23/11/2016.
 */

public class CityInfo implements Parcelable {

    private String name;
    private double maxTemp;
    private double minTemp;
    private String weather;

    public CityInfo(String name, double maxTemp, double minTemp, String weather) {
        this.name = name;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.weather = weather;
    }




    public String getName() {
        return name;
    }

    public double getMaxTemp() {
        return maxTemp;
    }

    public double getMinTemp() {
        return minTemp;
    }

    public String getWeather() {
        return weather;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    //Create CityInfo reading its parcel
    public CityInfo(Parcel parcel) {
        this.name = parcel.readString();
        this.maxTemp = parcel.readDouble();
        this.minTemp = parcel.readDouble();
        this.weather = parcel.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeDouble(maxTemp);
        parcel.writeDouble(minTemp);
        parcel.writeString(weather);
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
