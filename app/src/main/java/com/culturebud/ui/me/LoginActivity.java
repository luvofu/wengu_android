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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.culturebud.BaseActivity;
import com.culturebud.R;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.User;
import com.culturebud.contract.LoginContract;
import com.culturebud.presenter.LoginPresenter;

import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_REGIST;
import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_RETRIEVE_PASSWORD;

/**
 * Created by XieWei on 2016/10/25.
 */

@PresenterInject(LoginPresenter.class)
public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {
    private static final String TAG = "LoginActivity";
    private EditText etUserName, etPassword;
    private TextView tvRegist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        presenter.setView(this);
        showTitlebar();
        setTitle(R.string.login);
        etUserName = obtainViewById(R.id.et_username);
        etPassword = obtainViewById(R.id.et_password);
        tvRegist = obtainViewById(R.id.tv_regist);
        initData();
    }

    private void initData() {
        SpannableString ss = new SpannableString(getString(R.string.register_now));
        ClickableSpan cspan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(LoginActivity.this, "请注册", Toast.LENGTH_SHORT).show();
                startActivityForResult(new Intent(LoginActivity.this, RegistActivity.class), REQUEST_CODE_REGIST);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(android.R.color.holo_red_dark));
                ds.setUnderlineText(false);
                ds.clearShadowLayer();
            }
        };
        ss.setSpan(cspan, 5, 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ForegroundColorSpan fcSpan = new ForegroundColorSpan(getResources().getColor(R.color.tabar_font_checked));
        ss.setSpan(fcSpan, 5, 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvRegist.setText(ss);
        tvRegist.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_login:
                String userName = etUserName.getText().toString();
                String password = etPassword.getText().toString();
                presenter.login(userName, password);
                break;
            case R.id.tv_forget_pwd:
                startActivityForResult(new Intent(this, RetrievePasswordActivity.class), REQUEST_CODE_RETRIEVE_PASSWORD);
                break;
        }
    }

    @Override
    protected void onBack() {
        super.onBack();
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void loginSuccess(User user) {
        Intent intent = getIntent();
        intent.putExtra("res", true);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onLogout(boolean success) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_REGIST:
                if (resultCode == RESULT_OK) {
                    Intent intent = getIntent();
                    intent.putExtra("res", true);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
            case REQUEST_CODE_RETRIEVE_PASSWORD:
                if (resultCode == RESULT_OK) {
                    Intent intent = getIntent();
                    intent.putExtra("res", true);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
        }
    }
}
