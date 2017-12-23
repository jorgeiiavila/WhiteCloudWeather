package com.jorgeiiavila.whitecloudweather;

import android.content.Context;
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

public class DailyForecast extends Weather{

    private final String LOG_TAG = DailyForecast.class.getSimpleName();
    final String DATA = "data";
    final String DAILY = "daily";

    private String mDailySummary;
    private String mDailyIcon;
    private String mSunriseTime;
    private String mSunsetTime;
    private double mMoonPhase;
    private double mTemperatureMin;
    private double mTemperatureMax;

    // Empty Constructor
    public DailyForecast() {
    }

    public DailyForecast(JSONObject weatherData, int p) {

        // Extract the JSON array with each day data
        try{
            // JSON object with the daily data
            JSONObject daily = weatherData.getJSONObject(DAILY);
            JSONArray dailyData = daily.getJSONArray(DATA);
            JSONObject objectInPositionP = dailyData.getJSONObject(p);

            if (weatherData.has("timezone"))
                this.mTimeZone = weatherData.getString("timezone");
            if (daily.has("summary"))
                this.mSummary = daily.getString("summary");
            if (daily.has("icon"))
                this.mIcon = daily.getString("icon");
            if (objectInPositionP.has("time")){
                this.mTime = objectInPositionP.getString("time");
            }
            if (objectInPositionP.has("summary"))
                this.mDailySummary = objectInPositionP.getString("summary");
            if (objectInPositionP.has("icon"))
                this.mDailyIcon = objectInPositionP.getString("icon");
            if (objectInPositionP.has("sunriseTime")){
                this.mSunriseTime = objectInPositionP.getString("sunriseTime");
            }
            if (objectInPositionP.has("sunsetTime")){
                this.mSunsetTime = objectInPositionP.getString("sunsetTime");
            }
            if (objectInPositionP.has("moonPhase"))
                this.mMoonPhase = objectInPositionP.getDouble("moonPhase");
            if (objectInPositionP.has("precipIntensity"))
                this.mPrecipIntensity = objectInPositionP.getDouble("precipIntensity");
            if (objectInPositionP.has("precipProbability"))
                this.mPrecipProbability = objectInPositionP.getDouble("precipProbability");
            if (objectInPositionP.has("temperatureMin"))
                this.mTemperatureMin = objectInPositionP.getDouble("temperatureMin");
            if (objectInPositionP.has("temperatureMax"))
                this.mTemperatureMax = objectInPositionP.getDouble("temperatureMax");
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

        }catch (JSONException e){
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }

    // Getters
    public String getmDailySummary() {
        return mDailySummary;
    }

    public String getmDailyIcon() {
        return mDailyIcon;
    }

    public String getmSunriseTime() {
        return mSunriseTime;
    }

    public String  getmSunsetTime() {
        return mSunsetTime;
    }

    public double getmMoonPhase() {
        return mMoonPhase;
    }

    public double getmTemperatureMin() {
        return mTemperatureMin;
    }

    public double getmTemperatureMax() {
        return mTemperatureMax;
    }

    public void celsiusToFahrenheit(){
        mTemperatureMin = Math.round(convertToFahrenheit(mTemperatureMin));
        mTemperatureMax = Math.round(convertToFahrenheit(mTemperatureMax));
        mDewPoint = Math.round(convertToFahrenheit(mDewPoint));
    }

    public void fahrenheitToCelsius(){
        mTemperatureMin = Math.round(convertToCelsius(mTemperatureMin));
        mTemperatureMax = Math.round(convertToCelsius(mTemperatureMax));
        mDewPoint = Math.round(convertToCelsius(mDewPoint));
    }

    public void unixToHour(){
        mTime = convertToDate(mTime);
        mSunriseTime = convertToHour(mSunriseTime);
        mSunsetTime = convertToHour(mSunsetTime);
    }
}
