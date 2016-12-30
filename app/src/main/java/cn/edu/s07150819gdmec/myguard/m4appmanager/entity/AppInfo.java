package cn.edu.s07150819gdmec.myguard.m4appmanager.entity;

import android.graphics.drawable.Drawable;

/**
 * Created by student on 16/12/19.
 */

public class AppInfo {
    public String packageName;
    public Drawable icon;
    public String appName;
    public String apkPath;
    public long appSize;
    public boolean isInRoom;
    public boolean isUserApp;
    public boolean isSelected = false;


    //获取手机app位置字符串
    public String getApplocation(boolean isInRoom){
        if (isInRoom){
            return "手机内存";
        }else {
            return "外部存储";
        }
    }
}
