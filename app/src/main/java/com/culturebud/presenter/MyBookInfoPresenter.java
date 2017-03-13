package com.culturebud.presenter;

import com.culturebud.BaseApp;
import com.culturebud.bean.UserBookInfo;
import com.culturebud.contract.MyBookInfoContract;
import com.culturebud.model.MyBookInfoModel;
import com.culturebud.util.ApiException;

import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XieWei on 2017/3/2.
 */

public class MyBookInfoPresenter extends MyBookInfoContract.Presenter {

    public MyBookInfoPresenter() {
        setModel(new MyBookInfoModel());
    }

    @Override
    public void myBookInfo(long userBookId) {
        if (!validateToken()) {
            view.onToLogin();
            return;
        }
        model.myBookInfo(BaseApp.getInstance().getUser().getToken(), userBookId)
        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<UserBookInfo>() {
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
            public void onNext(UserBookInfo userBookInfo) {
                view.onBookInfo(userBookInfo);
            }
        });
    }

    @Override
    public void alterBookReadStatus(long userBookId, int status) {
        if (!validateToken()) {
            view.onToLogin();
            return;
        }
        model.alterBookReadStatus(BaseApp.getInstance().getUser().getToken(), userBookId, status)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
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
            public void onNext(Boolean res) {
                view.onAlert(userBookId, res, status);
            }
        });
    }

    @Override
    public void editUserBookInfo(long userBookId, Map<String, Object> editContent) {
        if (!validateToken()) {
            view.onToLogin();
            return;
        }
        model.editUserBookInfo(BaseApp.getInstance().getUser().getToken(), userBookId, editContent)
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
            public void onNext(Boolean res) {
                view.onEditUserBookInfo(userBookId, editContent, res);
            }
        });
    }
}
