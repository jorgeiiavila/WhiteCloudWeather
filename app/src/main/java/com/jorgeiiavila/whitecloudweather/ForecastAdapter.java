package com.jorgeiiavila.whitecloudweather;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static android.media.CamcorderProfile.get;
import static android.support.v4.content.ContextCompat.getColor;


/**
 * Created by jorge on 8/1/2017.
 */

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ViewHolder> {

    private City mDataSet;
    private char mType;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // each data item is just a string in this case
        public TextView mHourDay;
        public TextView mTemperature;
        public ImageView mConditionPic;
        public LinearLayout mLayout;
        public ViewHolder(View v) {
            super(v);
            mHourDay = (TextView) v.findViewById(R.id.hour_in_recycler_view);
            mTemperature = (TextView) v.findViewById(R.id.temperature_in_recycler_view);
            mConditionPic = (ImageView) v.findViewById(R.id.condition_pic_in_recycler_view);
            mLayout = (LinearLayout) v.findViewById(R.id.recycler_view_item_layout);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), DetailsActivity.class);
            v.getContext().startActivity(intent);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ForecastAdapter(City DataSet, char type) {
        this.mDataSet = DataSet;
        this.mType = type;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ForecastAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.forecast_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        if (mType == 'd'){
            holder.mHourDay.setText(mDataSet.getDailyForecasts().get(position).getmTime() + "");
            holder.mTemperature.setText((int) mDataSet.getDailyForecasts().get(position).getmTemperatureMin() + "°" +"/" +
                    (int) mDataSet.getDailyForecasts().get(position).getmTemperatureMax() + "°");
        }else{
            holder.mHourDay.setText(mDataSet.getHourlyForecasts().get(position).getmTime() + "");
            holder.mTemperature.setText((int) mDataSet.getHourlyForecasts().get(position).getmTemperature() + "°");
        }

        setIcon(holder, position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context c = v.getContext();
                Intent intent = new Intent(c, DetailsActivity.class);
                c.startActivity(intent);
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (mType == 'd')
            return mDataSet.getDailyForecasts().size();
        else
            return mDataSet.getHourlyForecasts().size();
    }

    private void setIcon(ViewHolder holder, int position){
        String icon;
        if(mType == 'd'){
            icon = mDataSet.getDailyForecasts().get(position).getmDailyIcon();
        }else{
            icon = mDataSet.getHourlyForecasts().get(position).getmHourlyIcon();
        }
        switch (icon){
            case "clear-day":
                holder.mConditionPic.setImageResource(R.drawable.weather_clear);
                changeBackgroundColor(holder, R.color.clear_day);
                break;
            case "clear-night":
                holder.mConditionPic.setImageResource(R.drawable.weather_clear_night);
                changeBackgroundColor(holder, R.color.clear_night);
                break;
            case "rain":
                holder.mConditionPic.setImageResource(R.drawable.weather_rain_night);
                changeBackgroundColor(holder, R.color.rain);
                break;
            case "snow":
                holder.mConditionPic.setImageResource(R.drawable.weather_snow);
                changeBackgroundColor(holder, R.color.snow);
                deepChangeTextColor(holder);
                break;
            case "sleet":
                holder.mConditionPic.setImageResource(R.drawable.weather_wind);
                changeBackgroundColor(holder, R.color.sleet);
                deepChangeTextColor(holder);
                break;
            case "wind":
                holder.mConditionPic.setImageResource(R.drawable.weather_wind);
                changeBackgroundColor(holder, R.color.wind);
                deepChangeTextColor(holder);
                break;
            case "fog":
                holder.mConditionPic.setImageResource(R.drawable.weather_fog);
                changeBackgroundColor(holder, R.color.fog);
                break;
            case "cloudy":
                holder.mConditionPic.setImageResource(R.drawable.weather_clouds);
                changeBackgroundColor(holder, R.color.cloudy);
                break;
            case "partly-cloudy-day":
                holder.mConditionPic.setImageResource(R.drawable.weather_clouds);
                changeBackgroundColor(holder, R.color.partly_cloudy_day);
                break;
            case "partly-cloudy-night":
                holder.mConditionPic.setImageResource(R.drawable.weather_clouds_night);
                changeBackgroundColor(holder, R.color.partly_cloudy_night);
                break;
        }
    }

    // Animate the background color change
    private void changeBackgroundColor(final ViewHolder holder, int colorId){
        int colorFrom = getColor(holder.mLayout.getContext(), colorId);
        int colorTo = getColor(holder.mLayout.getContext(), colorId);
        ValueAnimator colorAnimation = new ValueAnimator();
        colorAnimation.setObjectValues(colorFrom, colorTo);
        colorAnimation.setEvaluator(new ArgbEvaluator());
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                holder.mLayout.setBackgroundColor((Integer) animator.getAnimatedValue());
            }

        });
        colorAnimation.setDuration(500);
        colorAnimation.start();
    }

    public void deepChangeTextColor(ViewHolder holder){
        for (int count=0; count < holder.mLayout.getChildCount(); count++){
            View view = holder.mLayout.getChildAt(count);
            if(view instanceof TextView){
                ((TextView)view).setTextColor(getColor(holder.mLayout.getContext(), R.color.clear_night));
            }
        }
    }
}
