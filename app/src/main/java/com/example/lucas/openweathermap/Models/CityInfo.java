package com.example.lucas.openweathermap.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class CityInfo implements Parcelable {

    private String name;
    private int maxTemp;
    private int minTemp;
    private String weather;

    public CityInfo(String name, int maxTemp, int minTemp, String weather) {
        this.name = name;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.weather = weather;
    }

    public String getName() {
        return name;
    }

    public int getMaxTemp() {
        return maxTemp;
    }

    public int getMinTemp() {
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
        this.maxTemp = parcel.readInt();
        this.minTemp = parcel.readInt();
        this.weather = parcel.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(maxTemp);
        parcel.writeInt(minTemp);
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
