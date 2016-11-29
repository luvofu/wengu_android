package com.culturebud.presenter;

import android.text.TextUtils;

import com.culturebud.BaseApp;
import com.culturebud.bean.User;
import com.culturebud.contract.ChangePasswordContract;
import com.culturebud.model.ChangePasswordModel;
import com.culturebud.util.ApiException;
import com.culturebud.util.DigestUtil;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XieWei on 2016/11/18.
 */

public class ChangePasswordPresenter extends ChangePasswordContract.Presenter {

    public ChangePasswordPresenter() {
        setModel(new ChangePasswordModel());
    }

    @Override
    public void changePwd(String currPwd, String newPwd, String newPwdConfirm) {
        if (!validateToken()) {
            return;
        }
        if (TextUtils.isEmpty(newPwd) || newPwd.length() < 6 || newPwd.length() > 15) {
            view.onErrorTip("密码长度6~15位");
            return;
        }
        if (!newPwd.equals(newPwdConfirm)) {
            view.onErrorTip("两次输入密码不相同");
            return;
        }
        model.changePwd(BaseApp.getInstance().getUser().getToken(), DigestUtil.md5(currPwd), DigestUtil.md5(newPwd))
        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<User>() {
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
            public void onNext(User user) {
                BaseApp.getInstance().setUser(user);
                view.onUser(user);
            }
        });
    }
}
