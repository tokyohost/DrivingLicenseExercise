package com.example.drivinglicenseexercise.Utils;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;

/**
 * @author: wuchao
 * @date: 2018/11/2 13:11
 * @desciption: 界面 相关工具类
 */
public class UiUtils {

    private UiUtils() {
    }

    public static UiUtils getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * dip转px
     *
     * @param dip
     * @return
     */
    public static int dip2px(float dip) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    public static Resources getResources() {
        return getContext().getResources();
    }

    /**
     * 获取上下文
     *
     * @return
     */
    public static Context getContext() {
        return getContext();
    }

    /**
     * 屏幕宽度
     *
     * @return
     */
    public static int getScreenWidth() {
        return getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 屏幕高度
     *
     * @return
     */
    public static int getScreenHeight() {
        return getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * px转dip
     *
     * @param px
     * @return
     */
    public static int px2dip(int px) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /**
     * 获取字符数组
     */
    public static String[] getStringArray(int id) {
        return getResources().getStringArray(id);
    }

    /**
     * 获取颜色id
     */
    public static int getColor(int colorId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getResources().getColor(colorId, null);
        } else {
            return getResources().getColor(colorId);
        }
    }

    public static Drawable getDrawable(int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return getResources().getDrawable(id, null);
        } else {
            return getResources().getDrawable(id);
        }
    }

    /**
     * 根据id获取尺寸
     */
    public static int getDimens(int id) {
        return getResources().getDimensionPixelSize(id);
    }

    public static View inflate(int id) {
        return View.inflate(getContext(), id, null);
    }

    public static String getString(int resId) {
        return getContext().getString(resId);
    }

    public static int getTextSize(int id) {
        TypedValue value = new TypedValue();
        getResources().getValue(id, value, true);
        return (int) TypedValue.complexToFloat(value.data);
    }

    /**
     * 状态栏高度
     *
     * @return
     */
    public static int getStatusBarHeight() {
        int statusBarHeight = -1;
        //获取status_bar_height资源的ID
        int resourceId = getResources().getIdentifier("status_bar_height",
                "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    private static final class Holder {
        private static final UiUtils INSTANCE = new UiUtils();
    }
}