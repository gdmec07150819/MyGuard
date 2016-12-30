package cn.edu.s07150819gdmec.myguard.m3communicationguard.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsMessage;

import cn.edu.gdmec.s317.myguard.m3communicationguard.db.dao.BlackNumberDao;


/**
 * Created by Administrator on 2016/12/21.
 */
public class InterceptSmsReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences mSP=context.getSharedPreferences("config",Context.MODE_PRIVATE);
        boolean BlackNumStatus=mSP.getBoolean("BlackNumStatus",true);
        if (BlackNumStatus){
            return;
        }
        //黑名单终止广播
        BlackNumberDao dao = new BlackNumberDao(context);
        Object[] objs = (Object[]) intent.getExtras().get("pdus");
        for (Object obj : objs){
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
            String sender = smsMessage.getOriginatingAddress();
            String body = smsMessage.getEmailBody();
            if (sender.startsWith("+86")){
                sender = sender.substring(3,sender.length());
            }
            int mode = dao.getBlackContactMode(sender);
            if (mode == 2 || mode == 3){
                abortBroadcast();
            }
        }
    }
}
