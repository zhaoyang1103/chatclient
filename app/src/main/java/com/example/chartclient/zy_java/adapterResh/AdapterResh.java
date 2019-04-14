package com.example.chartclient.zy_java.adapterResh;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.BaseAdapter;

/**
 * Created by 昭阳 on 2019/4/14.
 */
public class AdapterResh {
    private static BaseAdapter baseAdapter;

    public static void ReshAdapter(BaseAdapter baseAdapter1) {
        baseAdapter = baseAdapter1;
        Looper.prepare();
        handler.sendEmptyMessage(0);
        Looper.loop();
    }

    private static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            baseAdapter.notifyDataSetChanged();
            super.handleMessage(msg);
        }
    };

}
