package com.illyasr.mydempviews.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.illyasr.mydempviews.R;
import com.illyasr.mydempviews.adapter.SpeedAdapter;
import com.illyasr.mydempviews.base.BaseActivity;
import com.illyasr.mydempviews.databinding.DialogColorsBinding;
import com.illyasr.mydempviews.view.ComClickDialog;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;

import androidx.databinding.DataBindingUtil;
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
    @BindView(R.id.tv_tcolor)
    TextView colorText;
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

    }

    private int textColor = 0xffffffff;
    @Override
    protected void initView() {


        findViewById(R.id.tv_tcolor).setOnClickListener(v -> new ComClickDialog(MainActivityText.this, R.layout.dialog_colors) {

            DialogColorsBinding binding;
            @Override
            public void initView() {
                binding = DataBindingUtil.bind(getContentView());
                binding.colorPicker.addSVBar(binding.svBar);
                binding.colorPicker.addOpacityBar(binding.opacityBar);
            }

            @Override
            public void initEvent() {
                binding.colorPicker.setOnColorChangedListener(color -> {
                    Log.e("TAG", "color = " + color);
                });

                binding.dismiss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });
                binding.enter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                        Log.e("TAG", "color = " + binding.colorPicker.getColor());
                        colorText.setTextColor(binding.colorPicker.getColor());
                        textColor = binding.colorPicker.getColor();
                    }
                });
            }
        }.show());
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
                it.putExtra("color", textColor);
                startActivity(it);
                break;
        }
    }
}
