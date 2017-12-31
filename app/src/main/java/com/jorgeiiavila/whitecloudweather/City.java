package com.jorgeiiavila.whitecloudweather;


import java.util.ArrayList;

/**
 * Created by jorge on 6/30/2017.
 */

public class City {

    private CurrentWeather currentWeather;
    private ArrayList<HourlyForecast> hourlyForecasts;
    private ArrayList<DailyForecast> dailyForecasts;

    // Empty Constructor
    public City() {

    }

    // Constructor with parameters
    public City(CurrentWeather currentWeather, ArrayList<HourlyForecast> hourlyForecasts, ArrayList<DailyForecast> dailyForecasts) {
        this.currentWeather = currentWeather;
        this.hourlyForecasts = hourlyForecasts;
        this.dailyForecasts = dailyForecasts;
    }

    // Setters and Getters
    public CurrentWeather getCurrentWeather() {
        return currentWeather;
    }

    public ArrayList<HourlyForecast> getHourlyForecasts() {
        return hourlyForecasts;
    }

    public ArrayList<DailyForecast> getDailyForecasts() {
        return dailyForecasts;
    }
}
