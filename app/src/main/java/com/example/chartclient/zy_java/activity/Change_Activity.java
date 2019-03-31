package com.example.chartclient.zy_java.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chartclient.R;
import com.example.chartclient.zy_java.bean.UserBean;
import com.example.chartclient.zy_java.dao.UserNameDao;

import java.util.List;

public class Change_Activity extends AppCompatActivity implements View.OnClickListener {

    private TextView tx_show;
    private EditText ed_qq;
    private EditText ed_oldpasswrod;
    private EditText ed_newpasswrod;
    private EditText ed_sure_password;
    private Button bt_change_change;
    private UserNameDao dao;
    private EditText ed_newname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_);
        initView();
    }

    private void initView() {
        tx_show = (TextView) findViewById(R.id.tx_show);
        ed_qq = (EditText) findViewById(R.id.ed_qq);
        ed_oldpasswrod = (EditText) findViewById(R.id.ed_oldpasswrod);
        ed_newpasswrod = (EditText) findViewById(R.id.ed_newpasswrod);
        ed_sure_password = (EditText) findViewById(R.id.ed_sure_password);
        bt_change_change = (Button) findViewById(R.id.bt_change_change);
        bt_change_change.setOnClickListener(this);
        dao = new UserNameDao();
        ed_newname = (EditText) findViewById(R.id.ed_newname);
        ed_newname.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_change_change:
                submit();
                break;
        }
    }

    private synchronized void submit() {
        // validate
        String qq = ed_qq.getText().toString().trim();
        if (TextUtils.isEmpty(qq)) {
            Toast.makeText(this, "qq号", Toast.LENGTH_SHORT).show();
            return;
        }

        String oldpasswrod = ed_oldpasswrod.getText().toString().trim();
        if (TextUtils.isEmpty(oldpasswrod)) {
            Toast.makeText(this, "原密码", Toast.LENGTH_SHORT).show();
            return;
        }

        String newpasswrod = ed_newpasswrod.getText().toString().trim();
        if (TextUtils.isEmpty(newpasswrod)) {
            Toast.makeText(this, "密码", Toast.LENGTH_SHORT).show();
            return;
        }

        String password = ed_sure_password.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "确认密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!newpasswrod.equals(password)) {
            Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }
        String newname = ed_newname.getText().toString().trim();
        if (TextUtils.isEmpty(newname)) {
            Toast.makeText(this, "可以修改网名噢", Toast.LENGTH_SHORT).show();
            return;
        } else {
            List<UserBean> userBean = dao.getUserBean(Integer.parseInt(qq));
            if (!userBean.get(0).getPassword().equals(oldpasswrod)) {
                Toast.makeText(this, "原密码输入有误", Toast.LENGTH_SHORT).show();
                return;
            } else {
                int i = dao.change_pass(Integer.parseInt(qq), newpasswrod, newname);
                if (i > 0) {
                    Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "修改失败", Toast.LENGTH_SHORT).show();
                }
            }


        }

        // TODO validate success, do something


    }


}
