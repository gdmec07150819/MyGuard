package cn.edu.s07150819gdmec.myguard.m1home.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import cn.edu.gdmec.s317.myguard.R;
import cn.edu.gdmec.s317.myguard.m1home.HomeActivity;
import cn.edu.gdmec.s317.myguard.m1home.entity.VersionEntity;


/**
 * Created by student on 16/12/19.
 */

public class VersionUpdateUtils {
    private static final int MESSAGE_NET_EEOR=101;
    private static final int MESSAGE_IO_EEOR=102;
    private static final int MESSAGE_JSON_EEOR=103;
    private static final int MESSAGE_SHOEW_DIALOG=104;
    private static final int MESSAGE_ENTERHOME=105;

    private Handler handler=new Handler() {
        public void handleMessage(android.os.Message msg){
            switch (msg.what){
                case MESSAGE_IO_EEOR:
                    Toast.makeText(context,"IO异常",Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case MESSAGE_JSON_EEOR:
                    Toast.makeText(context,"JSON解析异常",Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case MESSAGE_NET_EEOR:
                    Toast.makeText(context,"网络异常",Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case MESSAGE_SHOEW_DIALOG:
                    showUpdateDialog(versionEntity);
                    enterHome();
                    break;
                case MESSAGE_ENTERHOME:
                    Intent intent=new Intent(context, HomeActivity.class);
                    context.startActivity(intent);
                    context.finish();
                    break;
            }
        };
    };


    private String mVersion;
    private Activity context;
    private ProgressDialog mProgressDialog;
    private VersionEntity versionEntity;
    private Object cloudVersion;


    public VersionUpdateUtils(String Version,Activity activity){
        mVersion =Version;
        context=activity;
    }
    /*------------------------  */
    public void  getCloudVersion(){
        try {
            HttpClient client=new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(client.getParams(),5000);
            HttpConnectionParams.setSoTimeout(client.getParams(),5000);
            HttpGet httpGet=new HttpGet("http://172.16.25.14:8080/updateinfo.html");
            HttpResponse execute=client.execute(httpGet);
            if (execute.getStatusLine().getStatusCode()==200) {
                HttpEntity entity = execute.getEntity();
                String result = EntityUtils.toString(entity, "gbk");
                JSONObject jsonObject = new JSONObject(result);
                versionEntity = new VersionEntity();
                String code = jsonObject.getString("code");
                versionEntity.versioncode = code;
                String des = jsonObject.getString("des");
                versionEntity.description = des;
                String apkurl = jsonObject.getString("apkurl");
                versionEntity.apkurl = apkurl;
                if (!mVersion.equals(versionEntity.versioncode)) {
                    handler.sendEmptyMessage(MESSAGE_SHOEW_DIALOG);
                }
            }
        }catch (ClientProtocolException e){
            handler.sendEmptyMessage(MESSAGE_NET_EEOR);
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void enterHome() {
        handler.sendEmptyMessageDelayed(MESSAGE_ENTERHOME,2000);
    }
    private void showUpdateDialog(final VersionEntity versionEntity){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle("检查到新的版本:"+versionEntity.versioncode);
        builder.setMessage(versionEntity.description);
        builder.setIcon(R.mipmap.ic_launcher);//报错
        builder.setPositiveButton("立即升级", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                initProgressDialog();
                downloadnewApk(versionEntity.apkurl);
            }
        });
        builder.setNegativeButton("暂不升级", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                enterHome();
            }
        });
        builder.show();
    }

    private void initProgressDialog(){
        mProgressDialog=new ProgressDialog(context);
        mProgressDialog.setMessage("准备下载。。。");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.show();
    }
    protected void downloadnewApk(String apkurl){
        DownLoadUtils downLoadUtils=new DownLoadUtils();
        downLoadUtils.downapk(apkurl, "/mnt/sdcard/mobilesafe2.0.apk", new MyCallBack() {
            @Override
            public void onSuccess(ResponseInfo<File> arg0) {
                mProgressDialog.dismiss();
                MyUtils.installApk(context);
            }

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                mProgressDialog.setMessage("下载失败");
                mProgressDialog.dismiss();
                enterHome();

            }

            @Override
            public void onLoadding(long total, long current, boolean isUploading) {
                mProgressDialog.setMax((int) total);
                mProgressDialog.setMessage("正在下载...");
                mProgressDialog.setProgress((int) current);

            }
        });
    }


}

