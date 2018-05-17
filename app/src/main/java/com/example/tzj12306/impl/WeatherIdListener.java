package com.example.tzj12306.impl;

/**
 * Created by 正军 on 2018/5/17.
 */

public interface WeatherIdListener {
    void onCityStart(String data1,String data2);
    void onCityEnd(String data1,String data2);
}
