package com.example.weather_test.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.splashscreen.SplashScreen;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.weather_test.R;
import com.example.weather_test.api.pojo.weather.Coordinates;
import com.example.weather_test.api.pojo.weatherforecast.WeatherList;
import com.example.weather_test.api.response.WeatherResponse;
import com.example.weather_test.ui.adapters.WeatherAdapter;
import com.example.weather_test.ui.viewmodel.WeatherViewModel;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    WeatherViewModel viewModel;
    private boolean isViewModelInit = false;
    private static final int REQUEST_CODE = 0;
    private ScrollView mainActivity;
    private TextView currentDataTextView,weatherTypeTextView,
            windTextView,tempTextView,humidityTextView,wind,temp,humidity,todayTextView;
    private EditText currentCityEditText;
    private ImageView weatherImageView,line1,line2;
    private ProgressBar progressBar;
    private WeatherAdapter weatherAdapter;
    private RecyclerView weatherRecycleView;
    private final String TAG = "TAGTAGTAGTAG";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this); //SplashScreen
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        askPermission(); //calling a method that requests permission

        if (isViewModelInit){
            viewModel = new ViewModelProvider(this).get(WeatherViewModel.class);
        }
        updateUI();
    }
    private void initViews(){

        mainActivity = findViewById(R.id.mainActivity);

        currentDataTextView = findViewById(R.id.currentDateTextView);
        currentCityEditText = findViewById(R.id.currentCityEditText);
        weatherTypeTextView = findViewById(R.id.weatherTypeTextView);
        windTextView = findViewById(R.id.windTextView);
        tempTextView = findViewById(R.id.tempTextView);
        humidityTextView = findViewById(R.id.humidityTextView);
        wind = findViewById(R.id.wind);
        temp = findViewById(R.id.temp);
        humidity = findViewById(R.id.humidity);
        progressBar = findViewById(R.id.progressBar);
        todayTextView = findViewById(R.id.todayTextView);

        line1 = findViewById(R.id.line1);
        line2 = findViewById(R.id.line2);

        weatherImageView = findViewById(R.id.weatherImageView);

        weatherAdapter = new WeatherAdapter();

        weatherRecycleView = findViewById(R.id.weatherRecycleView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        weatherRecycleView.setLayoutManager(layoutManager);
    }

    private void askPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            isViewModelInit = true;
        }
        else if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            isViewModelInit = true;

        } else {
            // If permission is not granted, request it from the user.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},REQUEST_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE){
            if (grantResults.length > 0){
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    isViewModelInit = true;
                }
                else if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    isViewModelInit = true;
                }
            }
        }
    }
    private void updateUI(){
        viewModel.getWeatherResponseLiveData().observe(this, new Observer<WeatherResponse>() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onChanged(WeatherResponse weatherResponse) {
                if (weatherResponse!=null){
                    viewModel.getIsLoading().observe(MainActivity.this, new Observer<Boolean>() {
                        @Override
                        public void onChanged(Boolean isLoading) {
                            if (isLoading) {
                                progressBar.setVisibility(View.VISIBLE);
                                wind.setVisibility(View.GONE);
                                temp.setVisibility(View.GONE);
                                humidity.setVisibility(View.GONE);
                                line1.setVisibility(View.GONE);
                                line2.setVisibility(View.GONE);
                                todayTextView.setVisibility(View.GONE);
                                currentCityEditText.setVisibility(View.GONE);
                            } else {
                                progressBar.setVisibility(View.GONE);
                                wind.setVisibility(View.VISIBLE);
                                temp.setVisibility(View.VISIBLE);
                                humidity.setVisibility(View.VISIBLE);
                                line1.setVisibility(View.VISIBLE);
                                line2.setVisibility(View.VISIBLE);
                                todayTextView.setVisibility(View.VISIBLE);
                                currentCityEditText.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                    viewModel.getWeatherList().observe(MainActivity.this, new Observer<List<WeatherList>>() {
                        @Override
                        public void onChanged(List<WeatherList> weatherLists) {
                            weatherAdapter.setWeatherForecast(weatherLists);
                        }
                    });

                    //formats:
                    DecimalFormat decimalFormat = new DecimalFormat("#.#");
                    DecimalFormat decimalFormat1 = new DecimalFormat("#");
                    //temp:
                    double tempKelvin = weatherResponse.getMain().getTemp();
                    double tempDouble = (tempKelvin-273);
                    String formattedTemp = decimalFormat1.format(tempDouble);
                    String temp = formattedTemp +"°C";
                    tempTextView.setText(temp);

                    //wind speed:
                    double windDouble = weatherResponse.getWind().getSpeed();
                    String formattedWind = decimalFormat1.format(windDouble);
                    String wind = formattedWind + " m/s";
                    windTextView.setText(wind);
                    //humidity:
                    double humidityDouble = weatherResponse.getMain().getHumidity();
                    String formattedHumidity = decimalFormat1.format(humidityDouble);
                    String humidity = formattedHumidity + " %";
                    humidityTextView.setText(humidity);
                    //city:
                    String cityString = weatherResponse.getName().toUpperCase();
                    currentCityEditText.setHint(cityString);
                    //currentData:
                    Date currentDate = new Date();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM", Locale.ENGLISH);
                    String formattedDate = dateFormat.format(currentDate);
                    String todayDate = "Today, " + formattedDate;
                    currentDataTextView.setText(todayDate);
                    //id
                    int id = weatherResponse.getWeather()[0].getId();

                    //thunderstorm
                    if (id >= 200 && id <= 232){
                        Drawable drawable = getResources().getDrawable(R.drawable.weather_type_thunderstorm);
                        weatherImageView.setImageDrawable(drawable);
                        String type = "It's a thunderstorm!";
                        weatherTypeTextView.setText(type);

                        mainActivity.setBackgroundResource(R.drawable.heavy_rain_bg);

                        currentCityEditText.setBackgroundResource(R.drawable.custom_edit_text_haze);

                        Window window = getWindow();
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(getResources().getColor(R.color.startColorHeavyRainBg));
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setNavigationBarColor(getResources().getColor(R.color.endColorHeavyRainBg));
                    }
                    //drizzle:
                    if (id >= 300 && id <= 321){
                        Drawable drawable = getResources().getDrawable(R.drawable.weather_type_drizzle);
                        weatherImageView.setImageDrawable(drawable);
                        String type = "It's light rain!";
                        weatherTypeTextView.setText(type);

                        mainActivity.setBackgroundResource(R.drawable.rainy_bg);
                        currentCityEditText.setBackgroundResource(R.drawable.custom_edit_text_rainy);

                        Window window = getWindow();
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(getResources().getColor(R.color.startColorRainyBg));
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setNavigationBarColor(getResources().getColor(R.color.endColorRainyBg));
                    }
                    //rainy:
                    if (id >= 500 && id <= 504){
                        Drawable drawable = getResources().getDrawable(R.drawable.weather_type_rain1);
                        weatherImageView.setImageDrawable(drawable);
                        String type = "It's rainy!";
                        weatherTypeTextView.setText(type);

                        mainActivity.setBackgroundResource(R.drawable.rainy_bg);
                        currentCityEditText.setBackgroundResource(R.drawable.custom_edit_text_rainy);

                        Window window = getWindow();
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(getResources().getColor(R.color.startColorRainyBg));
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setNavigationBarColor(getResources().getColor(R.color.endColorRainyBg));
                    }
                    //heavyRainy:
                    if (id >= 504 && id <= 531){
                        Drawable drawable = getResources().getDrawable(R.drawable.weather_type_heavy_rain);
                        weatherImageView.setImageDrawable(drawable);
                        String type = "It's heavy rain!";
                        weatherTypeTextView.setText(type);

                        mainActivity.setBackgroundResource(R.drawable.heavy_rain_bg);
                        currentCityEditText.setBackgroundResource(R.drawable.custom_edit_text_haze);

                        Window window = getWindow();
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(getResources().getColor(R.color.startColorHeavyRainBg));
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setNavigationBarColor(getResources().getColor(R.color.endColorHeavyRainBg));
                    }
                    //snowy:
                    if (id >= 600 && id <= 622){
                        Drawable drawable = getResources().getDrawable(R.drawable.weather_type_snow);
                        weatherImageView.setImageDrawable(drawable);
                        String type = "It's snowy!";
                        weatherTypeTextView.setText(type);

                        mainActivity.setBackgroundResource(R.drawable.snowy_bg);
                        currentCityEditText.setBackgroundResource(R.drawable.custom_edit_text_snowy);


                        Window window = getWindow();
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(getResources().getColor(R.color.startColorSnowyBg));
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setNavigationBarColor(getResources().getColor(R.color.endColorSnowyBg));
                    }
                    //foggy:
                    if (id >= 701 && id <= 781){
                        Drawable drawable = getResources().getDrawable(R.drawable.weather_type_mist);
                        weatherImageView.setImageDrawable(drawable);
                        String type = "It's foggy!";
                        weatherTypeTextView.setText(type);

                        mainActivity.setBackgroundResource(R.drawable.heavy_rain_bg);
                        currentCityEditText.setBackgroundResource(R.drawable.custom_edit_text_haze);


                        Window window = getWindow();
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(getResources().getColor(R.color.startColorHeavyRainBg));
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setNavigationBarColor(getResources().getColor(R.color.endColorHeavyRainBg));
                    }
                    //clearSky:
                    if (id == 800){
                        Drawable drawable = getResources().getDrawable(R.drawable.weather_type_sunny);
                        weatherImageView.setImageDrawable(drawable);
                        String type = "It's sunny!";
                        weatherTypeTextView.setText(type);

                        currentCityEditText.setBackgroundResource(R.drawable.custom_edit_text_main);

                        Window window = getWindow();
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(getResources().getColor(R.color.startColorMainBg));
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setNavigationBarColor(getResources().getColor(R.color.endColorMainBg));

                    }
                    //cloudy:
                    if (id >= 801 && id <= 804){
                        Drawable drawable = getResources().getDrawable(R.drawable.weather_type_cloudy);
                        weatherImageView.setImageDrawable(drawable);
                        String type = "It's cloudy!";
                        weatherTypeTextView.setText(type);

                        mainActivity.setBackgroundResource(R.drawable.rainy_bg);

                        currentCityEditText.setBackgroundResource(R.drawable.custom_edit_text_rainy);


                        Window window = getWindow();
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(getResources().getColor(R.color.startColorRainyBg));
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setNavigationBarColor(getResources().getColor(R.color.endColorRainyBg));

                    }
                }

            }
        });
        weatherRecycleView.setAdapter(weatherAdapter);
    }
}