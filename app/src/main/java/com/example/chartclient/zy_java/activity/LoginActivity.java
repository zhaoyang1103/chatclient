package com.example.chartclient.zy_java.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.chartclient.R;
import com.example.chartclient.zy_java.bean.UserBean;
import com.example.chartclient.zy_java.dao.UserNameDao;
import com.example.chartclient.zy_java.util.Util;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText ed_qq;
    private EditText ed_passwrod;
    private Button login;
    private RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        ed_qq = (EditText) findViewById(R.id.ed_qq);
        ed_passwrod = (EditText) findViewById(R.id.ed_passwrod);
        login = (Button) findViewById(R.id.login);
        EventBus.getDefault().register(this);
        login.setOnClickListener(this);

        StrictMode.ThreadPolicy build = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(build);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                submit();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMessage(String data) {
        switch (data) {
            case "成功":
                Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MyQQAcitivity.class);
                startActivity(intent);
//                finish();
                break;
            case "失败":
                Toast.makeText(this, "用户名密码不对", Toast.LENGTH_SHORT).show();
                break;

        }
    }

    private void submit() {
        // validate

        String qq = ed_qq.getText().toString().trim();
        if (TextUtils.isEmpty(qq)) {
            Toast.makeText(this, "qq号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String passwrod = ed_passwrod.getText().toString().trim();
        if (TextUtils.isEmpty(passwrod)) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        } else {
//            loginDeal(qq, passwrod);
            try {
                login(Integer.parseInt(qq), passwrod);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // TODO validate success, do something///


    }


    private synchronized void login(final int qq, final String password) throws JSONException {
        requestQueue = Volley.newRequestQueue(LoginActivity.this);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("qq", qq);
        jsonObject.put("password", password);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(JsonObjectRequest.Method.POST, "http://47.94.218.85:8080/ChartServer/send_qq_password", jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    if (jsonObject.get("RESULT").equals("S")) {
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MyQQAcitivity.class);
                        startActivity(intent);
                        UserBean userNameDao = new UserBean(qq, password, "未设置", "100级");
                        Util.saveUser(LoginActivity.this, userNameDao);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "用户名密码不对", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(LoginActivity.this, "亲可能数据没有开哦", Toast.LENGTH_SHORT).show();

            }
        });
        requestQueue.add(jsonObjectRequest);

    }

    private void loginDeal(final String qq, final String passwrod) {
        new Thread() {
            @Override
            public void run() {
                UserNameDao dao = new UserNameDao();
                List<UserBean> userBeans = dao.getUserBean(Integer.parseInt(qq));
                if (userBeans.size() > 0) {
                    if (passwrod.equals(userBeans.get(0).getPassword())) {
                        EventBus.getDefault().post("成功");
                    } else {
                        EventBus.getDefault().post("失败");
                    }
                }

            }
        }.start();

    }
}
