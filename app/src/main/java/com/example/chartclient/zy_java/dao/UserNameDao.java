package com.example.chartclient.zy_java.dao;

import com.example.chartclient.zy_java.bean.UserBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 昭阳 on 2019/3/28.
 */
public class UserNameDao extends BaseDao {
    public List<UserBean> getUserBean(int qq) {
        List<Object> list = new ArrayList<>();
        list.add(qq);
        List<UserBean> userBeans = this.find("select * from qq_information where qq=?", list, UserBean.class);
        return userBeans;
    }


    public List<Integer> getAllQQ() {
        List<Object> list = new ArrayList<>();
        List<Integer> data_qq = new ArrayList<>();
        List<UserBean> userBeans = this.find("select * from qq_information", list, UserBean.class);
        for (int i = 0; i < userBeans.size(); i++) {
            data_qq.add(userBeans.get(i).getQq());
        }
        return data_qq;

    }


}
