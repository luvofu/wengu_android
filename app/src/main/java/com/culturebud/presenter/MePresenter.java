package com.culturebud.presenter;

import com.culturebud.BaseApp;
import com.culturebud.bean.User;
import com.culturebud.contract.MeContract;
import com.culturebud.model.MeModel;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XieWei on 2016/10/25.
 */

public class MePresenter extends MeContract.Presenter {

    public MePresenter() {
        setModel(new MeModel());
    }

    public void login() {
        view.showLoginPage();
    }

    @Override
    public void loadLastUser() {
        model.loadLastUser().subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<User>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(User user) {
                if (user != null) {
                    BaseApp.getInstance().setUser(user);
                    view.showUser(user);
                } else {
                    view.showLoginButton();
                }
            }
        });
    }


    public void processLoginResult(User user) {
        view.showUser(user);
    }
}
