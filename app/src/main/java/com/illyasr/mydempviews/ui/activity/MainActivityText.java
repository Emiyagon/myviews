package com.illyasr.mydempviews.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.illyasr.mydempviews.R;
import com.illyasr.mydempviews.adapter.SpeedAdapter;
import com.illyasr.mydempviews.base.BaseActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivityText extends BaseActivity {
    @BindView(R.id.msg)
    EditText text;
    @BindView(R.id.rv_speed)
    RecyclerView speed;
    @BindView(R.id.submit)
    FrameLayout mButton;
    private int textspeed = 1;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_main_text;
    }

    @Override
    protected void initData() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.HORIZONTAL);
        speed.setLayoutManager(manager);
        speed.setAdapter(new SpeedAdapter(this, new int[]{1, 2, 3, 4, 5, 10})
                .setOnSpeedClicklistener((pos, speed) -> {
                    textspeed = speed;
                }));

//        text.setText("如果下次遇见你" + "/希望是在晴天里" + "/要对你说的话想来想去" + "/是你我的记忆");
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.msg, R.id.submit})
    public void onViewClicked(View v) {
        switch (v.getId()) {
//            case R.id.msg:
//                break;
            case R.id.submit:
                if (TextUtils.isEmpty(text.getText().toString())){
                    showToast("请输入弹幕!");
                    return;
                }
                Intent it=new Intent();
                it.setClass(MainActivityText.this,BarrageActivity.class);//从MainActivity跳转到AfterLoginActivity
                it.putExtra("msg", text.getText().toString().trim());//给intent添加数据，函数第一个参数是key,第二个是值。
                it.putExtra("speed", textspeed);
                startActivity(it);
                break;
        }
    }
}
