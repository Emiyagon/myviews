package com.illyasr.mydempviews;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.illyasr.mydempviews.util.RxTimerUtil;

// 只是一个透明页面
public class SplashActivity extends AppCompatActivity {
    /**
     <img
     src="https://img.tuguaishou.com/designer_upload_asset/16/00/62/06/10/3c/3c4472e8ac50ff715eb305649762af4d.png!h1600_b_w?auth_key=2235484800-0-0-8f86c2eba1e346d63ccb5e7a3b961925&amp;v=2"


     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        RxTimerUtil.timer(2000, number -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        });

    }
}
