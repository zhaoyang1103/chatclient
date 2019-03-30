package com.example.chartclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText text;
    private Button bt_send;
    private LinearLayout lin;
    private RequestQueue requestQueue;
    private List<String> list;
    private Lv_Adapter adapter;
    private ListView lv_list;
    private Timer timer;
    private Random random;
    private int a;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {

        text = (EditText) findViewById(R.id.text);
        bt_send = (Button) findViewById(R.id.bt_send);
        lin = (LinearLayout) findViewById(R.id.lin);
        requestQueue = Volley.newRequestQueue(MainActivity.this);
        lv_list = (ListView) findViewById(R.id.lv_list);
        bt_send.setOnClickListener(this);
        list = new ArrayList<>();
        adapter = new Lv_Adapter();
        lv_list.setAdapter(adapter);
        timer = new Timer();
        random = new Random();
        a = random.nextInt(5000);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getMessage();
            }
        }, 0, 3000);
    }

    private void getMessage() {
        JSONObject jsonObject = new JSONObject();
        JsonObjectRequest request = new JsonObjectRequest(JsonObjectRequest.Method.POST, "http://234b13j093.imwork.net:55864/api/get_message", jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Gson gson = new Gson();
                Bean bean = gson.fromJson(jsonObject.toString(), Bean.class);
                list = bean.getDataList();

                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });
        requestQueue.add(request);


    }

    @Override
    protected void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_send:
                submit();
                break;
        }
    }

    private void submit() {
        // validate
        String textString = text.getText().toString().trim();
        if (TextUtils.isEmpty(textString)) {
            Toast.makeText(this, "聊天框", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Toast.makeText(this, "测试", Toast.LENGTH_SHORT).show();
            sendData();
        }

        // TODO validate success, do something


    }

    private void sendData() {
        String s = text.getText().toString();
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user", s);
            JsonObjectRequest request = new JsonObjectRequest(JsonObjectRequest.Method.POST, "http://47.94.218.85:8080/ChartServer/send_message", jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    Gson gson = new Gson();
                    Bean bean = gson.fromJson(jsonObject.toString(), Bean.class);
                    list = bean.getDataList();
                    text.setText("");
                    Toast.makeText(MainActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(MainActivity.this, "失败" + volleyError.toString(), Toast.LENGTH_SHORT).show();
                    Log.i("失败", "onErrorResponse: " + volleyError.toString());
                }
            });
            requestQueue.add(request);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    class Lv_Adapter extends BaseAdapter {
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = View.inflate(MainActivity.this, R.layout.item_test, null);
            ViewHolder viewHolder = new ViewHolder(convertView);
            viewHolder.item_content.setText(list.get(position) + "");
            return convertView;
        }

        public
        class ViewHolder {
            public View rootView;
            public TextView item_content;

            public ViewHolder(View rootView) {
                this.rootView = rootView;
                this.item_content = (TextView) rootView.findViewById(R.id.item_content);
            }

        }
    }

}
