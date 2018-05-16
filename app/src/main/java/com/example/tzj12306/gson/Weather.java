package com.example.tzj12306.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 正军 on 2018/5/4.
 */

public class Weather {
    public String status;
    public Basic basic;
    public AQI aqi;
    public Now now;
    public Suggestion suggestion;
    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;
}
