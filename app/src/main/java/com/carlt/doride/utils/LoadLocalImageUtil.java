package com.carlt.doride.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by Bert on 2016/8/18.
 */
public class LoadLocalImageUtil {
    private LoadLocalImageUtil() {
    }

    private static LoadLocalImageUtil instance = null;

    public static synchronized LoadLocalImageUtil getInstance() {
        if (instance == null) {
            instance = new LoadLocalImageUtil();
        }
        return instance;
    }

    /**
     * 从内存卡中异步加载本地图片无缓存
     * @param uri
     * @param imageView
     */
    public void displayFromSDCard(Object uri, ImageView imageView) {
        Glide.with(UiUtils.getContext())
                .load(uri)
                .into(imageView);

    }

    /**
     * 从内存卡中异步加载本地图片
     * @param uri
     * @param imageView
     */
    public void displayFromSDCardCache(String uri, ImageView imageView) {
        Glide.with(UiUtils.getContext())
                .load(uri)
                .into(imageView);

    }


    /**
     * 从drawable中异步加载本地图片
     * @param imageId
     * @param imageView
     */
    public void displayFromDrawable(int imageId, ImageView imageView) {
        Glide.with(UiUtils.getContext())
                .load(imageId)
                .into(imageView);
    }


    /**
     * 加载网络图片从网络
     * @param uri
     * @param imageView
     * @param defaulePic
     */
    public void displayFromWeb(String uri, ImageView imageView, int defaulePic) {
        Glide.with(UiUtils.getContext())
                .load(uri)
                .into(imageView);
    }

    /**
     * 从SD显示圆头像
     * @param uri
     * @param imageView
     * @param defaulePic
     */
    public void displayCircleFromSD(String uri, ImageView imageView, int defaulePic) {
        Glide.with(UiUtils.getContext())
                .load(uri)
                .dontAnimate()
                .transform(new GlideCircleTransform(UiUtils.getContext()))
                .error(defaulePic)
                .into(imageView);
    }

    /**
     * 从网络显示圆头像
     * @param uri
     * @param imageView
     * @param defaulePic
     */
    public void displayCircleFromWeb(String uri, ImageView imageView, int defaulePic) {
        Glide.with(UiUtils.getContext())
                .load(uri)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .skipMemoryCache(true)
                .transform(new GlideCircleTransform(UiUtils.getContext()))
                .error(defaulePic)
                .into(imageView);
    }

    /**
     * 显示圆角矩形从网络
     * @param uri
     * @param imageView
     * @param defaulePic
     * @param radius
     */
    public void displayRoundFromWeb(String uri, ImageView imageView, int defaulePic, int radius) {
        Glide.with(UiUtils.getContext())
                .load(uri)
                .transform(new GlideRoundTransform(UiUtils.getContext(), radius))
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .skipMemoryCache(true)
                .placeholder(defaulePic)
                .error(defaulePic)
                .into(imageView);
    }
}
