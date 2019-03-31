package com.example.chartclient.zy_java.dao;

import com.example.chartclient.zy_java.bean.UserBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 昭阳 on 2019/3/28.
 */
public class UserNameDao extends BaseDao {
    public List<UserBean> getUserBean(int qq) {

        List<UserBean> bean = new ArrayList<>();
        Connection connection = this.getConnection();
        try {
            PreparedStatement prep = connection.prepareStatement("select * from qq_information where qq=?");
            prep.setInt(1, qq);
            ResultSet resultSet = prep.executeQuery();
            while (resultSet.next()) {
                bean.add(new UserBean(resultSet.getInt("qq"), resultSet.getString("password"), resultSet.getString("name"), resultSet.getString("grade")));

            }

            this.closeAll(connection, prep, resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        List<Integer> data_qq = new ArrayList<>();
//        List<Integer> userBeans = this.find("select qq from qq_information", list, Integer.class);

        return bean;


    }


    public List<Integer> getAllQQ() {
        List<Integer> list = new ArrayList<>();
        Connection connection = this.getConnection();
        try {
            PreparedStatement prep = connection.prepareStatement("select qq from qq_information");
            ResultSet resultSet = prep.executeQuery();
            while (resultSet.next()) {
                list.add(resultSet.getInt(1));
            }

            this.closeAll(connection, prep, resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        List<Integer> data_qq = new ArrayList<>();
//        List<Integer> userBeans = this.find("select qq from qq_information", list, Integer.class);

        return list;
    }

    public int regedit(UserBean userBean) {
        int i = 0;
        List<Object> list = new ArrayList<>();
        list.add(userBean.getName());
        list.add(userBean.getPassword());
        list.add("1级");
        i = this.executeAdd("insert into qq_information (name,password,grade)values(?,?,?)", list);
        return i;
    }

    public int change_pass(int qq, String newpasswoerd, String name) {
        int i = 0;
        Connection connection = this.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("update qq_information set password=?,name=? where qq=?");
            preparedStatement.setString(1, newpasswoerd);
            preparedStatement.setString(2, name);
            preparedStatement.setInt(3, qq);
            i = preparedStatement.executeUpdate();
            closeAll(connection, preparedStatement);
            return i;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return i;

    }


}
