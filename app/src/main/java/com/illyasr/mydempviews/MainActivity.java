package com.illyasr.mydempviews;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import com.illyasr.mydempviews.adapter.MainAdapter;
import com.illyasr.mydempviews.base.BaseActivity;
import com.illyasr.mydempviews.bean.TabBean;
import com.illyasr.mydempviews.ui.activity.MainActivityText;
import com.illyasr.mydempviews.ui.activity.MyLocationActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.EasyPermissions;

//广告业
public class MainActivity extends BaseActivity {

    @BindView(R.id.rv_albums)
    RecyclerView rvAlbums;
    private MainAdapter adapter;
    private List<TabBean> list = new ArrayList<>();

    @Override
    protected int setLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {

        String[] messions = new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_PHONE_STATE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.CAMERA,
        };
        if (EasyPermissions.hasPermissions(this, messions)) {
//            GetLoc();
        } else {
            EasyPermissions.requestPermissions(this, getResources().getString(R.string.toast_1), 100, messions);

        }


        list.add(new TabBean("获取定位",0));
        list.add(new TabBean("撩妹神器",1));
        list.add(new TabBean("SpView",2));


        rvAlbums.setLayoutManager(new GridLayoutManager(this,3));
        adapter = new MainAdapter(this,list);
        rvAlbums.setAdapter(adapter);
        adapter.setOnRem((pos, type) -> {
            switch (pos) {
                case 0:// 获取定位
                    startActivity(new Intent(MainActivity.this, MyLocationActivity.class));
                    break;
                case 1:// 高能弹幕
                    startActivity(new Intent(MainActivity.this, MainActivityText.class));
                    break;
                case 2:// 高能弹幕
                    try {
                        startActivity(new Intent(MainActivity.this,Class.forName("com.coorchice.supertextview.MainActivity")));//
                    } catch (Exception e) {
                        showToast("请安装STV之后重试");
                        e.printStackTrace();
                    }
                    break;
            }

        });
    }

    @Override
    protected void initView() {

    }
}
