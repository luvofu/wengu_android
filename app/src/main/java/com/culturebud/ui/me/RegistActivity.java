package com.culturebud.ui.me;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.culturebud.BaseActivity;
import com.culturebud.R;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.User;
import com.culturebud.contract.RegistContract;
import com.culturebud.presenter.RegistPresenter;

/**
 * Created by XieWei on 2016/11/17.
 */

@PresenterInject(RegistPresenter.class)
public class RegistActivity extends BaseActivity<RegistContract.Presenter>
        implements RegistContract.View {
    private TextView tvLogin, tvAgreement, tvGetSucrityCode;
    private EditText etPhone, etSecurityCode, etPwd, etPwdConfirm;
    private Button btnRegist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regist);
        presenter.setView(this);
        showTitlebar();
        setTitle(R.string.regist);
        tvAgreement = obtainViewById(R.id.tv_agree_protocol);
        tvLogin = obtainViewById(R.id.tv_login);
        etPhone = obtainViewById(R.id.et_phone_number);
        etSecurityCode = obtainViewById(R.id.et_security_code);
        etPwd = obtainViewById(R.id.et_password);
        etPwdConfirm = obtainViewById(R.id.et_password_confirm);
        btnRegist = obtainViewById(R.id.btn_regist);
        tvGetSucrityCode = obtainViewById(R.id.tv_obtain_code);
        initData();
        setListeners();
    }

    private void setListeners() {
        btnRegist.setOnClickListener(this);
        tvGetSucrityCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_obtain_code:
                presenter.getSucrityCode(etPhone.getText().toString());
                break;
            case R.id.btn_regist:
                presenter.regist(etPhone.getText().toString(), etSecurityCode.getText().toString(),
                        etPwd.getText().toString(), etPwdConfirm.getText().toString());
                break;
        }
    }

    private void initData() {
        SpannableString loginSS = new SpannableString(tvLogin.getText());
        loginSS.setSpan(clickSpan, 6, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ForegroundColorSpan fcSpan = new ForegroundColorSpan(getResources().getColor(R.color.tabar_font_checked));
        loginSS.setSpan(fcSpan, 6, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvLogin.setText(loginSS);
        tvLogin.setMovementMethod(LinkMovementMethod.getInstance());

        SpannableString agreeSS = new SpannableString(tvAgreement.getText());
        agreeSS.setSpan(clickSpan, 8, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        agreeSS.setSpan(fcSpan, 8, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvAgreement.setText(agreeSS);
        tvAgreement.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private ClickableSpan clickSpan = new ClickableSpan() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_login:
                    finish();
                    break;
                case R.id.tv_agree_protocol:
                    startActivity(new Intent(RegistActivity.this, UserAgreementActivity.class));
                    break;
            }
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(getResources().getColor(android.R.color.holo_red_dark));
            ds.setUnderlineText(false);
            ds.clearShadowLayer();
        }
    };

    @Override
    protected void onBack() {
        super.onBack();
        finish();
    }

    @Override
    public void onRegist(User user) {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onInvalidate(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onObtainedCode(boolean hasSend) {
        if (hasSend) {
            tvGetSucrityCode.setEnabled(false);
            tvGetSucrityCode.setClickable(false);
            presenter.countDown();
        }
    }

    @Override
    public void onCountDown(int second) {
        if (second < 0) {
            tvGetSucrityCode.setText("获取验证码");
            tvGetSucrityCode.setEnabled(true);
            tvGetSucrityCode.setClickable(true);
        } else {
            tvGetSucrityCode.setText("重新获取（" + second + "）");
        }
    }
}
