package com.illyasr.mydempviews;

import android.content.Context;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

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
    }
}
