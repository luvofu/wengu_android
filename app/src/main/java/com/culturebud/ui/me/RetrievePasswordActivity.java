package com.culturebud.ui.me;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.culturebud.BaseActivity;
import com.culturebud.R;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.User;
import com.culturebud.contract.RetrievePasswordContract;
import com.culturebud.presenter.RetrievePasswordPresenter;

/**
 * Created by XieWei on 2016/11/17.
 */

@PresenterInject(RetrievePasswordPresenter.class)
public class RetrievePasswordActivity extends BaseActivity<RetrievePasswordContract.Presenter>
        implements RetrievePasswordContract.View {
    private EditText etPhone, etCode, etPwd, etPwdConfirm;
    private TextView tvGetCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.retrieve_password);
        presenter.setView(this);
        showTitlebar();
        setTitle("找回密码");
        etPhone = obtainViewById(R.id.et_phone_number);
        etCode = obtainViewById(R.id.et_security_code);
        etPwd = obtainViewById(R.id.et_password);
        etPwdConfirm = obtainViewById(R.id.et_password_confirm);
        tvGetCode = obtainViewById(R.id.tv_obtain_code);
        tvGetCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_obtain_code:
                presenter.getSucrityCode(etPhone.getText().toString());
                break;
            case R.id.btn_reset:
                presenter.retrievePassword(etPhone.getText().toString(), etCode.getText().toString(),
                        etPwd.getText().toString(), etPwdConfirm.getText().toString());
                break;
        }
    }

    @Override
    protected void onBack() {
        super.onBack();
        finish();
    }

    @Override
    public void onRetrieve(User user) {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onObtainedCode(boolean res) {
        if (res) {
            tvGetCode.setEnabled(res);
            tvGetCode.setClickable(false);
            presenter.countDown();
        }
    }

    @Override
    public void onCountDown(int second) {
        if (second < 0) {
            tvGetCode.setText("获取验证码");
            tvGetCode.setEnabled(true);
            tvGetCode.setClickable(true);
        } else {
            tvGetCode.setText("重新获取（" + second + "）");
        }
    }

}
