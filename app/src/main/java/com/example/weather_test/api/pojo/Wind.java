package com.example.weather_test.api.pojo;

import com.google.gson.annotations.SerializedName;

public class Wind {
    @SerializedName("speed")

    private double speed;

    public Wind(double speed) {
        this.speed = speed;
    }

    public double getSpeed() {
        return speed;
    }

    @Override
    public String toString() {
        return "Wind{" +
                "speed=" + speed +
                '}';
    }
}
