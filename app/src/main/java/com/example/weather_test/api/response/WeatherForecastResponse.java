package com.example.weather_test.api.response;

import com.example.weather_test.api.pojo.weatherforecast.WeatherList;

import java.util.ArrayList;

public class WeatherForecastResponse {
    public String cod;
    public int message;
    public int cnt;
    public ArrayList<WeatherList> list;

    public WeatherForecastResponse(String cod, int message, int cnt, ArrayList<WeatherList> list) {
        this.cod = cod;
        this.message = message;
        this.cnt = cnt;
        this.list = list;
    }

    public String getCod() {
        return cod;
    }

    public int getMessage() {
        return message;
    }

    public int getCnt() {
        return cnt;
    }

    public ArrayList<WeatherList> getList() {
        return list;
    }

    @Override
    public String toString() {
        return "WeatherForecatsResponse{" +
                "cod='" + cod + '\'' +
                ", message=" + message +
                ", cnt=" + cnt +
                ", list=" + list +
                '}';
    }
}
