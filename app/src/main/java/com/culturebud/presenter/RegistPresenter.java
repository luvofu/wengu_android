package com.culturebud.presenter;

import android.text.TextUtils;

import com.culturebud.BaseApp;
import com.culturebud.CommonConst;
import com.culturebud.CommonConst.SucrityCodeType;
import com.culturebud.bean.User;
import com.culturebud.contract.RegistContract;
import com.culturebud.model.RegistModel;
import com.culturebud.util.ApiException;
import com.culturebud.util.DigestUtil;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XieWei on 2016/11/17.
 */

public class RegistPresenter extends RegistContract.Presenter {

    public RegistPresenter() {
        setModel(new RegistModel());
    }

    @Override
    public void regist(String phoneNumber, String validcode, String pwd, String pwdConfirm) {
        if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length() != 11) {
            view.onInvalidate("手机号不合法");
            return;
        }
        if (TextUtils.isEmpty(validcode) || validcode.length() != 6) {
            view.onInvalidate("验证码不正确");
            return;
        }

        if (TextUtils.isEmpty(pwd) || pwd.length() < 6 || pwd.length() > 15) {
            view.onInvalidate("密码长度6-15位");
            return;
        }

        if (!pwd.equals(pwdConfirm)) {
            view.onInvalidate("两次输入密码不同");
            return;
        }
        model.regist(phoneNumber, validcode, DigestUtil.md5(pwd))
        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<User>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (e instanceof ApiException) {
                    view.onInvalidate(e.getMessage());
                }
            }

            @Override
            public void onNext(User user) {
                BaseApp.getInstance().setUser(user);
                view.onRegist(user);
            }
        });
    }

    @Override
    public void getSucrityCode(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length() != 11) {
            view.onInvalidate("手机号不合法");
            return;
        }
        model.getSucrityCode(null, phoneNumber, SucrityCodeType.TYPE_REGIST, CommonConst.ThirdType.TYPE_NONE)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e instanceof ApiException) {
                            view.onInvalidate(e.getMessage());
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
}
