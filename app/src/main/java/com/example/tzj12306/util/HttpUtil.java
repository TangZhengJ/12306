package com.example.tzj12306.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by 正军 on 2018/5/3.
 */

public class HttpUtil {
    public static void sendOkHttpRequest(String address, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}
