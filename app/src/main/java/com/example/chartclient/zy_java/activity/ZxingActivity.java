package com.example.chartclient.zy_java.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.example.chartclient.R;

public class ZxingActivity extends AppCompatActivity {

    private ImageView image_zxing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zxing);
        initView();
    }

    private void initView() {
        image_zxing = (ImageView) findViewById(R.id.image_zxing);
    }
}
