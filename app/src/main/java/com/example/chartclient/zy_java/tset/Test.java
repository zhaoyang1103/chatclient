package com.example.chartclient.zy_java.tset;

import com.example.chartclient.zy_java.bean.UserBean;
import com.example.chartclient.zy_java.dao.UserNameDao;
import java.util.List;

/**
 * Created by 昭阳 on 2019/3/28.
 */
public class Test {
    public static void main(String[] args) {
        UserNameDao dao = new UserNameDao();
//        UserBean bean = new UserBean();
//        bean.setGrade("100级");
//        bean.setName("小沟");
//        bean.setPassword("123123");
//        int i = dao.regedit(bean);
//        System.out.println(i + "");
        List<UserBean> userBean = dao.getUserBean(1001);
        System.out.println(userBean.get(0).getName() + "");
    }
}
