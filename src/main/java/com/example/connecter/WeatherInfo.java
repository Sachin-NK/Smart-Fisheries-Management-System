package com.example.connecter;

public class WeatherInfo {
    private String city;
    private double temperature;
    private String description;

    public WeatherInfo(String city, double temperature, String description) {
        this.city = city;
        this.temperature = temperature;
        this.description = description;
    }

    public String getCity() {
        return city;
    }

    public double getTemperature() {
        return temperature;
    }

    public String getDescription() {
        return description;
    }
}
