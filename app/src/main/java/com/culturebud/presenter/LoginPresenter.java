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
//                                       ApiException ae = (ApiException) e;
//                                       switch (ae.getCode()) {
//                                           case ApiErrorCode.ERROR_LOGIN:
//                                               view.showTip(ApiErrorCode.ERROR_LOGIN_INFO);
//                                               break;
//                                       }
                                   }
                               }

                               @Override
                               public void onNext(User user) {
                                   Log.d(TAG, "onNext()--> " + user);
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
                                                   Log.d(TAG, "onNext() --> onNext()" + res);
                                                   if (res) {
                                                       BaseApp.getInstance().setUser(user);
                                                       view.loginSuccess(user);
                                                   } else {
                                                       view.onErrorTip("登录失败");
                                                   }
                                               }
                                           });
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
        Log.d("LoginActivity", "loadLocalUser()");
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
                Log.d("LoginActivity", "loadLocalUser() ==>> onError()" + e.getMessage());
            }

            @Override
            public void onNext(User user) {
                Log.d("LoginActivity", "loadLocalUser() ==>> onNext()" + user);
                if (user != null) {
                    BaseApp.getInstance().setUser(user);
                    view.loginSuccess(user);
                }
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
