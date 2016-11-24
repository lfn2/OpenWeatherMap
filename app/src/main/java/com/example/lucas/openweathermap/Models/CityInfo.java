package com.example.lucas.openweathermap.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class CityInfo implements Parcelable {

    private String name;
    private double temp;
    private double maxTemp;
    private double minTemp;
    private String weather;
    private int weatherId;
    private double windSpeed;
    private int humidity;

    public CityInfo(String name, double temp, double maxTemp, double minTemp, String weather, int weatherId, double windSpeed, int humidity) {
        this.name = name;
        this.temp = temp;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.weather = weather;
        this.weatherId = weatherId;
        this.windSpeed = windSpeed;
        this.humidity = humidity;
    }

    public String getName() {
        return name;
    }

    public double getTemp() {
        return temp;
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

    public int getWeatherId() {
        return weatherId;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public int getHumidity() {
        return humidity;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    //Create CityInfo reading its parcel
    public CityInfo(Parcel parcel) {
        this.name = parcel.readString();
        this.temp = parcel.readDouble();
        this.maxTemp = parcel.readDouble();
        this.minTemp = parcel.readDouble();
        this.weather = parcel.readString();
        this.weatherId = parcel.readInt();
        this.windSpeed = parcel.readDouble();
        this.humidity = parcel.readInt();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeDouble(temp);
        parcel.writeDouble(maxTemp);
        parcel.writeDouble(minTemp);
        parcel.writeString(weather);
        parcel.writeInt(weatherId);
        parcel.writeDouble(windSpeed);
        parcel.writeInt(humidity);
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
