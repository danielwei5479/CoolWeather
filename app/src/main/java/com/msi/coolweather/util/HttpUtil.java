package com.msi.coolweather.util;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by MSI on 18/12/21.
 */


public class HttpUtil {
    /**
     * 异步请求方式请求数据
     * @param address
     * @param callback
     */
    public static void sendOkHttpRequest(String address,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
    /**
     * 同步请求方式请求数据
     * @param address
     * @return
     * @throws IOException
     */
    public static String  syncSendOkHttpRequest(String address) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        Response response = client.newCall(request).execute();
        String message = response.body().string();
        return message;
    }
}
