package com.jorgeiiavila.whitecloudweather;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by jorge on 9/16/2017.
 */

public abstract class Weather {

    String mTimeZone;
    double mLatitude;
    double mLongitude;
    String mTime;
    String mSummary;
    String mIcon;
    double mPrecipIntensity;
    double mPrecipProbability;
    double mDewPoint;
    double mHumidity;
    double mWindSpeed;
    double mVisibility;
    double mPressure;

    abstract void celsiusToFahrenheit();
    abstract void fahrenheitToCelsius();
    abstract void unixToHour();

    protected double convertToCelsius(double fahrenheit) {
        return (fahrenheit - 32) / 1.8;
    }

    protected double convertToFahrenheit(double celsius) {
        return celsius * 1.8 + 32;
    }

    protected double decimalToPercentage(double decimal) { return decimal * 100; }

    protected String convertToHour(String time) {
        String currentHour;
        long dv = Long.valueOf(time) * 1000;// its need to be in milliseconds
        Date df = new java.util.Date(dv);
        SimpleDateFormat hourFormat = new SimpleDateFormat("h:mma");
        hourFormat.setTimeZone(TimeZone.getTimeZone(mTimeZone));
        currentHour = hourFormat.format(df);
        return currentHour;
    }

    protected String convertToDate(String time) {
        String currentDate;
        long dv = Long.valueOf(time) * 1000;// its need to be in milisecond
        Date df = new java.util.Date(dv);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE");
        dateFormat.setTimeZone(TimeZone.getTimeZone(mTimeZone));
        currentDate = dateFormat.format(df);
        return currentDate;
    }

    public String getmTimeZone() {
        return mTimeZone;
    }

    public double getmLatitude() {
        return mLatitude;
    }

    public double getmLongitude() {
        return mLongitude;
    }

    public String getmTime() {
        return mTime;
    }

    public String getmSummary() {
        return mSummary;
    }

    public String getmIcon() {
        return mIcon;
    }

    public double getmPrecipIntensity() {
        return mPrecipIntensity;
    }

    public double getmPrecipProbability() {
        return mPrecipProbability;
    }

    public double getmDewPoint() {
        return mDewPoint;
    }

    public double getmHumidity() {
        return mHumidity;
    }

    public double getmWindSpeed() {
        return mWindSpeed;
    }

    public double getmVisibility() {
        return mVisibility;
    }

    public double getmPressure() {
        return mPressure;
    }
}
