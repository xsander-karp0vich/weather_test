package com.example.weather_test.api.pojo.weatherforecast;

import com.example.weather_test.api.pojo.weather.Main;
import com.example.weather_test.api.pojo.weather.Weather;

import java.util.*;

public class WeatherList {
    public int dt;
    public MainForecast mainForecast;
    public ArrayList<Weather> weather;
    public String dt_txt;


    public WeatherList(int dt, MainForecast mainForecast, ArrayList<Weather> weather, String dt_txt) {
        this.dt = dt;
        this.mainForecast = mainForecast;
        this.weather = weather;
        this.dt_txt = dt_txt;
    }

    public int getDt() {
        return dt;
    }

    public MainForecast getMainForecast() {
        return mainForecast;
    }

    public ArrayList<Weather> getWeather() {
        return weather;
    }

    public String getDt_txt() {
        return dt_txt;
    }

    @Override
    public String toString() {
        return "WeatherList{" +
                "dt=" + dt +
                ", mainForecast=" + mainForecast +
                ", weather=" + weather +
                ", dt_txt='" + dt_txt + '\'' +
                '}';
    }
}
