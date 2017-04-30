package com.culturebud.presenter;

import com.culturebud.BaseApp;
import com.culturebud.bean.UserMessage;
import com.culturebud.contract.MyMsgsContract;
import com.culturebud.model.MyMsgsModel;
import com.culturebud.util.ApiException;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XieWei on 2016/11/16.
 */

public class MyMsgsPresenter extends MyMsgsContract.Presenter {
    public MyMsgsPresenter() {
        setModel(new MyMsgsModel());
    }

    @Override
    public void getInviteMsgs(int page) {
        if (!validateToken()) {
            return;
        }
        model.getInviteMsgs(BaseApp.getInstance().getUser().getToken(), page)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<UserMessage>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<UserMessage> msgs) {
                        if (msgs != null && !msgs.isEmpty()) {
                            view.onInviteMsgs(msgs);
                        }
                    }
                });
    }

    @Override
    public void agreeInvite(long messageId) {
        if (!validateToken()) {
            return;
        }
        model.agreeInvite(BaseApp.getInstance().getUser().getToken(), messageId)
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
                view.onAgreeInvite(messageId, aBoolean);
            }
        });
    }
}
