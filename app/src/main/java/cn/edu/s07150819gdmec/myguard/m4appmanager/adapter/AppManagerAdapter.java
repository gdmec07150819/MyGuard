package cn.edu.s07150819gdmec.myguard.m4appmanager.adapter;


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.edu.gdmec.s317.myguard.R;
import cn.edu.gdmec.s317.myguard.m4appmanager.entity.AppInfo;
import cn.edu.gdmec.s317.myguard.m4appmanager.utils.DensityUtil;
import cn.edu.gdmec.s317.myguard.m4appmanager.utils.EngineUtils;


public class AppManagerAdapter extends BaseAdapter {

    private List<AppInfo> UserAppInfos;
    private List<AppInfo> SystemAppInfos;
    private Context context;
    private TextView textView;

    public AppManagerAdapter(List<AppInfo> userAppInfos,
                             List<AppInfo> systemAppInfos, Context context) {
        super();
        UserAppInfos = userAppInfos;
        SystemAppInfos = systemAppInfos;
        this.context = context;
    }


    @Override
    public int getCount() {
        //因为有两个条目需要显示用户进程，所以需要加2
        return UserAppInfos.size() + SystemAppInfos.size() + 2;
    }

    @Override
    public Object getItem(int position) {
        if (position == 0) {
            //第0个位置显示应该是用户程序个数的标签
            return null;
        } else if (position == (UserAppInfos.size() + 1)) {
            return null;
        }
        AppInfo appInfo;
        if (position < (UserAppInfos.size() + 1)) {
            //用户程序
            appInfo = UserAppInfos.get(position - 1);
            //多了一个textview标签所以减1
        } else {
            //系统程序
            int location = position - UserAppInfos.size() - 2;
            appInfo = SystemAppInfos.get(location);
        }
        return appInfo;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //如果position为0，则是textview
        if (position == 0) {
            TextView tv = getTextView();
            tv.setText("用户程序：" + UserAppInfos.size() + "个");
            return tv;
        } else if (position == UserAppInfos.size() + 1) {
            TextView tv = getTextView();
            tv.setText("系统程序：" + SystemAppInfos.size() + "个");
            return tv;
        }


        //获取当前的app对象
        AppInfo appInfo;
        if (position < (UserAppInfos.size() + 1)) {
            //如果position为0，则是textview
            appInfo = UserAppInfos.get(position - 1);
        } else {
            //系统应用
            appInfo = SystemAppInfos.get(position - UserAppInfos.size() - 2);
        }
        ViewHolder viewHolder = null;
        if (convertView != null & convertView instanceof LinearLayout) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_appmanager_list, null);
            viewHolder.mAppIconImgv = (ImageView) convertView.findViewById(R.id.imgv_appicon);
            viewHolder.mAppLocationTV = (TextView) convertView.findViewById(R.id.tv_appisroom);
            viewHolder.mAppSizeTV = (TextView) convertView.findViewById(R.id.tv_appsize);
            viewHolder.mAppNameTV = (TextView) convertView.findViewById(R.id.tv_appname);
            viewHolder.mLuanchAppTV = (TextView) convertView.findViewById(R.id.tv_launch_app);
            viewHolder.mSettingAppTV = (TextView) convertView.findViewById(R.id.tv_setting_app);
            viewHolder.mShareAppTV = (TextView) convertView.findViewById(R.id.tv_share_app);
            viewHolder.mUninstallAppTV = (TextView) convertView.findViewById(R.id.tv_uninstall_app);
            viewHolder.mAppOptionLL = (TextView) convertView.findViewById(R.id.ll_option_app);
            convertView.setTag(viewHolder);
        }
        if (appInfo != null) {
            viewHolder.mAppLocationTV.setText(appInfo.getApplocation(appInfo.isInRoom));
            viewHolder.mAppIconImgv.setImageDrawable(appInfo.icon);
            viewHolder.mAppSizeTV.setText(Formatter.formatFileSize(context, appInfo.appSize));
            viewHolder.mAppNameTV.setText(appInfo.appName);
            if (appInfo.isSelected) {
                viewHolder.mAppOptionLL.setVisibility(View.VISIBLE);
            } else {
                viewHolder.mAppOptionLL.setVisibility(View.GONE);
            }
        }
        MyClickListener listener = new MyClickListener(appInfo);
        viewHolder.mLuanchAppTV.setOnClickListener(listener);
        viewHolder.mSettingAppTV.setOnClickListener(listener);
        viewHolder.mShareAppTV.setOnClickListener(listener);
        viewHolder.mUninstallAppTV.setOnClickListener(listener);

        return convertView;
    }

    public TextView getTextView() {
        TextView tv = new TextView(context);
        tv.setBackgroundColor(ContextCompat.getColor(context,R.color.graye5));
        tv.setPadding(DensityUtil.dip2px(context,5),
                DensityUtil.dip2px(context,5),
                DensityUtil.dip2px(context,5),
                DensityUtil.dip2px(context,5));
        tv.setTextColor(ContextCompat.getColor(context,R.color.black));
        return tv;
    }

    static class ViewHolder{
        //启动app
        TextView mLuanchAppTV;
        //卸载app
        TextView mUninstallAppTV;
        //分享app
        TextView mShareAppTV;
        //设置app
        TextView mSettingAppTV;
        //app图标
        ImageView mAppIconImgv;
        //app位置
        TextView mAppLocationTV;
        //app大小
        TextView mAppSizeTV;
        //app名称
        TextView mAppNameTV;
        //操作app线性布局
        TextView mAppOptionLL;
    }

    class MyClickListener implements View.OnClickListener{
        private AppInfo appInfo;

        public MyClickListener(AppInfo appInfo){
            super();
            this.appInfo = appInfo;
        }
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_launch_app:
                    EngineUtils.startApplication(context,appInfo);
                    break;
                case R.id.tv_share_app:
                    EngineUtils.shareApplication(context,appInfo);
                    break;
                case R.id.tv_setting_app:
                    EngineUtils.SettingAppDetail(context,appInfo);
                    break;
                case R.id.tv_uninstall_app:
                    if (appInfo.packageName.equals(context.getPackageName())){
                        Toast.makeText(context,"您没有权限卸载此应用！",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    EngineUtils.unistallApplication(context,appInfo);
                    break;
            }
        }
    }
}
