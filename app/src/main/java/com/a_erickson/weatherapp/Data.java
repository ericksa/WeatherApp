package com.a_erickson.weatherapp;

import java.util.List;

// GSON will convert JSON data to a Data object
class Data {
    // Base address for icon download
    final static String ICON_ADDR = "http://openweathermap.org/img/w/";
    final static String BASE_ADDR = "http://api.openweathermap.org/data/2.5/weather";
    final static String APP_ID = ",us&units=metric&APPID=382c108de9814dc1715cd1145b8875d6";
    List<Weather> weather;
    Main main;
    String name;

    // A method for Celsius
//    String getTemperatureInCelsius() {
//        float temp = main.temp;
//        return String.format("%.2f", temp);
//    }
    String getTemperatureInFahrenheit() {
        float temp = (((main.temp * 9) / 5) + 32);
        return String.format("%.2f", temp);
    }

    // getIconAddress concatenates the base address and the specific code for
    // the icon
    public String getIconAddress() {
        return ICON_ADDR + weather.get(0).icon + ".png";
    }

    public String getDescription() {
        if (weather != null && weather.size() > 0)
            return weather.get(0).description;
        return null;
    }

    static class Weather {
        String description;
        String icon;
    }

    static class Main {
        float temp;
    }
}