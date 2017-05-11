package com.culturebud.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.culturebud.ApiErrorCode;
import com.culturebud.BaseApp;
import com.culturebud.CommonConst;
import com.culturebud.bean.ApiResultBean;
import com.culturebud.bean.User;
import com.culturebud.contract.LoginContract;
import com.culturebud.model.LoginModel;
import com.culturebud.util.ApiException;
import com.culturebud.util.DigestUtil;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XieWei on 2016/10/25.
 */

public class LoginPresenter extends LoginContract.Presenter {
    private static final String TAG = "LoginPresenter";

    public LoginPresenter() {
        setModel(new LoginModel());
    }

    @Override
    public void login(String userName, String password) {
        if (!validate(userName, password)) {
            return;
        }
        password = DigestUtil.md5(password);
        model.login(CommonConst.PLATFORM, BaseApp.getInstance().getDeviceId(), userName, password)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<User>() {
                               @Override
                               public void onCompleted() {

                               }

                               @Override
                               public void onError(Throwable e) {
                                   if (e instanceof ApiException) {
                                       view.onErrorTip(e.getMessage());
                                   }
                               }

                               @Override
                               public void onNext(User user) {
                                   processLoginRes(user);
                               }
                           }

                );
    }

    @Override
    public void logout() {
        if (!validateToken()) {
            return;
        }

        User user = BaseApp.getInstance().getUser();

        //1. 远程登出.
        model.logoutRemote(user.getToken()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                     public void onNext(Boolean res) {

                    }
                });

        //2. 本地登出，清空消息.
        model.logout(user).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        view.onLogout(false);
                    }

                    @Override
                    public void onNext(Boolean res) {
                        view.onLogout(res);
                    }
                });
    }

    @Override
    public void loadLocalUser() {
        model.loadLastUser().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<User>() {
                    @Override
                    public void onCompleted() {
                        Log.d("LoginActivity", "loadLocalUser() ==>> onCompleted()");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(User user) {
                        if (user != null) {
                            BaseApp.getInstance().setUser(user);
                            view.loginSuccess(user);
                        }
                    }
                });
    }

    @Override
    public void thirdLogin(String uid, String nick, int thirdType) {
        if (TextUtils.isEmpty(uid)) {
            view.onErrorTip("uid不能为空");
            return;
        }
        view.showProDialog();
        model.thirdLogin(uid, nick, thirdType)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<User>() {
                    @Override
                    public void onCompleted() {
                        view.hideProDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hideProDialog();
                        e.printStackTrace();
                        if (e instanceof ApiException) {
                            switch (((ApiException) e).getCode()) {
                                case 10121:
                                    view.onNeedBindPhone();
                                    break;
                            }
                        }
                    }

                    @Override
                    public void onNext(User user) {
                        processLoginRes(user);
                    }
                });
    }

    public void processLoginRes(final User user) {
        model.saveUser(user).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onErrorTip("登录失败");
                    }

                    @Override
                    public void onNext(Boolean res) {
                        if (res) {
                            BaseApp.getInstance().setUser(user);
                            view.loginSuccess(user);
                        } else {
                            view.onErrorTip("登录失败");
                        }
                    }
                });
    }

    @Override
    public void thirdBindLogin(String validCode, String regMobile, int thirdType, String uid, String nickname, int
            sex, String autograph, String birthday, String avatar) {
        if (TextUtils.isEmpty("regMobile")) {
            view.onErrorTip("手机号不能为空");
            return;
        }
        if (TextUtils.isEmpty(validCode)) {
            view.onErrorTip("验证码不能为空");
            return;
        }
        view.showProDialog();
        model.thirdBindLogin(validCode, regMobile, thirdType, uid, nickname, sex, autograph, birthday, avatar)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<User>() {
                    @Override
                    public void onCompleted() {
                        view.hideProDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hideProDialog();
                        e.printStackTrace();
                        if (e instanceof ApiException) {
                            view.onErrorTip(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(User user) {
                        processLoginRes(user);
                    }
                });
    }

    @Override
    public void getSecurityCode(String phoneNumber, int thirdType) {
        if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length() != 11) {
            view.onErrorTip("手机号不合法");
            return;
        }
        model.getSucrityCode(null, phoneNumber, CommonConst.SucrityCodeType.TYPE_THIRD_BIND, thirdType)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e instanceof ApiException) {
                            view.onErrorTip(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        view.onObtainedCode(aBoolean);
                    }
                });
    }

    @Override
    public void countDown() {
        model.countDown(60).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        view.onCountDown(-1);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Integer integer) {
                        view.onCountDown(integer);
                    }
                });
    }

    private boolean validate(String userName, String password) {
        if (TextUtils.isEmpty(userName)) {
            view.onErrorTip("用户名和密码不能为空");
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            view.onErrorTip("用户名和密码不能为空");
            return false;
        }

        return true;
    }
}
