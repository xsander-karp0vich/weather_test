package com.example.weather_test.api.services;

import com.example.weather_test.api.response.WeatherForecastResponse;
import com.example.weather_test.api.response.WeatherResponse;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {


    @GET("weather")
    Single<WeatherResponse> loadWeather(@Query("lat") double lat, @Query("lon") double lon, @Query("appid") String apiKey);

    @GET("forecast")
    Single<WeatherForecastResponse> loadWeatherForecast(@Query("lat") double lat, @Query("lon") double lon,@Query("cnt") int cnt, @Query("appid") String apiKey);

    @GET("weather")
    Single<WeatherResponse> loadWeatherByCityName();
}
