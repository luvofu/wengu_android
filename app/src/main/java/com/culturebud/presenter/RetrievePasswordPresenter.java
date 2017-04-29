package com.culturebud.presenter;

import android.text.TextUtils;

import com.culturebud.BaseApp;
import com.culturebud.CommonConst;
import com.culturebud.CommonConst.SucrityCodeType;
import com.culturebud.bean.User;
import com.culturebud.contract.RetrievePasswordContract;
import com.culturebud.model.RetrievePasswordModel;
import com.culturebud.util.ApiException;
import com.culturebud.util.DigestUtil;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XieWei on 2016/11/18.
 */

public class RetrievePasswordPresenter extends RetrievePasswordContract.Presenter {

    public RetrievePasswordPresenter() {
        setModel(new RetrievePasswordModel());
    }

    @Override
    public void retrievePassword(String phoneNumber, String sucrityCode, String pwd, String pwdConfirm) {
        if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length() != 11) {
            view.onErrorTip("手机号不合法");
            return;
        }
        if (TextUtils.isEmpty(sucrityCode) || sucrityCode.length() != 6) {
            view.onErrorTip("验证码不正确");
            return;
        }

        if (TextUtils.isEmpty(pwd) || pwd.length() < 6 || pwd.length() > 15) {
            view.onErrorTip("密码长度6-15位");
            return;
        }

        if (!pwd.equals(pwdConfirm)) {
            view.onErrorTip("两次输入密码不同");
            return;
        }
        model.forgotPassword(phoneNumber, DigestUtil.md5(pwd), sucrityCode)
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
                        view.onRetrieve(user);
                    }
                });
    }

    @Override
    public void getSucrityCode(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length() != 11) {
            view.onErrorTip("手机号不合法");
            return;
        }
        model.getSucrityCode(null, phoneNumber, SucrityCodeType.TYPE_FORGOT_PWD, CommonConst.ThirdType.TYPE_NONE)
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
}
