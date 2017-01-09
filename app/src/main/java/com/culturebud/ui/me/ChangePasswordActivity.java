package com.culturebud.ui.me;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.culturebud.BaseActivity;
import com.culturebud.R;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.User;
import com.culturebud.contract.ChangePasswordContract;
import com.culturebud.presenter.ChangePasswordPresenter;

/**
 * Created by XieWei on 2016/11/18.
 */

@PresenterInject(ChangePasswordPresenter.class)
public class ChangePasswordActivity extends BaseActivity<ChangePasswordContract.Presenter>
        implements ChangePasswordContract.View {
    private EditText etCurrentPwd, etNewPwd, etNewPwdConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);
        presenter.setView(this);
        showTitlebar();
        setTitle(R.string.change_pwd);
        etCurrentPwd = obtainViewById(R.id.et_current_pwd);
        etNewPwd = obtainViewById(R.id.et_new_pwd);
        etNewPwdConfirm = obtainViewById(R.id.et_new_pwd_confirm);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_confirm:
                presenter.changePwd(etCurrentPwd.getText().toString(),
                        etNewPwd.getText().toString(),
                        etNewPwdConfirm.getText().toString());
                break;
        }
    }

    @Override
    protected void onBack() {
        super.onBack();
        finish();
    }

    @Override
    public void onUser(User user) {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onErrorTip(String tip) {
        Toast.makeText(this, tip, Toast.LENGTH_SHORT).show();
    }
}
