package com.example.chartclient.zy_java.tset;

import com.example.chartclient.zy_java.bean.UserBean;
import com.example.chartclient.zy_java.dao.UserNameDao;

import java.util.List;

/**
 * Created by 昭阳 on 2019/3/28.
 */
public class Test {
    public static void main(String[] args) {
        UserNameDao dao=new UserNameDao();
        List<UserBean> userBean = dao.getUserBean(1001);
      if(userBean.size()>0)
      {
          System.out.println(userBean.get(0).getPassword()+"");
      }
    }
}
