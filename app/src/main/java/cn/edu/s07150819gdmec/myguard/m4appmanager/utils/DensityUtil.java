package cn.edu.s07150819gdmec.myguard.m4appmanager.utils;

import android.content.Context;

/**
 * Created by student on 16/12/19.
 */

public class DensityUtil {
    //dip转换成像素
    public static int dip2px(Context context,float dpValue){
        try {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int)(dpValue * scale + 0.5f);
        }catch (Exception e){
            e.printStackTrace();
        }
        return (int) dpValue;
    }

    //像素转换成dip
    public static int px2dip(Context context,float pxValue){
        try {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (pxValue / scale + 0.5f);
        }catch (Exception e){
            e.printStackTrace();
        }
        return (int) pxValue;
    }
}
