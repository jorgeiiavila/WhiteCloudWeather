package com.jorgeiiavila.whitecloudweather;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by jorge on 6/30/2017.
 */

public class City implements Parcelable{

    private CurrentWeather currentWeather;
    private ArrayList<HourlyForecast> hourlyForecasts;
    private ArrayList<DailyForecast> dailyForecasts;

    private String mCity;
    private String mCountry;

    // Empty Constructor
    public City() {

    }

    // Constructor with parameters
    public City(CurrentWeather currentWeather, ArrayList<HourlyForecast> hourlyForecasts, ArrayList<DailyForecast> dailyForecasts, String city, String country) {
        this.currentWeather = currentWeather;
        this.hourlyForecasts = hourlyForecasts;
        this.dailyForecasts = dailyForecasts;
        mCity = city;
        mCountry = country;
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

    public String getmCity() {
        return mCity;
    }

    public void setmCity(String mCity) {
        this.mCity = mCity;
    }

    public String getmCountry() {
        return mCountry;
    }

    public void setmCountry(String mCountry) {
        this.mCountry = mCountry;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeList(hourlyForecasts);
    }

    /** Static field used to regenerate object, individually or as arrays */
    public static final Parcelable.Creator<City> CREATOR = new Parcelable.Creator<City>() {
        public City createFromParcel(Parcel pc) {
            return new City(pc);
        }
        public City[] newArray(int size) {
            return new City[size];
        }
    };

    /**Ctor from Parcel, reads back fields IN THE ORDER they were written */
    public City(Parcel pc){
//        id         = pc.readLong();
//        age        =  pc.readInt();
//        phone      = pc.readString();
//        registered = ( pc.readInt() == 1 );
    }
}
