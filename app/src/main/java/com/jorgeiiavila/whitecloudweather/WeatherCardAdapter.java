package com.jorgeiiavila.whitecloudweather;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jorge on 6/16/2017.
 */

public class WeatherCardAdapter extends ArrayAdapter<City> {

    public WeatherCardAdapter(Activity context, ArrayList<City> cards) {
        super(context, 0, cards);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View weatherCardList = convertView;
        if (weatherCardList == null) {
            weatherCardList = LayoutInflater.from(getContext()).inflate(
                    R.layout.card_weather, parent, false);
        }

        // Get the current weather card
        final City currentCard = getItem(position);

        // Find the ImageView with the current condition picture and set the image resource
        ImageView currentCondition = (ImageView) weatherCardList.findViewById(R.id.current_condition_pic);
        currentCondition.setImageResource(R.drawable.sunny);

        // Find the city text view and set the name
        TextView cityName = (TextView) weatherCardList.findViewById(R.id.current_city_card);
        cityName.setText(currentCard.getmCity());

        // Find the state text view and set the name
        TextView stateName = (TextView) weatherCardList.findViewById(R.id.current_country_card);
        stateName.setText(currentCard.getmCountry());

        // Find the current temperature and set the value
        TextView currentTemp = (TextView) weatherCardList.findViewById(R.id.current_temperature_card);
        currentTemp.setText("°" + currentCard.getCurrentWeather().getmTemperature());

        return weatherCardList;
    }
}

