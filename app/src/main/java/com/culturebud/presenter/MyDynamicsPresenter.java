package com.culturebud.presenter;

import com.culturebud.BaseApp;
import com.culturebud.bean.BookCircleDynamic;
import com.culturebud.contract.MyDynamicsContract;
import com.culturebud.model.MyDynamicsModel;
import com.culturebud.util.ApiException;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XieWei on 2017/1/12.
 */

public class MyDynamicsPresenter extends MyDynamicsContract.Presenter {

    public MyDynamicsPresenter() {
        setModel(new MyDynamicsModel());
    }


    @Override
    public void myPublished(int page) {
        if (!validateToken()) {
            view.onToLogin();
            return;
        }
        model.myPublished(BaseApp.getInstance().getUser().getToken(), page)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<BookCircleDynamic>>() {
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
                    public void onNext(List<BookCircleDynamic> dynamics) {
                        view.onDynamics(dynamics);
                    }
                });
    }

    @Override
    public void myRelations(int page) {

    }
}
