package cn.edu.s07150819gdmec.myguard.m4appmanager.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

import com.stericson.RootTools.RootTools;

import cn.edu.gdmec.s317.myguard.m4appmanager.entity.AppInfo;


/**
 * Created by student on 16/12/19.
 */

public class EngineUtils {
    //分享应用

    public static void shareApplication(Context context, AppInfo appInfo){
        Intent intent = new Intent("android.intent.action.SEND");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,"推荐您使用一款应用，名称叫："
                + appInfo.appName + "下载路径：http://play/google.com/store/app/details?id="
                + appInfo.packageName);
        context.startActivity(intent);
    }

    //开启应用程序
    public static void startApplication(Context context,AppInfo appInfo){
        PackageManager pm = context.getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(appInfo.packageName);
        if (intent != null){
            context.startActivity(intent);
        }else {
            Toast.makeText(context,"该应用没有启动界面",Toast.LENGTH_SHORT).show();
        }
    }

    //开启应用设置界面
    public static void SettingAppDetail(Context context,AppInfo appInfo){
        Intent intent = new Intent();
        intent.setAction("android.setting.APPLICATION_DETAILS_SETTINGS");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse("package:" + appInfo.packageName));
        context.startActivity(intent);
    }

    //卸载应用
    public static void unistallApplication(Context context,AppInfo appInfo){
        if (appInfo.isUserApp){
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_DELETE);
            intent.setData(Uri.parse("packageName:" + appInfo.packageName));
            context.startActivity(intent);
        }else {
            //系统应用，root权限
            if (!RootTools.isRootAvailable()){
                Toast.makeText(context,"卸载系统应用，必须要有root权限",Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                if (!RootTools.isAccessGiven()){
                    Toast.makeText(context,"请授权黑马小卫士root权限",Toast.LENGTH_SHORT).show();
                    return;
                }
                RootTools.sendShell("mount -o remount ,rw/system",3000);
                RootTools.sendShell("rm -r" +appInfo.apkPath,30000);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
