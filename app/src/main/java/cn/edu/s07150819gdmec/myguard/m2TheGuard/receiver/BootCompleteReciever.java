package cn.edu.s07150819gdmec.myguard.m2TheGuard.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import cn.edu.gdmec.s317.myguard.App;


/**
 * Created by student on 16/12/21.
 */

public class BootCompleteReciever extends BroadcastReceiver{
    private static final String TAG=BootCompleteReciever.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        ((App) context.getApplicationContext()).correctSIM();
    }
}
