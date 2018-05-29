package com.example.tzj12306.UI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.tzj12306.Fragment.ChooseStationFragment;
import com.example.tzj12306.MyActionBar.MyActionbar;
import com.example.tzj12306.MyActionBar.MyBaseActivity;
import com.example.tzj12306.R;
import com.example.tzj12306.db.User;
import com.example.tzj12306.impl.WeatherIdListener;

public class MainActivity extends MyBaseActivity {
    private String city_start = "临安";
    private String city_end = "杭州";
    private String weather_start = "CN101210107";
    private String weather_end = "CN101210101";
    private TextView user_name;
    private TextView user_message;
    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        user_name = (TextView) findViewById(R.id.user_name);
        user_message = (TextView) findViewById(R.id.user_message);

    }

    @Override
    protected void onStart() {
        super.onStart();
        ChooseStationFragment.setOnWeatherIdListener(new OnWeatherIdListener());
    }

    public class OnWeatherIdListener implements WeatherIdListener {
        @Override
        public void onCityStart(String data1, String data2) {
            weather_start = data1;
            city_start = data2;
        }

        @Override
        public void onCityEnd(String data1, String data2) {
            weather_end = data1;
            city_end = data2;
        }
    }

    public String getCity_start() {
        return city_start;
    }

    public String getCity_end() {
        return city_end;
    }

    public String getWeather_start() {
        return weather_start;
    }

    public String getWeather_end() {
        return weather_end;
    }
}
