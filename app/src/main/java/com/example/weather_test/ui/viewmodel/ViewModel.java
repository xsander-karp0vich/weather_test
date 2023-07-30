package com.example.weather_test.ui.viewmodel;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;

import com.example.weather_test.api.pojo.Coordinates;
import com.example.weather_test.api.response.WeatherResponse;
import com.example.weather_test.api.services.ApiFactory;
import com.example.weather_test.ui.activity.MainActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Response;

public class ViewModel extends AndroidViewModel {
    private String TAG = "TAGTAGTAGTAG";
    private final String API_KEY = "0ae590706091ef379c1aaeb379d4dad8";
    FusedLocationProviderClient fusedLocationProviderClient;

    private MutableLiveData<WeatherResponse> weatherResponseLiveData = new MutableLiveData<>();
    private MutableLiveData<Coordinates<Double,Double>> coordinates = new MutableLiveData<>();
    public LiveData<Coordinates<Double, Double>> getCoordinates() {
        return coordinates;
    }

    public MutableLiveData<WeatherResponse> getWeatherResponseLiveData() {
        return weatherResponseLiveData;
    }

    public ViewModel(@NonNull Application application) {
        super(application);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(application);
    }

    public void getCurrentUserLocation(){
        if (ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                Geocoder geocoder = new Geocoder(getApplication(), Locale.getDefault());
                                try {
                                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                                    double lat = addresses.get(0).getLatitude();
                                    double lon = addresses.get(0).getLongitude();

                                    coordinates.setValue(new Coordinates<>(lat,lon));

                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }

                            }
                        }
                    });
        }


    }

    @SuppressLint("CheckResult")
    public void getWeatherDetails(Coordinates coordinates){
        double lat = (double) coordinates.getLatitude();
        double lon = (double) coordinates.getLongitude();


        ApiFactory.apiService.loadWeather(lat,lon,API_KEY)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherResponse>() {
                    @Override
                    public void accept(WeatherResponse weatherResponse) throws Throwable {
                        Log.d(TAG, "REQUEST: "+ weatherResponse.toString());
                        weatherResponseLiveData.setValue(weatherResponse);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d(TAG, throwable.toString());
                    }
                });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
