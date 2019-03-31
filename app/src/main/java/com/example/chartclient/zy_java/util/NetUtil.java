package com.example.chartclient.zy_java.util;

import android.util.Log;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by 昭阳 on 2019/3/29.
 */
public class NetUtil {

    //同步传输  用于实现发送JSon等数据  请求结果
    public static String getData(String url, String data) {
        String result = "";
        Log.i("发送的数据", "getData: " + data);
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.get("application/json"), data);
        Request request = new Request.Builder().post(requestBody).url(url).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            result = response.body().string();
            Log.i("接收的数据", "getData: " + result);
            return result;
        } catch (IOException e) {
            Log.i("接收失败", "getData: " + result);
            e.printStackTrace();
        }
        return result;
    }
    //异步传输  用于实现发送JSon等数据  请求结果
    public static synchronized void getData(String url, String data, Callback callback) {

        Log.i("发送的数据", "getData: " + data);
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.get("application/json"), data);
        Request request = new Request.Builder().post(requestBody).url(url).build();
        okHttpClient.newCall(request).enqueue(callback);
    }


}
