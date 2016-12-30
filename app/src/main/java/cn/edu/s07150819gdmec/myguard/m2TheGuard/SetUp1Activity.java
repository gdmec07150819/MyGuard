package cn.edu.s07150819gdmec.myguard.m2TheGuard;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.Toast;

import cn.edu.gdmec.s317.myguard.R;


public class SetUp1Activity extends BaseSetUpActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up1);
        initView();
    }
    private void initView() {
        ((RadioButton)findViewById(R.id.rb_first)).setChecked(true);
    }
    @Override
    protected void showNext() {
        startActivityAndFinishSelf(SetUp2Activity.class);

    }

    @Override
    protected void showPre() {
        Toast.makeText(this,"当前页面已经时第一页",Toast.LENGTH_SHORT).show();
    }



}
