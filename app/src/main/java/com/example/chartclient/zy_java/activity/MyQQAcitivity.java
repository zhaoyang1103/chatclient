package com.example.chartclient.zy_java.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.chartclient.R;
import com.example.chartclient.zy_java.bean.BigFriendsBean;
import com.example.chartclient.zy_java.bean.UserBean;
import com.example.chartclient.zy_java.util.Util;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MyQQAcitivity extends AppCompatActivity {

    private TextView tx_qqshow;
    private ListView lv_friend;
    private String myuser = "";
    private Context context;
    private RequestQueue requestQueue;
    private List<UserBean> list;
    private FriendAdapter friendAdapter;
    private List<BigFriendsBean.FriendsBean> friends;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_qqacitivity);
        initView();
    }

    private void initView() {
        context = MyQQAcitivity.this;
        tx_qqshow = (TextView) findViewById(R.id.tx_qqshow);
        lv_friend = (ListView) findViewById(R.id.lv_friend);
        myuser = Util.getUser(MyQQAcitivity.this).getQq() + "";
        tx_qqshow.setText(myuser + "的QQ在线");
        requestQueue = Volley.newRequestQueue(context);
        getQQFriendLine();
        friendAdapter = new FriendAdapter();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getQQFriendLine();
            }
        }, 0, 60000);

        lv_friend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MyQQAcitivity.this, PeronActivity.class);
                intent.putExtra("friend_name", friends.get(position).getName());
                intent.putExtra("friend_qq", friends.get(position).getQq());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }

    private void getQQFriendLine() {
        JSONObject object = new JSONObject();
        JsonObjectRequest request = new JsonObjectRequest(JsonObjectRequest.Method.POST, "http://234b13j093.imwork.net:55864/api/get_qq_friend", object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Gson gson = new Gson();
                BigFriendsBean bigFriendsBean = gson.fromJson(jsonObject.toString(), BigFriendsBean.class);
                friends = bigFriendsBean.getFriends();
                UserBean user = Util.getUser(context);
                for (int i = 0; i < friends.size(); i++) {
                    if (friends.get(i).getQq() == user.getQq()) {
                        friends.remove(i);
                    }

                }

                lv_friend.setAdapter(friendAdapter);
                friendAdapter.notifyDataSetChanged();


            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MyQQAcitivity.this, "亲可能服务器没有开哦", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(request);
    }

    class FriendAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return friends.size();
        }

        @Override
        public Object getItem(int position) {
            return friends.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = View.inflate(context, R.layout.item_friend, null);
            ViewHolder viewHolder = new ViewHolder(convertView);
            viewHolder.tx_qq.setText(friends.get(position).getQq() + "");
            viewHolder.tx_qqname.setText(friends.get(position).getName());
            return convertView;
        }

        public
        class ViewHolder {
            public View rootView;
            public ImageView image_show;
            public TextView tx_qqname;
            public TextView tx_qq;

            public ViewHolder(View rootView) {
                this.rootView = rootView;
                this.image_show = (ImageView) rootView.findViewById(R.id.image_show);
                this.tx_qqname = (TextView) rootView.findViewById(R.id.tx_qqname);
                this.tx_qq = (TextView) rootView.findViewById(R.id.tx_qq);
            }

        }
    }
}
