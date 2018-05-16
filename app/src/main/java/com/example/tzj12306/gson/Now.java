package com.example.tzj12306.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 正军 on 2018/5/4.
 */

public class Now {
    @SerializedName("tmp")
    public String temperature;
    @SerializedName("cond")
    public More more;

    public class More {
        @SerializedName("txt")
        public String info;
    }
}
