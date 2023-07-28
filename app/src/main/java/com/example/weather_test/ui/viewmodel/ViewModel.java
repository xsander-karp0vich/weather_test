package com.example.weather_test.ui.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;

import com.example.weather_test.api.response.WeatherResponse;
import com.example.weather_test.api.services.ApiFactory;
import com.example.weather_test.ui.activity.MainActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ViewModel extends AndroidViewModel {
    private String TAG = "TAGTAGTAGTAG";
    private final String API_KEY = "0ae590706091ef379c1aaeb379d4dad8";
    FusedLocationProviderClient fusedLocationProviderClient;
    private MutableLiveData<Double> latitude = new MutableLiveData<>();
    private MutableLiveData<Double> longitude = new MutableLiveData<>();
    public LiveData<Double> getLatitude() {
        return latitude;
    }
    public LiveData<Double> getLongitude() {
        return longitude;
    }

    public ViewModel(@NonNull Application application) {
        super(application);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(application);

    }
    @SuppressLint("CheckResult")
    public void getWeatherDetails(double lat, double lon){
        ApiFactory.apiService.loadWeather(lat,lon,API_KEY)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherResponse>() {
                    @Override
                    public void accept(WeatherResponse weatherResponse) throws Throwable {
                        Log.d(TAG, "accept: "+ weatherResponse.toString());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d(TAG, throwable.toString());
                    }
                });
    }

}
