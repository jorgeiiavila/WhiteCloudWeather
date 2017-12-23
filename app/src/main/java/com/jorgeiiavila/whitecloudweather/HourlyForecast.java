package com.jorgeiiavila.whitecloudweather;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by jorge on 6/30/2017.
 */

public class HourlyForecast extends Weather{

    private final String LOG_TAG = HourlyForecast.class.getSimpleName();
    final String HOURLY = "hourly";
    final String DATA = "data";

    // Weather parameters
    private String mHourlySummary;
    private String mHourlyIcon;
    private double mTemperature;
    private double mApparentTemperature;

    public HourlyForecast(JSONObject weatherData, int p) {

        // Extract the JSON objects data into the variables
        try {
            // JSON object with the hourly data
            JSONObject hourly = weatherData.getJSONObject(HOURLY);
            JSONArray hourlyData = hourly.getJSONArray(DATA);
            JSONObject objectInPositionP = hourlyData.getJSONObject(p);

            if (hourly.has("summary"))
                this.mSummary = hourly.getString("summary");
            if (hourly.has("icon"))
                this.mIcon = hourly.getString("icon");
            if (weatherData.has("timezone"))
                this.mTimeZone = weatherData.getString("timezone");
            if (objectInPositionP.has("time"))
                this.mTime = objectInPositionP.getString("time");
            if (objectInPositionP.has("summary"))
                this.mHourlySummary = objectInPositionP.getString("summary");
            if (objectInPositionP.has("icon"))
                this.mHourlyIcon = objectInPositionP.getString("icon");
            if (objectInPositionP.has("precipIntensity"))
                this.mPrecipIntensity = objectInPositionP.getDouble("precipIntensity");
            if (objectInPositionP.has("precipProbability"))
                this.mPrecipProbability = objectInPositionP.getDouble("precipProbability");
            if (objectInPositionP.has("temperature"))
                this.mTemperature = objectInPositionP.getDouble("temperature");
            if (objectInPositionP.has("apparentTemperature"))
                this.mApparentTemperature = objectInPositionP.getDouble("apparentTemperature");
            if (objectInPositionP.has("dewPoint"))
                this.mDewPoint = objectInPositionP.getDouble("dewPoint");
            if (objectInPositionP.has("humidity"))
                this.mHumidity = objectInPositionP.getDouble("humidity");
            if (objectInPositionP.has("windSpeed"))
                this.mWindSpeed = objectInPositionP.getDouble("windSpeed");
            if (objectInPositionP.has("visibility"))
                this.mVisibility = objectInPositionP.getDouble("visibility");
            if (objectInPositionP.has("pressure"))
                this.mPressure = objectInPositionP.getDouble("pressure");

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }

    public String getmHourlySummary() {
        return mHourlySummary;
    }

    public String getmHourlyIcon() {
        return mHourlyIcon;
    }

    public double getmTemperature() {
        return mTemperature;
    }

    public double getmApparentTemperature() {
        return mApparentTemperature;
    }

    public void celsiusToFahrenheit(){
        mTemperature = Math.round(convertToFahrenheit(mTemperature));
        mApparentTemperature = Math.round(convertToFahrenheit(mApparentTemperature));
    }

    public void fahrenheitToCelsius() {
        mTemperature = Math.round(convertToCelsius(mTemperature));
        mApparentTemperature = Math.round(convertToCelsius(mApparentTemperature));
    }

    public void unixToHour(){
        mTime = convertToHour(mTime);
    }
}
