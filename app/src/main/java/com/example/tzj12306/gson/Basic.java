package com.example.tzj12306.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 正军 on 2018/5/4.
 */

public class Basic {
    @SerializedName("city")
    public String cityName;
    @SerializedName("id")
    public String weatherId;
    public Update update;

    public class Update {
        @SerializedName("loc")
        public String updateTime;
    }
}
