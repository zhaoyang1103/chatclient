package com.example.chartclient.zy_java.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.chartclient.zy_java.bean.UserBean;

/**
 * Created by 昭阳 on 2019/3/28.
 */
public class Util {
    public static void saveUser(Context context, UserBean userBean) {

        SharedPreferences sp = context.getSharedPreferences("userbean", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("name", userBean.getName());
        editor.putInt("qq", userBean.getQq());
        editor.putString("password", userBean.getPassword());
        editor.putString("grade", userBean.getGrade());
        editor.commit();
    }

    public static UserBean getUser(Context context) {
        SharedPreferences sp = context.getSharedPreferences("userbean", Context.MODE_PRIVATE);
        UserBean userBean = new UserBean(sp.getInt("qq", 000), sp.getString("password", ""), sp.getString("name", ""),
                sp.getString("grade", ""));
        return userBean;
    }

}
