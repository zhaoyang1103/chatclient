package com.example.chartclient.zy_java.util;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by 昭阳 on 2019/3/31.
 */
public class ProDiaBar {
    public ProgressDialog progressDialog;
    private Context context;

    public ProDiaBar(Context context) {
        this.context = context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
    }

    public void show() {


        progressDialog.show();
    }

    public void diss() {
        progressDialog.dismiss();
    }
}
