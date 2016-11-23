package com.example.lucas.openweathermap.Utils;

public class Utils {

    public static int convertTemperatureFromKelvin(double temperature, String unit) {
        int convertedTemp = 0;

        switch (unit) {
            case "metric":
                convertedTemp = (int) Math.round(temperature - 273.15);
                break;
        }

        return convertedTemp;
    }

}
