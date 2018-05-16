package com.example.tzj12306.gson;

/**
 * Created by 正军 on 2018/5/4.
 */

public class AQI {
    public AQIcity city;

    public class AQIcity {
        public String aqi;
        public String pm25;
    }
}
