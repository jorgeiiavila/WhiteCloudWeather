package com.jorgeiiavila.whitecloudweather;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


/**
 * Created by jorge on 6/16/2017.
 */

public class CurrentWeather extends Weather{

    private final String LOG_TAG = CurrentWeather.class.getSimpleName();
    private final String CURRENTLY = "currently";

    // Weather Parameters
    private double mTemperature;
    private double mApparentTemperature;

    // Empty Constructor
    public CurrentWeather() {
        mTemperature = 0;
        mApparentTemperature = 0;
    }

    // Constructor
    public CurrentWeather(JSONObject weatherData) {

        try {
            // JSON object with the current weather data
            JSONObject currently = weatherData.getJSONObject(CURRENTLY);

            if (weatherData.has("latitude"))
                this.mLatitude = weatherData.getDouble("latitude");
            if (weatherData.has("longitude"))
                this.mLongitude = weatherData.getDouble("longitude");
            if (weatherData.has("timezone"))
                this.mTimeZone = weatherData.getString("timezone");
            if (currently.has("time"))
                this.mTime = currently.getString("time");
            if (currently.has("summary"))
                this.mSummary = currently.getString("summary");
            if (currently.has("icon"))
                this.mIcon = currently.getString("icon");
            if (currently.has("precipIntensity"))
                this.mPrecipIntensity = currently.getDouble("precipIntensity");
            if (currently.has("precipProbability")){
                this.mPrecipProbability = currently.getDouble("precipProbability");
                mPrecipProbability = decimalToPercentage(mPrecipIntensity);
            }
            if (currently.has("temperature"))
                this.mTemperature = currently.getDouble("temperature");
            if (currently.has("apparentTemperature"))
                this.mApparentTemperature = currently.getDouble("apparentTemperature");
            if (currently.has("dewPoint"))
                this.mDewPoint = currently.getDouble("dewPoint");
            if (currently.has("humidity"))
                this.mHumidity = currently.getDouble("humidity");
            if (currently.has("windSpeed"))
                this.mWindSpeed = currently.getDouble("windSpeed");
            if (currently.has("visibility"))
                this.mVisibility = currently.getDouble("visibility");
            if (currently.has("pressure"))
                this.mPressure = currently.getDouble("pressure");
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }

    // Getters
    public double getmTemperature() {
        return mTemperature;
    }

    public double getmApparentTemperature() {
        return mApparentTemperature;
    }

    public void celsiusToFahrenheit(){
        mTemperature = Math.round(convertToFahrenheit(mTemperature));
        mApparentTemperature = Math.round(convertToFahrenheit(mApparentTemperature));
        mDewPoint = Math.round(convertToFahrenheit(mDewPoint));
    }

    public void fahrenheitToCelsius() {
        mTemperature = Math.round(convertToCelsius(mTemperature));
        mApparentTemperature = Math.round(convertToCelsius(mApparentTemperature));
        mDewPoint = Math.round(convertToCelsius(mDewPoint));
    }

    public void unixToHour(){
        mTime = convertToHour(mTime);
    }
}
