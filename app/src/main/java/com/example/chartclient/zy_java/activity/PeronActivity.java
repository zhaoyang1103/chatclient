package com.example.chartclient.zy_java.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.example.chartclient.R;
import com.example.chartclient.zy_java.DataServer;
import com.example.chartclient.zy_java.bean.Single_personBean;
import com.example.chartclient.zy_java.util.Util;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.util.Log.i;

public class PeronActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tx_friend_qq;
    private ListView lv_list;
    private EditText text;
    private Button bt_send;
    private LinearLayout lin;
    private RequestQueue requestQueue;
    private int friendqq;
    private int my_qq;
    private List<Single_personBean.DataBean> list_data;
    private String friendname;
    private Timer timer;
    private SingleAdapter adapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            adapter.notifyDataSetChanged();
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peron);
        initView();
    }

    private void initView() {
        tx_friend_qq = (TextView) findViewById(R.id.tx_friend_qq);
        lv_list = (ListView) findViewById(R.id.lv_list);
        text = (EditText) findViewById(R.id.text);
        bt_send = (Button) findViewById(R.id.bt_send);
        lin = (LinearLayout) findViewById(R.id.lin);
        bt_send.setOnClickListener(this);
        list_data = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(PeronActivity.this);
        initFriends();
        initAdapter();
        startServer();
        initTimer();
    }

    private void initTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                list_data = Single_personBean.DataBean.getDatataBean();
                handler.sendEmptyMessage(0);
//                AdapterResh.ReshAdapter(adapter);
//                getFriendMessage();
            }


        }, 0, 2000);
    }

    private void initAdapter() {
        adapter = new SingleAdapter();
        lv_list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void initFriends() {
        friendname = getIntent().getStringExtra("friend_name");
        i("PeronActivity", "" + friendname);
        friendqq = getIntent().getIntExtra("friend_qq", 0);
        i("PeronActivity", "" + friendqq);
        tx_friend_qq.setText("和" + friendname + "的聊天");
        my_qq = Util.getUser(PeronActivity.this).getQq();
    }

    private void startServer() {
        Single_personBean.DataBean.setMybean(new Single_personBean.DataBean(my_qq, friendqq));
        Intent intent = new Intent(this, DataServer.class);
        intent.putExtra("singleChat", "singleChat");
        startService(intent);
    }

    private void getFriendMessage() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("qq_1", my_qq);
            jsonObject.put("qq_2", friendqq);
            JsonObjectRequest request = new JsonObjectRequest(JsonObjectRequest.Method.POST, "http://47.94.218.85:8080/ChartServer/get_person_message", jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    Gson gson = new Gson();
                    Single_personBean single_personBean = gson.fromJson(jsonObject.toString(), Single_personBean.class);
                    list_data = single_personBean.getData();
                    adapter.notifyDataSetChanged();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(PeronActivity.this, "亲可能服务器没有开哦", Toast.LENGTH_SHORT).show();
                }
            });

            requestQueue.add(request);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
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
            sendMessage(text.getText().toString());
        }

        // TODO validate success, do something


    }

    private void sendMessage(String data) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("qq_1", my_qq);
            jsonObject.put("qq_2", friendqq);
            jsonObject.put("message_1", data + ":" + my_qq);
            jsonObject.put("message_2", "");
            JsonObjectRequest request = new JsonObjectRequest(JsonObjectRequest.Method.POST, "http://47.94.218.85:8080/ChartServer/send_person_message", jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    Log.i("sad", "onResponse: " + "数据发送成功");
                    Gson gson = new Gson();
                    Single_personBean single_personBean = gson.fromJson(jsonObject.toString(), Single_personBean.class);

                    list_data = single_personBean.getData();
                    Single_personBean.DataBean.setDatataBean(list_data);
                    text.setText("");
                    adapter.notifyDataSetChanged();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(PeronActivity.this, "亲可能服务器没有开哦", Toast.LENGTH_SHORT).show();
                }
            });

            requestQueue.add(request);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    class SingleAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list_data.size();
        }

        @Override
        public Object getItem(int position) {
            return list_data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = View.inflate(PeronActivity.this, R.layout.item_chart_friend, null);
            ViewHolder viewHolder = new ViewHolder(convertView);
            String[] split = list_data.get(position).getMessage_1().split(":");
            if (list_data.get(position).getMessage_1().contains(my_qq + "")) {
                viewHolder.tx_my_message.setText(split[0] + "：我");
                viewHolder.tx_my_message.setPadding(10, 10, 10, 10);
                viewHolder.tx_friend_message.setPadding(0, 0, 0, 0);
            } else {
                viewHolder.tx_friend_message.setText(friendname + ":" + split[0]);
                viewHolder.tx_friend_message.setPadding(10, 10, 10, 10);
                viewHolder.tx_friend_message.setPadding(0, 0, 0, 0);
            }

            return convertView;
        }

        public
        class ViewHolder {
            public View rootView;

            public TextView tx_friend_message;

            public TextView tx_my_message;

            public ViewHolder(View rootView) {
                this.rootView = rootView;
                this.tx_friend_message = (TextView) rootView.findViewById(R.id.tx_friend_message);
                this.tx_my_message = (TextView) rootView.findViewById(R.id.tx_my_message);
            }

        }
    }

}
