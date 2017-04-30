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
import com.culturebud.contract.MobileBindingContract;
import com.culturebud.presenter.MobileBindingPresenter;
import com.culturebud.util.TxtUtil;

import java.util.Timer;
import java.util.TimerTask;

@PresenterInject(MobileBindingPresenter.class)
public class MobileBindingActivity extends BaseActivity<MobileBindingContract.Presenter> implements
        MobileBindingContract.View {
    private TextView tvHaveBinding, tvGetVerifyCode;
    private EditText etNewMobile, etVerifyCode;
    private Button btnSubmit;

    private String mobile;
    private int recLen = 60;
    private boolean unClickable;
    private String newMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.setView(this);
        setContentView(R.layout.mobile_binding);
        tvHaveBinding = obtainViewById(R.id.have_binding);
        tvGetVerifyCode = obtainViewById(R.id.get_verify_code);
        etNewMobile = obtainViewById(R.id.new_mobile);
        etVerifyCode = obtainViewById(R.id.verify_code);
        btnSubmit = obtainViewById(R.id.submit_btn);
        showTitlebar();
        setListeners();
        initView();
    }

    private void initView() {
        mobile = getIntent().getStringExtra("mobile");
        if (mobile != null) {
            setTitle(R.string.change_binding_mobile);

            etNewMobile.setVisibility(View.GONE);
            tvHaveBinding.setVisibility(View.VISIBLE);
            tvHaveBinding.setText(String.format(getResources().getString(R.string.have_binding_mobile), mobile));
            btnSubmit.setText("下一步");
        } else {
            setTitle(R.string.binding_mobile);

            etNewMobile.setVisibility(View.VISIBLE);
            tvHaveBinding.setVisibility(View.GONE);
            btnSubmit.setText("确认绑定");
        }
    }

    private void setListeners() {
        tvGetVerifyCode.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
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
                    presenter.getValidCode(mobile, CommonConst.SucrityCodeType.TYPE_CHECK_MOBILE);
                } else {
                    String newMobile = etNewMobile.getText().toString();
                    if (!TxtUtil.isChinaPhoneLegal(newMobile)) {
                        onErrorTip(getString(R.string.incorrect_phone_num));
                        return;
                    }
                    presenter.getValidCode(newMobile, CommonConst.SucrityCodeType.TYPE_BIND_MOBILE);
                }

                break;
            }
            case R.id.submit_btn://提交
            {
                String verifyCode = etVerifyCode.getText().toString();
                if (verifyCode == null || verifyCode.length() < 6) {
                    Toast.makeText(this, "验证码错误", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mobile != null) {

                    presenter.checkMobile(mobile, verifyCode);

                } else {
                    newMobile = etNewMobile.getText().toString();
                    if (!TxtUtil.isChinaPhoneLegal(newMobile)) {
                        onErrorTip(getResources().getString(R.string.incorrect_phone_num));
                        return;
                    }
                    presenter.changeMobile(newMobile, verifyCode);

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
                        tvGetVerifyCode.setText("重新获取(" + recLen + ")");
                        if (recLen < 0) {
                            timer.cancel();
                            recLen = 60;
                            unClickable = false;
                            tvGetVerifyCode.setText("获取验证码");
                        }
                    }
                });
            }
        };
        timer.schedule(task, 1000, 1000);
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
        onErrorTip(message);
    }

    @Override
    public void onObtainedCode(boolean result) {
        if (result) {
        }
    }

    @Override
    public void onBindingMobile(boolean result) {
        if (result) {
            BaseApp.getInstance().getUser().setRegMobile(newMobile);
            onErrorTip("绑定成功");
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
