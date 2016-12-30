package cn.edu.s07150819gdmec.myguard.m2TheGuard;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import cn.edu.gdmec.s317.myguard.R;


/**
 * Created by student on 16/12/21.
 */
public class SetUp2Activity extends BaseSetUpActivity implements View.OnClickListener{
    private TelephonyManager mTelephonyManager;
    private Button mBindSIMBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up2);
        mTelephonyManager= (TelephonyManager) getSystemService(TELECOM_SERVICE);
        initView();
    }

    private void initView() {
        ((RadioButton)findViewById(R.id.rb_second)).setChecked(true);
        mBindSIMBtn=(Button) findViewById(R.id.btn_bind_sim);
        mBindSIMBtn.setOnClickListener(this);
        if (isBind()){
            mBindSIMBtn.setEnabled(false);
        }else{
            mBindSIMBtn.setEnabled(true);
        }
    }

    private boolean isBind() {
        String simString =sp.getString("sim",null);
        if (TextUtils.isEmpty(simString)){
            return false;
        }
        return true;
    }



    @Override
    protected void showNext() {
    if (!isBind()){
        Toast.makeText(this,"您还没有绑定SIM卡",Toast.LENGTH_SHORT).show();
        return;
    }
        startActivityAndFinishSelf(SetUp3Activity.class);
    }

    @Override
    protected void showPre() {
    startActivityAndFinishSelf(SetUp1Activity.class);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_bind_sim:
                bindSIM();
                break;
        }
    }

    private void bindSIM() {
        if (!isBind()){
            String simSerialNumber=mTelephonyManager.getSimSerialNumber();
            SharedPreferences.Editor edit=sp.edit();
            edit.putString("sim",simSerialNumber);
            edit.commit();
            Toast.makeText(this,"SIM卡绑定成功!",Toast.LENGTH_SHORT).show();
            mBindSIMBtn.setEnabled(false);
        }else{
            Toast.makeText(this,"SIM卡已经绑定!",Toast.LENGTH_SHORT).show();
            mBindSIMBtn.setEnabled(false);
        }

    }
}
