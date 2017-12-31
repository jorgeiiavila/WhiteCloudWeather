package com.jorgeiiavila.whitecloudweather;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static android.support.v4.content.ContextCompat.getColor;


public class ForecastFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    // Code for the Google Places API
    private final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    String mCity, mCountry;

    // Object containing all the data of the current city
    City currentCity;

    SharedPreferences sharedPreferences;

    SwipeRefreshLayout mSwipeRefreshLayout;
    CardView mCurrentData;
    CardView mNextHours;
    CardView mNextDays;
    CardView mOtherData;
    // Views of the main fragment
    private TextView mTemperature;
    private TextView mCurrentCity;
    private TextView mCurrentCountry;
    private TextView mApparentTemperature;
    private TextView mRainProbability;
    private TextView mMaxTemperature;
    private TextView mMinTemperature;
    private TextClock mHour;
    private TextView mWindSpeed;
    private TextView mHumidity;
    private TextView mSunrise;
    private TextView mPressure;
    private TextView mDewPoint;
    private TextView mSunset;
    private TextView mMoonPhase;
    private ImageView mWeatherIcon;

    public ForecastFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        setHasOptionsMenu(true);

        if (savedInstanceState != null) {
            Gson gson = new Gson();
            String city = savedInstanceState.getString("city");
            currentCity = gson.fromJson(city, City.class);
            mCity = savedInstanceState.getString("city_name");
            mCountry = savedInstanceState.getString("country_name");
        }

        // SwipeRefreshLayout
        mSwipeRefreshLayout = rootView.findViewById(R.id.main_fragment_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Gson gson = new Gson();
        String city = gson.toJson(currentCity);
        outState.putString("city", city);
        outState.putString("city_name", mCity);
        outState.putString("country_name", mCountry);
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (currentCity != null) {
            updateWeatherInfo();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        String city = sharedPreferences.getString("city", null);
        mCity = sharedPreferences.getString("city_name", null);
        mCountry = sharedPreferences.getString("country_name", null);
        if (city != null) {
            Gson gson = new Gson();
            currentCity = gson.fromJson(city, City.class);
            updateWeatherInfo();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        Gson gson = new Gson();
        String city = gson.toJson(currentCity);
        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("city", city);
        editor.putString("city_name", mCity);
        editor.putString("country_name", mCountry);
        editor.apply();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.forecast_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings){
            startActivity(new Intent(getActivity(), SettingsActivity.class));
            return true;
        }

        if (id == R.id.action_add_city){
            try {
                Intent intent =
                        new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                .build(getActivity());
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
            } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                // TODO: Handle the error.
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Do something when the city is selected on the AutoComplete Activity
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place currentPlace = PlaceAutocomplete.getPlace(getActivity(), data);

                // Original String returned by the Place Object
                String place = currentPlace.getLatLng().toString();

                // String containing the coordinates with a comma
                String coordinates = place.substring(10, place.length() - 1);

                // String array with the lat and lon on each position
                String[] LatLon = coordinates.split(",");

                Log.i(TAG, "Place: " + currentPlace.getName() + " Name " + currentPlace.getAddress());

                String address = currentPlace.getAddress().toString();
                if (address.indexOf(',') != -1){
                    String[] splittedAddress = address.split(",");
                    mCity = splittedAddress[0];
                    mCountry = splittedAddress[1].substring(1);
                } else {
                    mCity = address;
                    mCountry = " ";
                }

                // Execute task with the coordinates from the selected city
                new FetchWeatherTask().execute(LatLon[0], LatLon[1]);

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    // Update the main fragment views with the information of the CurrentCity Object
    private void updateWeatherInfo() {
        java.text.DecimalFormat format = new java.text.DecimalFormat("0");

        initViews();

        CurrentWeather currentWeather = currentCity.getCurrentWeather();
        DailyForecast dailyForecast = currentCity.getDailyForecasts().get(0);

        mCurrentCountry.setText(mCountry);
        mCurrentCity.setText(mCity);
        mTemperature.setText(format.format(currentWeather.getmTemperature()) + "°");
        mApparentTemperature.setText("Feels like " + format.format(currentWeather.getmApparentTemperature()) + "°");
        mRainProbability.setText(format.format(currentWeather.getmPrecipProbability()) + "%");
        mMinTemperature.setText(format.format(dailyForecast.getmTemperatureMin()) + "°");
        mMaxTemperature.setText(format.format(dailyForecast.getmTemperatureMax()) + "°");
        mHour.setTimeZone(currentWeather.getmTimeZone());
        mWindSpeed.setText(format.format(currentWeather.getmWindSpeed()) + " Mph");
        mHumidity.setText(format.format(currentWeather.getmHumidity()) + "%");
        mSunrise.setText(dailyForecast.getmSunriseTime());
        mPressure.setText(format.format(currentWeather.getmPressure()) + " mb");
        mDewPoint.setText(format.format(currentWeather.getmDewPoint()) + "°");
        mSunset.setText(dailyForecast.getmSunsetTime());
        mMoonPhase.setText(moonPhase(dailyForecast.getmMoonPhase()));

        // Recycler View variables
        RecyclerView mHourlyForecast = getActivity().findViewById(R.id.hourly_forecast_recycler_view);
        ;
        RecyclerView mDailyForecast = getActivity().findViewById(R.id.daily_forecast_recycler_view);
        ;
        RecyclerView.LayoutManager mDailyLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        ;
        RecyclerView.LayoutManager mHourlyLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mDailyForecast.setHasFixedSize(true);
        mHourlyForecast.setHasFixedSize(true);
        mDailyForecast.setLayoutManager(mDailyLayoutManager);
        mHourlyForecast.setLayoutManager(mHourlyLayoutManager);
        mDailyForecast.setAdapter(new ForecastAdapter(currentCity, 'd'));
        mHourlyForecast.setAdapter(new ForecastAdapter(currentCity, 'h'));

        String icon = currentCity.getCurrentWeather().getmIcon();

        switch (icon) {
            case "clear-day":
                changeBackgroundColor(R.color.clear_day);
                mWeatherIcon.setImageResource(R.drawable.weather_clear);
                break;
            case "clear-night":
                changeBackgroundColor(R.color.clear_night);
                mWeatherIcon.setImageResource(R.drawable.weather_clear_night);
                break;
            case "rain":
                changeBackgroundColor(R.color.rain);
                mWeatherIcon.setImageResource(R.drawable.weather_rain_night);
                break;
            case "snow":
                changeBackgroundColor(R.color.snow);
                mWeatherIcon.setImageResource(R.drawable.weather_snow);
                deepChangeTextColor();
                break;
            case "sleet":
                changeBackgroundColor(R.color.sleet);
                mWeatherIcon.setImageResource(R.drawable.weather_wind);
                deepChangeTextColor();
                break;
            case "wind":
                changeBackgroundColor(R.color.wind);
                mWeatherIcon.setImageResource(R.drawable.weather_wind);
                deepChangeTextColor();
                break;
            case "fog":
                changeBackgroundColor(R.color.fog);
                mWeatherIcon.setImageResource(R.drawable.weather_fog);
                break;
            case "cloudy":
                changeBackgroundColor(R.color.cloudy);
                mWeatherIcon.setImageResource(R.drawable.weather_clouds);
                break;
            case "partly-cloudy-day":
                changeBackgroundColor(R.color.partly_cloudy_day);
                mWeatherIcon.setImageResource(R.drawable.weather_clouds);
                deepChangeTextColor();
                break;
            case "partly-cloudy-night":
                changeBackgroundColor(R.color.partly_cloudy_night);
                mWeatherIcon.setImageResource(R.drawable.weather_clouds_night);
                break;
        }
    }

    // Initialize views
    private void initViews() {
        mTemperature = getActivity().findViewById(R.id.current_temperature_main);
        mCurrentCity = getActivity().findViewById(R.id.current_city_main);
        mCurrentCountry = getActivity().findViewById(R.id.current_country_main);
        mApparentTemperature = getActivity().findViewById(R.id.apparent_temperature_main);
        mRainProbability = getActivity().findViewById(R.id.rain_probability);
        mMinTemperature = getActivity().findViewById(R.id.min_temperature);
        mMaxTemperature = getActivity().findViewById(R.id.max_temperature);
        mHour = getActivity().findViewById(R.id.hour);
        mCurrentData = getActivity().findViewById(R.id.now_card);
        mNextDays = getActivity().findViewById(R.id.daily_forecast_card);
        mNextHours = getActivity().findViewById(R.id.hourly_forecast_card);
        mOtherData = getActivity().findViewById(R.id.more_data_card);
        mWindSpeed = getActivity().findViewById(R.id.wind_speed);
        mHumidity = getActivity().findViewById(R.id.humidity);
        mSunrise = getActivity().findViewById(R.id.sunrise);
        mPressure = getActivity().findViewById(R.id.pressure);
        mDewPoint = getActivity().findViewById(R.id.dew_point);
        mSunset = getActivity().findViewById(R.id.sunset);
        mMoonPhase = getActivity().findViewById(R.id.moon_state);
        mWeatherIcon = getActivity().findViewById(R.id.weather_icon);
    }

    // Animate the background color change
    private void changeBackgroundColor(int colorId) {
        int colorFrom = mCurrentData.getCardBackgroundColor().getDefaultColor();
        int colorTo = getColor(getActivity(), colorId);
        ValueAnimator colorAnimation = new ValueAnimator();
        colorAnimation.setObjectValues(colorFrom, colorTo);
        colorAnimation.setEvaluator(new ArgbEvaluator());
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                mCurrentData.setCardBackgroundColor((Integer) animator.getAnimatedValue());
                mOtherData.setCardBackgroundColor((Integer) animator.getAnimatedValue());
            }

        });
        colorAnimation.setDuration(500);
        colorAnimation.start();
    }

    private void deepChangeTextColor() {
        for (int count = 0; count < mCurrentData.getChildCount(); count++) {
            View view = mCurrentData.getChildAt(count);
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(getColor(getActivity(), R.color.clear_night));
            }
        }
    }

    private String moonPhase(double moonPhase){
        if (moonPhase >= 0 && moonPhase < 0.25){
            return getString(R.string.new_moon);
        }

        if (moonPhase >= 0.25 && moonPhase < 0.5){
            return getString(R.string.quarter_moon);
        }

        if (moonPhase >= 0.5 && moonPhase < 0.75){
            return getString(R.string.full_moon);
        }

        if (moonPhase >= 0.75 && moonPhase < 1){
            return getString(R.string.last_quarter_moon);
        }

        return null;
    }

    // AsyncTask for retrieving data from the DarkSky API
    private class FetchWeatherTask extends AsyncTask<String, Void, Void> {

        private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

        /**
         * Take the String representing the complete forecast in JSON Format and
         * pull out the data we need to construct the Strings needed for the wireframes.
         * <p>
         * Fortunately parsing is easy:  constructor takes the JSON string and converts it
         * into an Object hierarchy for us.
         */
        private void getWeatherDataFromJson(String forecastJsonStr)
                throws JSONException {

            // Data containers of each city
            CurrentWeather currentWeather;
            ArrayList<DailyForecast> dailyForecasts = new ArrayList<>();
            ArrayList<HourlyForecast> hourlyForecasts = new ArrayList<>();
            // JSON object with all the data
            JSONObject weatherData = new JSONObject(forecastJsonStr);

            // Create a CurrentWeather object
            currentWeather = new CurrentWeather(weatherData);
            currentWeather.fahrenheitToCelsius();
            currentWeather.unixToHour();

            // Add to the list HourlyForecast object with the hourly data
            for (int i = 0; i < 24; i++) {
                hourlyForecasts.add(new HourlyForecast(weatherData, i));
                hourlyForecasts.get(i).fahrenheitToCelsius();
                hourlyForecasts.get(i).unixToHour();
            }

            // Add to the list DailyForecast objects with the daily data
            for (int i = 0; i < 7; i++) {
                dailyForecasts.add(new DailyForecast(weatherData, i));
                dailyForecasts.get(i).fahrenheitToCelsius();
                dailyForecasts.get(i).unixToHour();
            }

            // Initialize the CurrentCity object with the data downloaded
            currentCity = new City(currentWeather, hourlyForecasts, dailyForecasts);

            //String vv = new SimpleDateFormat("MM dd, yyyy hh:mma").format(df);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr;

            try {

                // Construct the DarkSky Api URL for retrieving the data
                String key = "";

                try {
                    ApplicationInfo appInfo = getActivity().getPackageManager().getApplicationInfo(
                            getActivity().getPackageName(), PackageManager.GET_META_DATA);
                    if (appInfo.metaData != null) {
                        key = appInfo.metaData.getString("dark_sky_api_key");
                    }
                } catch (PackageManager.NameNotFoundException e) {
                }

                // API site params title constants
                final String FORECAST_BASE_URL = "https://api.darksky.net/forecast";

                // Construct the API URL through an URI builder
                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendPath(key)
                        .appendPath(params[0] + "," + params[1])
                        .build();

                // String that contains the API URL
                String baseUrl = builtUri.toString();

                // Add the API key to the URL
                URL url = new URL(baseUrl);

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();

                Log.v(LOG_TAG, "Forecast Json String: " + forecastJsonStr);

            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                getWeatherDataFromJson(forecastJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        protected void onProgressUpdate(Void... params) {

        }

        @Override
        protected void onPostExecute(Void v) {
            updateWeatherInfo();
        }
    }
}
