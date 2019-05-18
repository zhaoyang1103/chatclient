package com.example.chartclient.zy_java;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.chartclient.zy_java.bean.Single_personBean;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class DataServer extends Service {
    private Timer timer;
    private RequestQueue requestQueue;

    public DataServer() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, "简易服务已经启动", Toast.LENGTH_SHORT).show();
        timer = new Timer();
        requestQueue = Volley.newRequestQueue(this);

        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "简易服务已经关闭", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                getSingle(intent);
            }
        }, 0, 500);

        return super.onStartCommand(intent, flags, startId);
    }

    private void getSingle(Intent intent) {
//        String singleChat = intent.getStringExtra("singleChat");
//        if (singleChat.equals("singleChat")) {
        initSingleChat();
//        }
    }


    private void initSingleChat() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("qq_1", Single_personBean.DataBean.getMybean().getQq_1());
            jsonObject.put("qq_2", Single_personBean.DataBean.getMybean().getQq_2());
            JsonObjectRequest request = new JsonObjectRequest(JsonObjectRequest.Method.POST, "http://47.94.218.85:8080/ChartServer/get_person_message", jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    Gson gson = new Gson();
                    Single_personBean single_personBean = gson.fromJson(jsonObject.toString(), Single_personBean.class);
                    Single_personBean.DataBean.setDatataBean(single_personBean.getData());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(getApplicationContext(), "亲可能服务器没有开哦", Toast.LENGTH_SHORT).show();
                }
            });
            requestQueue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
