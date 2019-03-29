package com.example.chartclient.zy_java.bean;

/**
 * Created by 昭阳 on 2019/3/28.
 */
public class UserBean {
    private int qq;
    private  String password;
    private  String name;
    private  String grade;

    public UserBean() {
    }

    public UserBean(int qq, String password, String name, String grade) {
        this.qq = qq;
        this.password = password;
        this.name = name;
        this.grade = grade;
    }

    public int getQq() {
        return qq;
    }

    public void setQq(int qq) {
        this.qq = qq;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
