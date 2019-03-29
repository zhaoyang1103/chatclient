package com.example.chartclient.zy_java.bean;

import java.util.List;

/**
 * Created by 昭阳 on 2019/3/29.
 */
public class Single_personBean {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * message_1 : 你好
         * message_2 : 123
         * qq_1 : 1001
         * qq_2 : 1002
         */

        private String message_1;
        private String message_2;
        private int qq_1;
        private int qq_2;

        public String getMessage_1() {
            return message_1;
        }

        public void setMessage_1(String message_1) {
            this.message_1 = message_1;
        }

        public String getMessage_2() {
            return message_2;
        }

        public void setMessage_2(String message_2) {
            this.message_2 = message_2;
        }

        public int getQq_1() {
            return qq_1;
        }

        public void setQq_1(int qq_1) {
            this.qq_1 = qq_1;
        }

        public int getQq_2() {
            return qq_2;
        }

        public void setQq_2(int qq_2) {
            this.qq_2 = qq_2;
        }
    }
}
