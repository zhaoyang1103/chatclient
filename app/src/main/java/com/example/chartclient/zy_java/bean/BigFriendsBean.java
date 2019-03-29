package com.example.chartclient.zy_java.bean;

import java.util.List;

/**
 * Created by 昭阳 on 2019/3/29.
 */
public class BigFriendsBean {

    private List<FriendsBean> friends;

    public List<FriendsBean> getFriends() {
        return friends;
    }

    public void setFriends(List<FriendsBean> friends) {
        this.friends = friends;
    }

    public static class FriendsBean {
        /**
         * grade : 100级
         * name : 小明
         * password : 123123
         * qq : 1001
         */

        private String grade;
        private String name;
        private String password;
        private int qq;

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public int getQq() {
            return qq;
        }

        public void setQq(int qq) {
            this.qq = qq;
        }
    }
}
