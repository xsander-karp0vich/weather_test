package com.example.weather_test.ui.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather_test.R;
import com.example.weather_test.api.pojo.weatherforecast.WeatherList;
import com.example.weather_test.api.response.WeatherResponse;

import java.util.ArrayList;
import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {


    private List<WeatherList> weatherForecast = new ArrayList<>();

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class WeatherViewHolder extends RecyclerView.ViewHolder{

        private final ImageView weatherImageView;
        private final TextView dataTextView;
        private final TextView tempItemTextView;

        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            weatherImageView = itemView.findViewById(R.id.weatherIconImageView);
            dataTextView = itemView.findViewById(R.id.dataTextView);
            tempItemTextView = itemView.findViewById(R.id.tempItemTextView);
        }
    }
}
