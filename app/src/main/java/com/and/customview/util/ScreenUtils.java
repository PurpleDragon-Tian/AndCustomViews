package com.and.customview.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.DisplayMetrics;

/**
 * @author Tianzilong
 * @date 19-5-13 下午5:27
 */
public class ScreenUtils {

    /**
     * @param dp
     * @param context
     * @return
     * @Description: 转换dp到pixel
     */
    public static int dp2px(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int px = (int) (dp * (metrics.densityDpi / 160f));
        return px;
    }

    /**
     * @param px
     * @param context
     * @return
     * @Description:转换像素到dp
     */
    public static int px2dp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int dp = (int) (px / (metrics.densityDpi / 160f));
        return dp;
    }

    public static float getDensity(Context mContext) {
        Resources resources = mContext.getResources();
        DisplayMetrics metrics = new DisplayMetrics();
        return metrics.density;
    }

    /**
     * @param activity
     * @return Point.x为屏幕宽，Point.y为屏幕高
     * @Description: 获取屏幕的宽高
     */
    public static Point getScreenSize(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Point size = new Point();
        size.y = metrics.heightPixels;
        size.x = metrics.widthPixels;
        return size;
    }

    /**
     * @param context
     * @return [0]为屏幕宽，[1]为屏幕高
     * @Description: 获取屏幕的宽高
     */
    public static int[] getScreenSize(Context context) {
        int[] screenSize = new int[2];
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        screenSize[0] = metrics.widthPixels;
        screenSize[1] = metrics.heightPixels;
        return screenSize;
    }

}
