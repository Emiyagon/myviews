package com.illyasr.mydempviews.base;

import android.Manifest;
import android.app.Activity;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.illyasr.mydempviews.MyApplication;
import com.illyasr.mydempviews.R;
import com.illyasr.mydempviews.listener.CheckPermissionsListener;
import com.illyasr.mydempviews.receiver.NetWorkStateReceiver;
import com.illyasr.mydempviews.util.AppUtils;
import com.illyasr.mydempviews.util.GlideCacheUtil;
import com.illyasr.mydempviews.util.ScreenUtil;
import com.illyasr.mydempviews.util.SoftInputUtils;
import com.illyasr.mydempviews.util.StatusUtils;
import com.illyasr.mydempviews.view.MProgressDialog;
import com.jeremyliao.liveeventbus.LiveEventBus;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.EasyPermissions;

import static com.illyasr.mydempviews.util.ToastUtils.getView;


/**
 *
 * @author bullet
 * @date 2018/8/11
 */

public abstract class BaseActivity extends AppCompatActivity {

    /**
     *    标题栏高度 128dp
     */
    public static int title_height = 128;


    private MyBaseActiviy_Broad oBaseActiviy_Broad;
    Window window = getWindow();
//    Disposable disposable;
    MProgressDialog progressDialog;
    public static final int REQUEST_CODE = 2018;
    private CheckPermissionsListener mListener;

