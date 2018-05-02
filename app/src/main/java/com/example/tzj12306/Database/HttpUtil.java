package com.example.tzj12306.Database;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by 正军 on 2018/4/20.
 */

public class HttpUtil {
    public static void sendOkHttp(String address,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}
