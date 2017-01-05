package com.culturebud.presenter;

import com.culturebud.BaseApp;
import com.culturebud.bean.BookCircleDynamic;
import com.culturebud.contract.DynamicDetailContract;
import com.culturebud.model.DynamicDetailModel;
import com.culturebud.util.ApiException;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XieWei on 2017/1/4.
 */

public class DynamicDetailPresenter extends DynamicDetailContract.Presenter {

    public DynamicDetailPresenter() {
        setModel(new DynamicDetailModel());
    }

    @Override
    public void dynamicDetail(long dynamicId) {
        if (!validateToken()) {
            view.onToLogin();
            return;
        }
        view.showProDialog();
        model.dynamicDetail(BaseApp.getInstance().getUser().getToken(), dynamicId)
        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<BookCircleDynamic>() {
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
            public void onNext(BookCircleDynamic dynamic) {
                view.onDynamic(dynamic);
            }
        });
    }
}