    public static final String BASE_ADDRESS = AppUtils.getPackageName(MyApplication.getInstance()) + ".base.BaseActivity";
    /**
     * 标记不同事件的请求
     */
    public static int flag = -1;
    public int statueHeight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //动态注册广播
        oBaseActiviy_Broad = new MyBaseActiviy_Broad();
        //这里写baseactivity的地址
        IntentFilter intentFilter = new IntentFilter(BASE_ADDRESS);
        registerReceiver(oBaseActiviy_Broad, intentFilter);
       // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //让布局向上移来显示软键盘
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(setLayoutId());
        ButterKnife.bind(this);
        // 获取状态栏高度
        statueHeight = ScreenUtil.getStatueBarHeight(BaseActivity.this);

//        View titleView = LayoutInflater.from(MyApplication.getInstance()).inflate(R.layout.title_view, null);
        //view_title
        if (isNetworkAvailable(this) == false) {
            showToast("网络连接不可用,请检查您的网络!");
        }
        initData();
        initView();


    }

    private void SetOffLine(final Activity activity) {
        LiveEventBus.get().with("SetOffLine",Boolean.class).observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean) {
                    Intent intent = new Intent(BASE_ADDRESS);
                    intent.putExtra(CLOSE_ALL, 1);
                    //发送广播
                    sendBroadcast(intent);
//                    startActivity(new Intent("com.zhuosen.chaoqijiaoyu.ui.activity.LoginActivity").setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//                    startActivity(new Intent(activity,LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//                    RetrofitService.getInstance().myTag = 0;
//                    RetrofitService.getInstance().X5Tag = 0;
                    finish();
                }
            }
        });
    }
    /**
     *  设置margin
     * @param titles
     */
    public static void setMargin(final View titles) {

       /* int height = ScreenUtil.getStatueBarHeight(titles.getContext());
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(titles.getLayoutParams());
        lp.setMargins(0, height+1, 0, 0);
        titles.setLayoutParams(lp);*/

        titles.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                titles.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Rect rect = new Rect();
                titles.getWindowVisibleDisplayFrame(rect);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(titles.getLayoutParams());
                params.setMargins(0, rect.top, 0, 0);
                titles.setLayoutParams(params);
            }
        });

    }
    public static void setMargin(final View titles, int nums) {
       /* LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(titles.getLayoutParams());
        lp.setMargins(0, nums, 0, 0);
        titles.setLayoutParams(lp);*/
        titles.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                titles.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Rect rect = new Rect();
                titles.getWindowVisibleDisplayFrame(rect);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(titles.getLayoutParams());
                params.setMargins(0, rect.top, 0, 0);
                titles.setLayoutParams(params);
            }
        });
    }

    public static void setMargins(View titles,int nums) {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(titles.getLayoutParams());
        lp.setMargins(0, nums, 0, 0);
        titles.setLayoutParams(lp);
    }

    /**
     * 新版设置view远离刘海屏的方法
     */
    public void setHairBuffer(final View LLTop) {
        LLTop.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                LLTop.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Rect rect = new Rect();
                getView().getWindowVisibleDisplayFrame(rect);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LLTop.getLayoutParams());
                params.setMargins(0, rect.top, 0, 0);
                LLTop.setLayoutParams(params);
            }
        });
    }

    public void setLHairBuffer(final View LLTop) {
        LLTop.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                LLTop.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Rect rect = new Rect();
                getView().getWindowVisibleDisplayFrame(rect);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LLTop.getLayoutParams());
                params.setMargins(0, rect.top, 0, 0);
                LLTop.setLayoutParams(params);
            }
        });
    }
    /**
     * Sets layout id.
     *
     * @return the layout id
     */
    protected abstract int setLayoutId();
    /**
     * 适配6.0沉浸式状态栏操作
     * 保险起见直接粘贴,或者直接提取一个方法也可以
     */
   /* public  void Immersion(){
        ImmersionBar.with(this)
                .transparentStatusBar()  //透明状态栏，不写默认透明色
                .transparentNavigationBar()  //透明导航栏，不写默认黑色(设置此方法，fullScreen()方法自动为true)
                .transparentBar()             //透明状态栏和导航栏，不写默认状态栏为透明色，导航栏为黑色（设置此方法，fullScreen()方法自动为true）
                .statusBarColor(R.color.colorPrimary)     //状态栏颜色，不写默认透明色
                .navigationBarColor(R.color.colorPrimaryDark) //导航栏颜色，不写默认黑色
//                .barColor(R.color.colorPrimary)  //同时自定义状态栏和导航栏颜色，不写默认状态栏为透明色，导航栏为黑色
//                .statusBarAlpha(0.3f)  //状态栏透明度，不写默认0.0f
//                .navigationBarAlpha(0.1f)  //导航栏透明度，不写默认0.0F
                .barAlpha(0.5f)  //状态栏和导航栏透明度，不写默认0.0f
//                .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
                .statusBarDarkFont(false)   //状态栏字体是深色，不写默认为亮色(false)
//                .fullScreen(true)      //有导航栏的情况下，activity全屏显示，也就是activity最下面被导航栏覆盖，不写默认非全屏
                *//*
                  FLAG_HIDE_BAR,  //隐藏状态栏和导航栏
                FLAG_SHOW_BAR  //显示状态栏和导航栏
                *//*
//                .hideBar(BarHide.FLAG_HIDE_BAR)  //隐藏状态栏或导航栏或两者，不写默认不隐藏
//                .addViewSupportTransformColor(toolbar)  //设置支持view变色，可以添加多个view，不指定颜色，默认和状态栏同色，还有两个重载方法

//                .titleBarMarginTop(toolbar)     //解决状态栏和布局重叠问题，任选其一,这个方法在我的手机上可以实现,但是明明设置了颜色却显示灰色
                .fitsSystemWindows(true)    //解决状态栏和布局重叠问题,但是statuebar颜色总是和colorpramirydark一致,这个无法解决
                .init();
    }*/



    /**
     * 控件注册和使用
     */
    protected abstract void initData();

    /**
     * 逻辑代码实现
     */
    protected abstract void initView();

    @Override
    /**
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE:
                List<String> deniedPermissions = new ArrayList<>();
                int length = grantResults.length;
                for (int i = 0; i < length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        //该权限被拒绝了
                        deniedPermissions.add(permissions[i]);
                    }
                }
                if (deniedPermissions.size() > 0) {
                    mListener.onDenied(deniedPermissions);
                } else {
                    mListener.onGranted();
                }
                break;
            default:
                break;
        }
    }

    /**
     *   申请权限 (6.0专用)
     */
    public void newPromession() {
        ContentValues values;
        String[] messions = new String[]{
//                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.SYSTEM_ALERT_WINDOW,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CALL_PHONE};
        if (!EasyPermissions.hasPermissions(this, messions)) {
            EasyPermissions.requestPermissions(this, "请打开相关权限,以保证您可以正常使用app!", 100, messions);
        }
    }
    /**
     * 检查当前网络是否可用
     * @param
     * @return
     */
    public boolean isNetworkAvailable(Context activity){
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null){
            return false;
        }else{
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0){
                for (int i = 0; i < networkInfo.length; i++){
                    Log.e("TAG",i + "===状态===" + networkInfo[i].getState());
                    Log.e("TAG", i + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 判断网络状态
     */
    NetWorkStateReceiver netWorkStateReceiver;

    /**
     * 判断是否有网络连接
     */
    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     *  判断WIFI网络是否可用
     */
    public boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }
    /**
     * 判断MOBILE网络是否可用
     */
    public boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }
    /**
     * 获取当前网络连接的类型信息
     */
    public static int getConnectedType(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.getType();
            }
        }
        return -1;
    }
    /**
     * 有时候我们连接上一个没有外网连接的WiFi或者有线就会出现这种极端的情况，
     * 目前Android SDK还不能识别这种情况，一般的解决办法就是ping一个外网。
     */
    /* @author suncat
     * @category 判断是否有外网连接（普通方法不能判断外网的网络是否连接，比如连接上局域网）
     * @return
     */
    public static final boolean ping() {

        String result = null;
        try {
            String ip = "www.baidu.com";// ping 的地址，可以换成任何一种可靠的外网
            Process p = Runtime.getRuntime().exec("ping -c 3 -w 100 " + ip);// ping网址3次
            // 读取ping的内容，可以不加
            InputStream input = p.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            StringBuffer stringBuffer = new StringBuffer();
            String content = "";
            while ((content = in.readLine()) != null) {
                stringBuffer.append(content);
            }
            Log.d("------ping-----", "result content : " + stringBuffer.toString());
            // ping的状态
            int status = p.waitFor();
            if (status == 0) {
                result = "success";
                return true;
            } else {
                result = "failed";
            }
        } catch (IOException e) {
            result = "IOException";
        } catch (InterruptedException e) {
            result = "InterruptedException";
        } finally {
            Log.d("----result---", "result = " + result);
        }
        return false;
    }

    /**
     * 在onResume()方法注册
     */
    @Override
    protected void onResume() {
        if (netWorkStateReceiver == null) {
            netWorkStateReceiver = new NetWorkStateReceiver();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        super.onResume();
//        EasyHttp.cancelSubscription(disposable);
        //  沉浸式状态栏
        StatusUtils.transparentStatusBar(this, true);
        //还有一种用法 ,看到底需要哪种
    }
    /**
     * onPause()方法注销
     */
    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * 在销毁的方法里面注销广播
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //注销广播
        unregisterReceiver(oBaseActiviy_Broad);
        EventBus.getDefault().unregister(this);//注销eventbus
//        EasyHttp.cancelSubscription(disposable);
        //clearImageAllCache  清除glide缓存,防止oom
        GlideCacheUtil.getInstance().clearImageAllCache(BaseActivity.this);

        SoftInputUtils.hideSoftInput(this);
    }

    /**
     * 定义一个广播
     */
    public static final String CLOSE_ALL = "closeAll";
    public class MyBaseActiviy_Broad extends BroadcastReceiver {
        @Override
        public void onReceive(Context arg0, Intent intent) {
            //接收发送过来的广播内容
            int closeAll = intent.getIntExtra(CLOSE_ALL, 0);
            if (closeAll == 1) {
                finish();//销毁BaseActivity
            }
        }
    }
    /**
     * 弹出提示框
     * @param msg
     */
    public MProgressDialog showDialog(String msg) {
        if (progressDialog == null) {
            progressDialog = new MProgressDialog(this);
            progressDialog = progressDialog.createLoadingDialog(msg);
            progressDialog.show();
        } else if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
        return progressDialog;
    }


    /**
     * 弹出提示框
     *
     * @param msg
     */
