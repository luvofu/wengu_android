package com.culturebud.presenter;

import com.culturebud.BaseApp;
import com.culturebud.bean.User;
import com.culturebud.contract.AccountBindingContract;
import com.culturebud.model.AccountBindingModel;
import com.culturebud.util.ApiException;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XieWei on 2017/5/1.
 */

public class AccountBindingPresenter extends AccountBindingContract.Presenter {

    public AccountBindingPresenter() {
        setModel(new AccountBindingModel());
    }

    @Override
    public void thirdBinding(String uid, String nickname, int thirdType) {
        if (!validateToken()) {
            return;
        }
        model.thirdBinding(BaseApp.getInstance().getUser().getToken(), uid, nickname, thirdType)
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
                view.onBindingRes(true, thirdType);
                updateLocalUser(user);
            }
        });
    }

    @Override
    public void thirdUnbinding(String uid, int thirdType) {
        if (!validateToken()) {
            return;
        }
        view.showProDialog();
        model.thirdUnbinding(BaseApp.getInstance().getUser().getToken(), uid, thirdType)
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
                BaseApp.getInstance().setUser(user);
                view.onBindingRes(true, thirdType);
                updateLocalUser(user);
            }
        });
    }

    @Override
    public void updateLocalUser(User user) {
        model.updateLocalUser(user).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Boolean aBoolean) {

            }
        });
    }
}
