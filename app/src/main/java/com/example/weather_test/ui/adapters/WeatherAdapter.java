package com.example.weather_test.ui.adapters;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather_test.R;
import com.example.weather_test.api.pojo.weatherforecast.WeatherList;
import com.example.weather_test.api.response.WeatherResponse;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {

    private static final String TAG = "DAGDAGDAG";

    private List<WeatherList> weatherForecast = new ArrayList<>();

    public void setWeatherForecast(List<WeatherList> weatherForecast) {
        this.weatherForecast = weatherForecast;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.weather_item,
                parent,
                false);
        return new WeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        WeatherList weather = weatherForecast.get(position);

        double id = weather.getWeather().get(0).getId();

        double tempKelvin = weather.getMain().getTemp();
        double tempDouble = (tempKelvin-273);
        DecimalFormat decimalFormat1 = new DecimalFormat("#");
        String formattedTemp = decimalFormat1.format(tempDouble);
        String temp = formattedTemp +"°C";

        holder.tempItemTextView.setText(temp);

        String dataTimeString = weather.dt_txt;
        holder.dataTextView.setText(formatDataTime(dataTimeString));

        //clear sky
        if (id == 800){
            holder.weatherImageView.setImageResource(R.drawable.weather_type_sunny);
            holder.constraintLayout.setBackgroundResource(R.color.main_bg_weather_item);
        }
        //thunderstorm
        if (id > 200 && id < 232){
            holder.weatherImageView.setImageResource(R.drawable.weather_type_thunderstorm);
            holder.constraintLayout.setBackgroundResource(R.color.startColorHeavyRainBg);
        }
        //drizzle:
        if (id > 300 && id < 321){
            holder.weatherImageView.setImageResource(R.drawable.weather_type_drizzle);
            holder.constraintLayout.setBackgroundResource(R.color.startColorRainyBg);
        }
        //rainy:
        if (id > 500 && id < 504){
            holder.weatherImageView.setImageResource(R.drawable.weather_type_rain1);
            holder.constraintLayout.setBackgroundResource(R.color.startColorRainyBg);
        }
        //heavyRainy:
        if (id > 504 && id < 531){
            holder.weatherImageView.setImageResource(R.drawable.weather_type_heavy_rain);
            holder.constraintLayout.setBackgroundResource(R.color.startColorHeavyRainBg);
        }
        //snowy:
        if (id > 600 && id < 622){
            holder.weatherImageView.setImageResource(R.drawable.weather_type_snow);
            holder.constraintLayout.setBackgroundResource(R.color.startColorSnowyBg);
        }
        //foggy:
        if (id > 701 && id < 781){
            holder.weatherImageView.setImageResource(R.drawable.weather_type_mist);
            holder.constraintLayout.setBackgroundResource(R.color.startColorHeavyRainBg);
        }
        //cloudy:
        if (id > 801 && id < 804){
            holder.weatherImageView.setImageResource(R.drawable.weather_type_cloudy);
            holder.constraintLayout.setBackgroundResource(R.color.startColorHeavyRainBg);
        }

    }
    public static String formatDataTime(String dataTimeString){
        try{
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            Date date = inputFormat.parse(dataTimeString);

            SimpleDateFormat outputFormat = new SimpleDateFormat("HH a", Locale.ENGLISH);
            return outputFormat.format(date);
        } catch (ParseException e){
            Log.d(TAG, "formatDataTime: "+e);
        }
        return "";
    }

    @Override
    public int getItemCount() {
        return weatherForecast.size();
    }

    static class WeatherViewHolder extends RecyclerView.ViewHolder{

        private final ImageView weatherImageView;
        private final TextView dataTextView;
        private final TextView tempItemTextView;
        private final ConstraintLayout constraintLayout;

        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            weatherImageView = itemView.findViewById(R.id.weatherIconImageView);
            dataTextView = itemView.findViewById(R.id.dataTextView);
            tempItemTextView = itemView.findViewById(R.id.tempItemTextView);
            constraintLayout = itemView.findViewById(R.id.constraintlayout);
        }
    }
}
