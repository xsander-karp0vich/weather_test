package com.example.weather_test.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.splashscreen.SplashScreen;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.service.autofill.SavedDatasetsInfoCallback;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.widget.Toast;

import com.example.weather_test.R;
import com.example.weather_test.api.pojo.Coordinates;
import com.example.weather_test.api.response.WeatherResponse;
import com.example.weather_test.api.services.ApiFactory;
import com.example.weather_test.ui.viewmodel.ViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    ViewModel viewModel;
    private static int REQUEST_CODE = 100;

    private final String TAG = "TAGTAGTAGTAG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this); //SplashScreen
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(ViewModel.class);

        askPermission(); //calling a method that requests permission

        observeViewModel();
    }

    private void askPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            // If the permission has already been granted, call the method to get the user's current location.
            viewModel.getCurrentUserLocation();

        } else {
            // If permission is not granted, request it from the user.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                viewModel.getCurrentUserLocation();
            }
            else {
               // Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void observeViewModel(){
        viewModel.getCoordinates().observe(this, new Observer<Coordinates<Double, Double>>() {
            @Override
            public void onChanged(Coordinates<Double, Double> coordinates) {
                Log.d(TAG, "LOCATION: "+coordinates.toString());
                viewModel.getWeatherDetails(coordinates);
            }
        });

    }
    private void updateUI(){
        viewModel.getWeatherResponseLiveData().observe(this, new Observer<WeatherResponse>() {
            @Override
            public void onChanged(WeatherResponse weatherResponse) {
                
            }
        });
    }

}