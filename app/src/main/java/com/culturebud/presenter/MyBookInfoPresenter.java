package com.culturebud.presenter;

import com.culturebud.BaseApp;
import com.culturebud.bean.UserBookInfo;
import com.culturebud.contract.MyBookInfoContract;
import com.culturebud.model.MyBookInfoModel;
import com.culturebud.util.ApiException;

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
}
