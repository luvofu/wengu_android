package com.culturebud.ui.me;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.culturebud.BaseActivity;
import com.culturebud.BaseApp;
import com.culturebud.R;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.User;
import com.culturebud.contract.LoginContract;
import com.culturebud.presenter.LoginPresenter;
import com.culturebud.ui.WelcomeActivity;
import com.culturebud.vo.Tag;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.wechat.friends.Wechat;

import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_REGIST;
import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_RETRIEVE_PASSWORD;

/**
 * Created by XieWei on 2016/10/25.
 */

@PresenterInject(LoginPresenter.class)
public class LoginActivity extends BaseActivity<LoginContract.Presenter> implements LoginContract.View,
        PlatformActionListener {
    private static final String TAG = "LoginActivity";
    private EditText etUserName, etPassword;
    private TextView tvRegist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate ==>> " + BaseApp.getInstance());
        setContentView(R.layout.login);
        presenter.setView(this);
        showTitlebar();
        setTitle(R.string.login);
        etUserName = obtainViewById(R.id.et_username);
        etPassword = obtainViewById(R.id.et_password);
        tvRegist = obtainViewById(R.id.tv_regist);
        initData();
        presenter.loadLocalUser();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume ==>> " + BaseApp.getInstance());
        super.onResume();
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
                startActivityForResult(new Intent(this, RetrievePasswordActivity.class),
                        REQUEST_CODE_RETRIEVE_PASSWORD);
                break;
            case R.id.iv_weibo:
                Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                authorize(weibo);
                break;
            case R.id.iv_wechat:
                    Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                authorize(wechat);
                break;
        }
    }
    private void authorize(Platform plat) {
        if (plat == null) {
//            popupOthers();
            onErrorTip("平台为空");
            return;
        }

        if(plat.isAuthValid()) {
            String userId = plat.getDb().getUserId();
            if (userId != null) {
                Log.d(TAG, userId + ", " + plat.getDb().getUserName());
                return;
            }
        }

        plat.setPlatformActionListener(this);
        //关闭SSO授权
        plat.SSOSetting(true);
        plat.showUser(null);
//        plat.authorize();
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

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        Log.i(TAG, platform.getName() + "");
        Log.i(TAG, hashMap + "");
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        Log.e(TAG, "onError()");
        Log.e(TAG, throwable.getMessage());
    }

    @Override
    public void onCancel(Platform platform, int i) {
        Log.e(TAG, "onCancel()");
    }
}
