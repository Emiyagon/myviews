package com.illyasr.mydempviews.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.illyasr.mydempviews.MyApplication;
import com.illyasr.mydempviews.R;
import com.illyasr.mydempviews.databinding.ActivityGetVideoBinding;
import com.illyasr.mydempviews.util.DownloadUtil;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.cache.model.CacheMode;
import com.zhouyou.http.callback.CallBack;
import com.zhouyou.http.exception.ApiException;

import org.json.JSONException;
import org.json.JSONObject;

public class GetVideoActivity extends AppCompatActivity {

    private ActivityGetVideoBinding binding;

    private long time = 0l;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_get_video);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_get_video);
        binding.setLifecycleOwner(this);

        binding.tvLongChart.setOnLongClickListener(v -> {
            WebActivity.GoTo(this,"http://www.17dot.cn/");
            return false;
        });

        binding.stv0.setOnClickListener(v -> {
            if ( ! binding.etHine.getText().toString().startsWith("http")){
                Toast.makeText(MyApplication.getInstance(),"请输入正确的链接!",Toast.LENGTH_SHORT).show();
                return;
            }
            EasyHttp.get(binding.etHine.getText().toString())
                    .cacheTime(300)//缓存300s 单位s
                    .cacheKey("cachekey")//缓存key
                    .cacheMode(CacheMode.CACHEANDREMOTE)//设置请求缓存模式
                    .execute(new CallBack<String>() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(ApiException e) {

                        }

                        @Override
                        public void onSuccess(String str) {
                            String s1 = str.substring(str.indexOf("<body>"), str.indexOf("</body>"));
                            String s2 = s1.substring(s1.indexOf("window.__PRELOADED_STATE__ ="), s1.indexOf("document.querySelector("));
                            String json =  s2.substring(s2.indexOf("{"),s2.length()).replace(";", "").trim();
                            longLog("第二次截取之后的是" + json);
                            try {
                                JSONObject jsonObject = new JSONObject(json);
                                JSONObject js1 = jsonObject.getJSONObject("curVideoMeta");
                                String video = js1.getString("playurl");
                                longLog("截取到的视频是 " + video);
                                if (System.currentTimeMillis() - time < 5000) {
                                    return;
                                }
                                time = System.currentTimeMillis();
                                showToast("获取成功,开始下载");
                                DownloadUtil.downMp4(GetVideoActivity.this, video, new DownloadUtil.OnDownloadListener() {
                                    @Override
                                    public void onSuccess(String s) {
                                        showToast(s);
                                    }

                                    @Override
                                    public void onFailed(String s) {

                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                                longLog("问题是 " + e.getMessage());
                                showToast("获取失败,请联系管理员或者去上方网页中获取!");
                            }
                        }
                    });
        });

    }

    private void showToast(String s) {
        Toast.makeText(MyApplication.getInstance(),s,Toast.LENGTH_SHORT).show();
    }
    /**
     * 分段打印出较长log文本
     * @param str        原log文本
     */
    private static String TAG = "TAG";
    public  static void  longLog(String str){
        int max_str_length=2001-TAG.length();
        //大于4000时
        while (str.length()>max_str_length){
            Log.e(TAG, str.substring(0,max_str_length) );
            str=str.substring(max_str_length);
        }
        //剩余部分
        Log.e(TAG, str );
    }
}