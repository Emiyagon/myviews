package com.illyasr.mydempviews;

import android.content.Context;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.readystatesoftware.chuck.ChuckInterceptor;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.cache.converter.SerializableDiskConverter;
import com.zhouyou.http.model.HttpHeaders;

public class MyApplication extends MultiDexApplication {
    public static MyApplication myApp;
    public static boolean isTestEnvironment = true;
    public static MyApplication getInstance() {
        return myApp;
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        myApp=this;
//        MultiDex.install(MyApplication.getInstance());

        // 初始化HttpClient.
        EasyHttp.init(this);//默认初始化,必须调用
        //设置请求头
        HttpHeaders headers = new HttpHeaders();
//        headers.put("sign", HttpUtils.sign);
//        headers.put("token",token);
        EasyHttp.getInstance()
                .setCacheTime(-1)//-1表示永久缓存,单位:秒 ，Okhttp和自定义RxCache缓存都起作用
                .debug("RxEasyHttp", true)
                .addCommonHeaders(headers)//设置全局公共头
                .setReadTimeOut(60 * 1000)
                .setWriteTimeOut(60 * 1000)
                .setConnectTimeout(60 * 1000)
                .setRetryCount(3)//默认网络不好自动重试3次
                .setRetryDelay(500)//每次延时500ms重试
                .setRetryIncreaseDelay(500)//每次延时叠加500ms
//                .setBaseUrl(RetrofitUtils.BASE_URL)
                .setCacheDiskConverter(new SerializableDiskConverter())//默认缓存使用序列化转化
                .setCacheMaxSize(50 * 1024 * 1024)//设置缓存大小为50M
                .setCacheVersion(0)//缓存版本为1
//                .addInterceptor(new ChuckInterceptor(MyApplication.getInstance()))
                .setCertificates();//信任所有证书;
    }
}
