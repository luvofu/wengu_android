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

import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_CHANGEMOBILEBIND;

@PresenterInject(MobileBindingPresenter.class)
public class MobileBindingActivity extends BaseActivity<MobileBindingContract.Presenter> implements
        MobileBindingContract.View {
    private TextView tvHaveBinding, tvGetVerifyCode;
    private EditText etNewMobile, etVerifyCode;
    private Button btnSubmit;

    private String mobile;
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
                if (mobile != null) {
                    presenter.getValidCode(mobile, CommonConst.SucrityCodeType.TYPE_CHECK_MOBILE);
                } else {
                    String newMobile = etNewMobile.getText().toString();
                    presenter.getValidCode(newMobile, CommonConst.SucrityCodeType.TYPE_BIND_MOBILE);
                }

                break;
            }
            case R.id.submit_btn://提交
            {
                String verifyCode = etVerifyCode.getText().toString();
                if (verifyCode.length() <= 0) {
                    Toast.makeText(this, "验证码错误", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mobile != null) {
                    presenter.checkMobile(mobile, verifyCode);
                } else {
                    newMobile = etNewMobile.getText().toString();
                    presenter.changeMobile(newMobile, verifyCode);
                }
                break;
            }
        }
    }

    @Override
    protected void onBack() {
        super.onBack();
        finish();
    }

    @Override
    public void onInvalidate(String message) {
        onErrorTip(message);
    }

    @Override
    public void onObtainedCode(boolean result) {
        if (result) {
            tvGetVerifyCode.setEnabled(false);
            tvGetVerifyCode.setClickable(false);
            presenter.countDown();
        }
    }

    @Override
    public void onBindingMobile(boolean result) {
        if (result) {
            BaseApp.getInstance().getUser().setRegMobile(newMobile);
            presenter.updateLocalUser(BaseApp.getInstance().getUser());
            onErrorTip("绑定成功");

            //绑定成功，需要回到上级页面.
            Intent intent = new Intent(this, AccountBindActivity.class);
            intent.putExtra("requestcode", REQUEST_CODE_CHANGEMOBILEBIND);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
    }

    @Override
    public void onCheckMobile(boolean result) {
        if (result) {
            Intent intent = new Intent(this, MobileBindingActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onCountDown(int second) {
        if (second < 0) {
            tvGetVerifyCode.setText("获取验证码");
            tvGetVerifyCode.setEnabled(true);
            tvGetVerifyCode.setClickable(true);
        } else {
            tvGetVerifyCode.setText("重新获取（" + second + "）");
        }
    }
}
