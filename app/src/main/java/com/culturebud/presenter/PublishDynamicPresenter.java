package com.culturebud.presenter;

import android.net.Uri;

import com.culturebud.BaseApp;
import com.culturebud.contract.PublishDynamicContract;
import com.culturebud.model.PublishDynamicModel;
import com.culturebud.util.ApiException;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XieWei on 2017/1/9.
 */

public class PublishDynamicPresenter extends PublishDynamicContract.Presenter {

    public PublishDynamicPresenter() {
        setModel(new PublishDynamicModel());
    }

    @Override
    public void publish(String content, Uri imgUri, int permission, int linkType, long linkId) {
        if (!validateToken()) {
            view.onToLogin();
            return;
        }

        view.showProDialog();
        model.publish(BaseApp.getInstance().getUser().getToken(), content, imgUri, permission, linkType, linkId)
        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<Boolean>() {
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
            public void onNext(Boolean result) {
                view.onPublishResult(result);
            }
        });
    }
}
