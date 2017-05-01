package com.culturebud.ui.me;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.culturebud.BaseActivity;
import com.culturebud.BaseApp;
import com.culturebud.CommonConst;
import com.culturebud.R;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.User;
import com.culturebud.contract.LoginContract;
import com.culturebud.presenter.LoginPresenter;

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
    private boolean needCancelToHome = true;

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

    private Dialog ppwBinding;
    private EditText etPhone, etValidCode;
    private TextView tvObtainCode;

    private void initBindingDlg() {
        if (ppwBinding == null) {
            ppwBinding = new Dialog(this);
            ppwBinding.getWindow().setBackgroundDrawable(new ColorDrawable(0x55333333));
            View view = LayoutInflater.from(this).inflate(R.layout.dlg_binding_login, null);
            etPhone = obtainViewById(view, R.id.et_phone_number);
            etValidCode = obtainViewById(view, R.id.et_security_code);
            tvObtainCode = obtainViewById(view, R.id.tv_obtain_code);
            ppwBinding.setContentView(view);
            ppwBinding.setCancelable(false);
        }
    }

    private void showBindingDlg() {
        initBindingDlg();
        etPhone.setText("");
        etValidCode.setText("");
        ppwBinding.show();
        WindowManager.LayoutParams params = ppwBinding.getWindow().getAttributes();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        params.width = dm.widthPixels - getResources().getDimensionPixelSize(R.dimen.common_margin_big);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        ppwBinding.getWindow().setAttributes(params);
    }

    private void hideBindingDlg() {
        if (ppwBinding != null && ppwBinding.isShowing()) {
            ppwBinding.dismiss();
        }
    }

    private void initData() {
        needCancelToHome = getIntent().getBooleanExtra("need_cancel_to_home", true);
        SpannableString ss = new SpannableString(getString(R.string.register_now));
        ClickableSpan cspan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
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

    public void onCloseBinding(View view) {
        hideBindingDlg();
    }

    private long lastThirdLogin;

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
                if (System.currentTimeMillis() - lastThirdLogin < 3000) {
                    return;
                }
                lastThirdLogin = System.currentTimeMillis();
                Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                authorize(weibo);
                break;
            case R.id.iv_wechat:
                if (System.currentTimeMillis() - lastThirdLogin < 3000) {
                    return;
                }
                lastThirdLogin = System.currentTimeMillis();
                Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                authorize(wechat);
                break;
            case R.id.btn_confirm:
                presenter.thirdBindLogin(etValidCode.getText().toString(), etPhone.getText().toString(), thirdType, uid,
                        nickname, sex, null, null, avatar);
                break;
            case R.id.tv_obtain_code:
                presenter.getSecurityCode(etPhone.getText().toString(), thirdType);
                break;
        }
    }

    private void authorize(Platform plat) {
        if (plat == null) {
            onErrorTip("平台为空");
            return;
        }

        if (plat.isAuthValid()) {
            plat.removeAccount(true);
        }

        plat.setPlatformActionListener(this);
        //关闭SSO授权
        plat.SSOSetting(true);
        plat.authorize();
        plat.showUser(null);
    }

    @Override
    protected void onBack() {
        super.onBack();
        if (needCancelToHome) {
            setResult(RESULT_CANCELED_CUSTOM);
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (needCancelToHome) {
            setResult(RESULT_CANCELED_CUSTOM);
        }
        finish();
    }

    @Override
    public void loginSuccess(User user) {
        hideBindingDlg();
        Intent intent = getIntent();
        intent.putExtra("res", true);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onLogout(boolean success) {

    }

    @Override
    public void onNeedBindPhone() {
        showBindingDlg();
    }

    @Override
    public void onObtainedCode(boolean success) {
        if (tvObtainCode == null) {
            return;
        }
        if (success) {
            tvObtainCode.setEnabled(false);
            tvObtainCode.setClickable(false);
            presenter.countDown();
        }
    }

    @Override
    public void onCountDown(int second) {
        if (tvObtainCode == null) {
            return;
        }
        if (second < 0) {
            tvObtainCode.setText("获取验证码");
            tvObtainCode.setEnabled(true);
            tvObtainCode.setClickable(true);
        } else {
            tvObtainCode.setText("重新获取（" + second + "）");
        }
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

    private String uid, nickname, avatar;
    private int sex, thirdType;

    @Override
    public void onComplete(Platform platform, int action, HashMap<String, Object> hashMap) {
        Log.i(TAG, platform.getName() + "");
        Log.i(TAG, hashMap + "");
        Log.i(TAG, platform.getDb().getUserId() + ", " + platform.getDb().getUserName() + ", " + platform.getDb()
                .getUserGender());
        uid = null;
        nickname = null;
        avatar = null;

        if (Wechat.NAME.equalsIgnoreCase(platform.getName())) {
            thirdType = CommonConst.ThirdType.TYPE_WECHAT;
            uid = hashMap.get("unionid").toString();
            nickname = platform.getDb().getUserName();
            avatar = hashMap.get("headimgurl").toString();
            sex = Integer.valueOf(hashMap.get("sex").toString());
            runOnUiThread(() -> presenter.thirdLogin(uid, nickname, 0));
        } else if (SinaWeibo.NAME.equalsIgnoreCase(platform.getName())) {
            thirdType = CommonConst.ThirdType.TYPE_SINA_WEIBO;
            uid = platform.getDb().getUserId();
            nickname = platform.getDb().getUserName();
            runOnUiThread(() -> presenter.thirdLogin(uid, nickname, 1));
        }
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
