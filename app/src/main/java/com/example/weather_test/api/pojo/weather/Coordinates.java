package com.example.weather_test.api.pojo.weather;

public class Coordinates<lat, lon> {
    private lat latitude;
    private lon longitude;

    public Coordinates(lat latitude, lon longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public lat getLatitude() {
        return latitude;
    }

    public lon getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
