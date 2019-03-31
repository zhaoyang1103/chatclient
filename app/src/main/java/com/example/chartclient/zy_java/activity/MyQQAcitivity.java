package com.example.chartclient.zy_java.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.chartclient.MainActivity;
import com.example.chartclient.R;
import com.example.chartclient.zy_java.bean.BigFriendsBean;
import com.example.chartclient.zy_java.bean.UserBean;
import com.example.chartclient.zy_java.util.Util;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MyQQAcitivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tx_qqshow;
    private ListView lv_friend;
    private String myuser = "";
    private Context context;
    private RequestQueue requestQueue;
    private List<UserBean> list;
    private FriendAdapter friendAdapter;
    private List<BigFriendsBean.FriendsBean> friends;
    private Timer timer;
    private Button bt_stword;
    private EditText ed_find;

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
        bt_stword = (Button) findViewById(R.id.bt_stword);
        bt_stword.setOnClickListener(this);
        ed_find = (EditText) findViewById(R.id.ed_find);
        ed_find.setOnClickListener(this);
        ed_find.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String s1 = s.toString();
                for (int i = 0; i < friends.size(); i++) {
                    if (s1.contains(friends.get(i).getQq() + "")) {
                        List<BigFriendsBean.FriendsBean> a = new ArrayList<>();
                        a.add(new BigFriendsBean.FriendsBean(friends.get(i).getGrade(), friends.get(i).getName(), friends.get(i).getPassword(), friends.get(i).getQq()));
                        friends = a;
                        friendAdapter.notifyDataSetChanged();
                    }
                }
                if (s1.equals(null) || s1.equals("")) {
                    getQQFriendLine();
                }
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
        JsonObjectRequest request = new JsonObjectRequest(JsonObjectRequest.Method.POST, "http://47.94.218.85:8080/ChartServer/get_qq_friend", object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Gson gson = new Gson();
                BigFriendsBean bigFriendsBean = gson.fromJson(jsonObject.toString(), BigFriendsBean.class);
                friends = bigFriendsBean.getFriends();
                UserBean user = Util.getUser(context);

                for (int i = 0; i < friends.size(); i++) {
                    if (friends.get(i).getQq() == user.getQq()) {
                        tx_qqshow.setText(myuser + "(" + friends.get(i).getName() + ")" + "的QQ在线" + "--" + friends.get(i).getGrade());

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_stword:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.ed_find:


                break;
        }
    }

    private void submit() {
        // validate
        String find = ed_find.getText().toString().trim();
        if (TextUtils.isEmpty(find)) {
            Toast.makeText(this, "搜索好友(qq)", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO validate success, do something


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
            viewHolder.tx_qqname.setText(friends.get(position).getName() + "--" + friends.get(position).getGrade());
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
