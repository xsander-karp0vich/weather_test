package com.example.weather_test.ui.viewmodel;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.weather_test.api.pojo.weather.Coordinates;
import com.example.weather_test.api.pojo.weatherforecast.WeatherList;
import com.example.weather_test.api.response.WeatherForecastResponse;
import com.example.weather_test.api.response.WeatherResponse;
import com.example.weather_test.api.services.ApiFactory;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class WeatherViewModel extends AndroidViewModel {
    private String TAG = "TAGTAGTAGTAG";
    private final String API_KEY = "0ae590706091ef379c1aaeb379d4dad8";
    FusedLocationProviderClient fusedLocationProviderClient;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    //LiveData:
    private MutableLiveData<WeatherResponse> weatherResponseLiveData = new MutableLiveData<>();
    private MutableLiveData<List<WeatherList>> weatherList = new MutableLiveData<>();
    private MutableLiveData<Coordinates<Double,Double>> coordinates = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    //Getters:
    public LiveData<Coordinates<Double, Double>> getCoordinates() {
        return coordinates;
    }
    public MutableLiveData<WeatherResponse> getWeatherResponseLiveData() {
        return weatherResponseLiveData;
    }

    public MutableLiveData<List<WeatherList>> getWeatherList() {
        return weatherList;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public WeatherViewModel(@NonNull Application application) {
        super(application);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(application);
        getCurrentUserLocation();
    }
    @SuppressLint("CheckResult")
    public void getWeatherDetails(Coordinates coordinates){
        double lat = (double) coordinates.getLatitude();
        double lon = (double) coordinates.getLongitude();

        Disposable currentWeather = ApiFactory.apiService.loadWeather(lat,lon,API_KEY)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Throwable {
                        isLoading.setValue(true);
                    }
                })
                .doAfterTerminate(new Action() {
                    @Override
                    public void run() throws Throwable {
                        isLoading.setValue(false);
                    }
                })
                .subscribe(new Consumer<WeatherResponse>() {
                    @Override
                    public void accept(WeatherResponse weatherResponse) throws Throwable {
                        //Log.d(TAG, "REQUEST: "+ weatherResponse.toString());
                        weatherResponseLiveData.setValue(weatherResponse);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        // Log.d(TAG, throwable.toString());
                    }
                });
        compositeDisposable.add(currentWeather);

        Disposable weatherForecast = ApiFactory.apiService.loadWeatherForecast(lat,lon,5,API_KEY)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherForecastResponse>() {
                    @Override
                    public void accept(WeatherForecastResponse weatherForecastResponse) throws Throwable {
                        Log.d(TAG, weatherForecastResponse.toString());
                        List<WeatherList> loadedWeather = weatherList.getValue();
                        if (loadedWeather!=null){
                            loadedWeather.addAll(weatherForecastResponse.getList());
                        } else {
                            weatherList.setValue(weatherForecastResponse.getList());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d(TAG, ""+throwable.getMessage());
                    }
                });
        compositeDisposable.add(weatherForecast);
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
                                    if (coordinates!=null){
                                        getWeatherDetails(Objects.requireNonNull(coordinates.getValue()));
                                    }
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }

                            }
                        }
                    });
        }
        else if (ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED){
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
                                    if (coordinates!=null){
                                        getWeatherDetails(Objects.requireNonNull(coordinates.getValue()));
                                    }
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }

                            }
                        }
                    });
        }
    }



    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
