package com.illyasr.mydempviews.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.illyasr.mydempviews.R;


/**
 *  glide加载工具类
 * @author bullet
 * @date 2018/8/13
 */

public class GlideUtil {


    public static void putImg(Context context, Object bitmap, ImageView imageView) {
        Glide.with(context).load(bitmap).into(imageView);
    }




    /**
     * 加载圆形图片
     * @param context context
     * @param url  图片地址
     * @param imageView ima
     */
    public static void  putRollImg(Context context, Object url, ImageView imageView) {
        RequestOptions options = new RequestOptions();
        options.centerCrop()
                .placeholder(R.mipmap.touxiang)
                .error(R.mipmap.touxiang)
                .fallback(R.mipmap.touxiang);

        new RequestOptions().centerCrop()
                .placeholder(R.mipmap.touxiang)
                .error(R.mipmap.touxiang)
                .fallback(R.mipmap.touxiang);
        Glide.with(context)
                .load(url)
//                .apply(options)
                .apply( new RequestOptions().centerCrop()
                        .placeholder(R.mipmap.touxiang)
                        .error(R.mipmap.touxiang)
                        .fallback(R.mipmap.touxiang))
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(imageView);
    }


    /**
     * 加载圆角图片
     * @param context
     * @param url
     * @param imageView
     * @param corner
     */
    public static void putrollCornerImg(Context context, Object url, ImageView imageView, int corner) {
        //设置图片圆角角度
        Glide.with(context)
                .load(url)
//                .apply(new RequestOptions().centerCrop().placeholder(R.mipmap.touxiang)
                .apply(new RequestOptions().centerCrop().placeholder(R.mipmap.jzy_content_pic_cute)
//                .error(R.mipmap.touxiang)
                .error(R.mipmap.jzy_content_pic_cute)
//                .fallback(R.mipmap.touxiang))
                .fallback(R.mipmap.jzy_content_pic_cute))
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(corner)))
                .into(imageView);

    }


    /**
     *   加载一张正常的图片
     * @param context
     * @param url
     * @param imageView
     */
    public static void putHttpImg(Context context, Object url, ImageView imageView) {
        Glide.with(context)
                .load(url)
//                .apply(new RequestOptions().centerCrop().placeholder(R.mipmap.touxiang)
                .apply(new RequestOptions().centerInside().placeholder(R.mipmap.jzy_content_pic_cute)//CENTER_INSIDE
//                .error(R.mipmap.touxiang)
                .error(R.mipmap.jzy_content_pic_cute)
//                .fallback(R.mipmap.touxiang))
                .fallback(R.mipmap.jzy_content_pic_cute))
//                .thumbnail(0.1f)//先显示缩略图  缩略图为原图的1/10
                .into(imageView);
    }

    public static void putHttpImg(Object url, ImageView imageView) {
        Glide.with(imageView)
                .load(url)
//                .apply(new RequestOptions().centerCrop().placeholder(R.mipmap.touxiang)
                .apply(new RequestOptions().centerInside().placeholder(R.mipmap.jzy_content_pic_cute)//CENTER_INSIDE
//                .error(R.mipmap.touxiang)
                .error(R.mipmap.jzy_content_pic_cute)
//                .fallback(R.mipmap.touxiang))
                .fallback(R.mipmap.jzy_content_pic_cute))
//                .thumbnail(0.1f)//先显示缩略图  缩略图为原图的1/10
                .into(imageView);
    }



}
