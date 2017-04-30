package com.culturebud.ui.me;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.culturebud.BaseActivity;
import com.culturebud.BaseApp;
import com.culturebud.CommonConst;
import com.culturebud.R;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.User;
import com.culturebud.contract.MobileBindingContract;
import com.culturebud.presenter.MobileBindingPresenter;
import com.culturebud.util.TxtUtil;

import java.util.Timer;
import java.util.TimerTask;

@PresenterInject(MobileBindingPresenter.class)
public class MobileBindingActivity extends BaseActivity<MobileBindingContract.Presenter> implements MobileBindingContract.View {
    private TextView haveBindingView, getVerifyCodeView;
    private EditText newMobileView, verifyCodeView;
    private Button submitBtn;

    private String mobile;
    private int recLen = 60;
    private boolean unClickable;
    private User user;
    private String newMobile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobile_binding);
        haveBindingView = obtainViewById(R.id.have_binding);
        getVerifyCodeView = obtainViewById(R.id.get_verify_code);
        newMobileView = obtainViewById(R.id.new_mobile);
        verifyCodeView = obtainViewById(R.id.verify_code);
        submitBtn = obtainViewById(R.id.submit_btn);
        user = BaseApp.getInstance().getUser();
        presenter.setView(this);
        showTitlebar();
        setListeners();
        initView();
    }

    private void initView() {
        mobile = getIntent().getStringExtra("mobile");
        if (mobile != null) {
            setTitle(R.string.change_binding_mobile);

            newMobileView.setVisibility(View.GONE);
            haveBindingView.setVisibility(View.VISIBLE);
            haveBindingView.setText(String.format(getResources().getString(R.string.have_binding_mobile), mobile));
            submitBtn.setText("下一步");
        } else {
            setTitle(R.string.binding_mobile);

            newMobileView.setVisibility(View.VISIBLE);
            haveBindingView.setVisibility(View.GONE);
            submitBtn.setText("确认绑定");
        }
    }

    private void setListeners() {
        getVerifyCodeView.setOnClickListener(this);
        submitBtn.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.get_verify_code://获取验证码
            {
                if (unClickable) {
                    return;
                } else {
                    enableGetCode();
                }

                if (mobile != null) {
                    presenter.getValidcode(user.getToken(), mobile, CommonConst.SucrityCodeType.TYPE_CHECK_MOBILE);
                } else {
                    String newMobile = newMobileView.getText().toString();
                    if (!TxtUtil.isChinaPhoneLegal(newMobile)) {
                        Toast.makeText(this, getResources().getString(R.string.incorrect_phone_num), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    presenter.getValidcode(user.getToken(), newMobile, CommonConst.SucrityCodeType.TYPE_BIND_MOBILE);
                }

                break;
            }
            case R.id.submit_btn://提交
            {
                String verifyCode = verifyCodeView.getText().toString();
                if(verifyCode == null || verifyCode.length()<6){
                    Toast.makeText(this,"验证码错误", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mobile != null) {

                    presenter.checkMobile(user.getToken(),mobile,verifyCode);

                } else {
                    newMobile = newMobileView.getText().toString();
                    if (!TxtUtil.isChinaPhoneLegal(newMobile)) {
                        Toast.makeText(this, getResources().getString(R.string.incorrect_phone_num), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    presenter.changeMobile(user.getToken(), newMobile, verifyCode);

                }
                break;
            }
        }
    }

    private void enableGetCode() {
        unClickable = true;
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {// UI thread    
                    @Override
                    public void run() {
                        recLen--;
                        getVerifyCodeView.setText("重新获取("+recLen+")");
                        if (recLen < 0) {
                            timer.cancel();
                            recLen = 60;
                            unClickable = false;
                            getVerifyCodeView.setText("获取验证码");
                        }
                    }
                });
            }
        };
        timer.schedule(task,1000,1000);
    }

    @Override
    protected void onBack() {
        super.onBack();
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    @Override
    public void onInvalidate(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onObtainedCode(boolean result) {
        if (result) {
        }
    }

    @Override
    public void onBindingMobile(boolean result) {
        if (result) {
            user.setRegMobile(newMobile);
            BaseApp.getInstance().setUser(user);
            Toast.makeText(this, "绑定成功", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCheckMobile(boolean result) {
        if (result) {
            Intent intent = new Intent(this, MobileBindingActivity.class);
            startActivity(intent);
        }
    }
}