//    public SVProgressHUD mSVProgressHUD;
//    public void showDialog(String msg,int tag) {
//        if (mSVProgressHUD == null) {
//            mSVProgressHUD = new SVProgressHUD(this);
//            mSVProgressHUD.showWithStatus(msg);
//        }  else if (!mSVProgressHUD.isShowing()) {
//            mSVProgressHUD.showWithStatus(msg);
//        }
//    }

    /**
     * 关闭提示框
     */
    public void dismissDialog() {
        if (this.progressDialog != null) {
            if (this.progressDialog.isShowing()) {
                this.progressDialog.dismiss();
            }
            this.progressDialog = null;
        }

        //------
//        if (this.mSVProgressHUD != null) {
//            if (this.mSVProgressHUD.isShowing()) {
//                this.mSVProgressHUD.dismiss();
//            }
//        }

    }
    /**
     * 显示Toast信息
     */
    public void showToast(String text) {
        Toast.makeText(MyApplication.getInstance(), text, Toast.LENGTH_SHORT).show();
    }

    public void isViewShow(View view,int zes) {
        if (zes > 0) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    /**
     * 控制控件的显示与否
     * @param view 控件
     * @param type 1 显示   0 隐藏
     */
    public void onViewShowed(View view, int type) {
        switch (type%2) {
            default:
                break;

            case 0:
                //隐藏
                view.setVisibility(View.GONE);
                break;

            case  1:
                //显示
                view.setVisibility(View.VISIBLE);
                break;
        }
    }
    /**
     * 正计时器的实现
     */
    /*
    * public class MyThread implements Runnable{   // thread
    @Override
    public void run(){
      while(true){
        try{
          Thread.sleep(1000);   // sleep 1000ms
          Message message = new Message();
          message.what = 1;
          handler.sendMessage(message);
        }catch (Exception e) {
        }
    * */
    int recLen = 0;
    TextView txtView;
    final Handler handler = new Handler(){
        // handle
        @Override
        public void handleMessage(Message msg){
            switch (msg.what) {
                default:
                    break;
                case 1:
                    recLen++;
                    txtView.setText("" + recLen);
            }
            super.handleMessage(msg);
        }
    };
    private class MyThread implements Runnable{
        @Override
        public void run() {
            while (true) {
                try {
                    //睡1s
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what=1;
                    handler.sendMessage(msg);
                } catch (Exception e) {
                }
            }
        }
    }


}
