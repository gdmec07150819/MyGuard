package cn.edu.s07150819gdmec.myguard.m4appmanager.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.edu.gdmec.s317.myguard.m4appmanager.entity.AppInfo;


/**
 * Created by student on 16/12/19.
 */

public class AppInfoParser {
    //获取手机所有应用程序
    public static List<AppInfo> getAppInfo(Context context){
        //得到一个Java保证的包管理器
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> packageInfos  = pm.getInstalledPackages(0);
        List<AppInfo> appInfos = new ArrayList<AppInfo>();
        for (PackageInfo packageInfo : packageInfos){
            AppInfo appInfo = new AppInfo();
            String packageName = packageInfo.packageName;
            appInfo.packageName = packageName;
            Drawable icon = packageInfo.applicationInfo.loadIcon(pm);
            appInfo.icon = icon;
            String appname = packageInfo.applicationInfo.loadLabel(pm).toString();
            appInfo.appName = appname;
            //应用程序包的路径
            String apkpath = packageInfo.applicationInfo.sourceDir;
            appInfo.apkPath = apkpath;
            File file = new File(apkpath);
            long appSize = file.length();
            appInfo.appSize = appSize;
            //应用程序安装路径
            int flags = packageInfo.applicationInfo.flags;
            if ((ApplicationInfo.FLAG_EXTERNAL_STORAGE & flags) != 0) {
                //外部存储
                appInfo.isInRoom = false;
            }else {
                //手机内存
                appInfo.isInRoom = true;
            }
            if ((ApplicationInfo.FLAG_SYSTEM & flags) != 0){
                //系统应用
                appInfo.isUserApp = false;
            }else {
                //用户应用
                appInfo.isUserApp = true;
            }
            appInfos.add(appInfo);
            appInfo = null;
        }
        return appInfos;
    }
}
