package com.culturebud.presenter;

import com.culturebud.ApiErrorCode;
import com.culturebud.BaseApp;
import com.culturebud.bean.BookCircleDynamic;
import com.culturebud.bean.User;
import com.culturebud.contract.UserBookHomeContract;
import com.culturebud.model.UserBookHomeModel;
import com.culturebud.util.ApiException;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XieWei on 2016/12/30.
 */

public class UserBookHomePresenter extends UserBookHomeContract.Presenter {

    public UserBookHomePresenter() {
        setModel(new UserBookHomeModel());
    }

    @Override
    public void getUserProfile(long userId) {
        if (!validateToken()) {
            view.onToLogin();
            return;
        }
        model.getUserProfile(BaseApp.getInstance().getUser().getToken(), userId)
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
                        view.onUser(user);
                    }
                });

    }

    @Override
    public void getDynamics(long userId, int page) {
        if (!validateToken()) {
            view.onToLogin();
            return;
        }
        view.showProDialog();
        model.getDynamics(BaseApp.getInstance().getUser().getToken(), userId, page)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<BookCircleDynamic>>() {
                    @Override
                    public void onCompleted() {
                        view.hideProDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        view.hideProDialog();
                        if (e instanceof ApiException) {
                            view.onErrorTip(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(List<BookCircleDynamic> bookCircleDynamics) {
                        view.onDynamics(bookCircleDynamics);
                    }
                });
    }
}
