package com.culturebud.ui.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;

import com.culturebud.BaseActivity;
import com.culturebud.BaseApp;
import com.culturebud.CommonConst;
import com.culturebud.R;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.User;
import com.culturebud.contract.AccountBindingContract;
import com.culturebud.presenter.AccountBindingPresenter;
import com.culturebud.widget.SettingItemView;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.wechat.friends.Wechat;

import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_CHANGEMOBILEBIND;

@PresenterInject(AccountBindingPresenter.class)
public class AccountBindActivity extends BaseActivity<AccountBindingContract.Presenter> implements
        PlatformActionListener, AccountBindingContract.View {
    private SettingItemView sivBindMobile, sivBindWechat, sivBindSinaWeibo;
    private long lastThirdBind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_binding);
        presenter.setView(this);
        showTitlebar();
        sivBindMobile = obtainViewById(R.id.siv_bind_mobile);
        sivBindWechat = obtainViewById(R.id.siv_bind_wechat);
        sivBindSinaWeibo = obtainViewById(R.id.siv_bind_weibo);
        setTitle(R.string.binding_account);
        setListeners();

    }

    private void setListeners() {
        sivBindMobile.setOnClickListener(this);
        sivBindWechat.setOnClickListener(this);
        sivBindSinaWeibo.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        User user = BaseApp.getInstance().getUser();
        if (null != user) {
            String mobile = user.getRegMobile();
            if (!TextUtils.isEmpty(mobile)) {
                sivBindMobile.setRightInfo(mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
            } else {
                sivBindMobile.setRightInfo(R.string.no_binding);
            }
            String wexinId = user.getWechatId();
            if (!TextUtils.isEmpty(wexinId)) {
                sivBindWechat.setRightInfo(user.getWechatNick());
            } else {
                sivBindWechat.setRightInfo(R.string.no_binding);
            }
            if (!TextUtils.isEmpty(user.getWeiboId())) {
                sivBindSinaWeibo.setRightInfo(user.getWeiboNick());
            } else {
                sivBindSinaWeibo.setRightInfo(R.string.no_binding);
            }
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.siv_bind_mobile://绑定手机号
            {
                Intent intent = new Intent(this, MobileBindingActivity.class);
                String mobile = BaseApp.getInstance().getUser().getRegMobile();
                intent.putExtra("mobile", mobile);
                startActivity(intent);
                break;
            }
            case R.id.siv_bind_wechat://绑定微信
            {
                if (System.currentTimeMillis() - lastThirdBind < 3000) {
                    return;
                }
                lastThirdBind = System.currentTimeMillis();
                if (TextUtils.isEmpty(BaseApp.getInstance().getUser().getWechatId())) {
                    Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                    authorize(wechat);
                } else {
                    new AlertDialog.Builder(this).setMessage("确定解除微信账号绑定？")
                            .setPositiveButton("确定", (dialog, which) -> {
                                presenter.thirdUnbinding(BaseApp.getInstance().getUser().getWechatId(), CommonConst.ThirdType.TYPE_WECHAT);
                            })
                            .setNegativeButton("取消", null)
                            .show();
                }
                break;
            }
            case R.id.siv_bind_weibo://bind sina weibo
            {
                if (System.currentTimeMillis() - lastThirdBind < 3000) {
                    return;
                }
                lastThirdBind = System.currentTimeMillis();
                if (TextUtils.isEmpty(BaseApp.getInstance().getUser().getWeiboId())) {
                    Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                    authorize(weibo);
                } else {
                    new AlertDialog.Builder(this).setMessage("确定解除微博账号绑定？")
                            .setPositiveButton("确定", (dialog, which) -> {
                                presenter.thirdUnbinding(BaseApp.getInstance().getUser().getWeiboId(), CommonConst.ThirdType.TYPE_SINA_WEIBO);
                            })
                            .setNegativeButton("取消", null)
                            .show();
                }
                break;
            }
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
        plat.SSOSetting(false);
//        plat.authorize();
        plat.showUser(null);
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
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Bundle data = intent.getExtras();
        int requestCode = data.getInt("requestcode");
        if (requestCode == REQUEST_CODE_CHANGEMOBILEBIND) {
            //换绑成功，刷新UI.
            initData();
        }
    }

    private String uid, nickname;

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        if (Wechat.NAME.equalsIgnoreCase(platform.getName())) {
            uid = hashMap.get("unionid").toString();
            nickname = platform.getDb().getUserName();
            runOnUiThread(() -> presenter.thirdBinding(uid, nickname, CommonConst.ThirdType.TYPE_WECHAT));
        } else if (SinaWeibo.NAME.equalsIgnoreCase(platform.getName())) {
            uid = platform.getDb().getUserId();
            nickname = platform.getDb().getUserName();
            runOnUiThread(() -> presenter.thirdBinding(uid, nickname, CommonConst.ThirdType.TYPE_SINA_WEIBO));
        }
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {

    }

    @Override
    public void onCancel(Platform platform, int i) {

    }

    @Override
    public void onBindingRes(boolean success, int thirdType) {
        if (success) {
            User user = BaseApp.getInstance().getUser();
            if (user != null) {
                switch (thirdType) {
                    case CommonConst.ThirdType.TYPE_WECHAT:
                        if (!TextUtils.isEmpty(user.getWechatId())) {
                            sivBindWechat.setRightInfo(user.getWechatNick());
                        } else {
                            sivBindWechat.setRightInfo(R.string.no_binding);
                        }
                        break;
                    case CommonConst.ThirdType.TYPE_SINA_WEIBO:
                        if (!TextUtils.isEmpty(user.getWeiboId())) {
                            sivBindSinaWeibo.setRightInfo(user.getWeiboNick());
                            break;
                        } else {
                            sivBindSinaWeibo.setRightInfo(R.string.no_binding);
                        }
                }
            }
        }
    }
}
