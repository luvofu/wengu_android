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

        view.showLoadingView();
        model.getInviteMsgs(BaseApp.getInstance().getUser().getToken(), page)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<UserMessage>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hiddenNoDataView();
                        e.printStackTrace();

                        if (e instanceof ApiException) {
                            if (page == 0) {
                                //第一页.
                                view.showErrorView(e.getMessage());
                            }
                        } else {
                            view.onErrorTip(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(List<UserMessage> msgs) {
                        view.hiddenNoDataView();
                        if (page == 0 && msgs.isEmpty()) {
                            //显示无数据页面.
                            view.showNoDataView("暂时没有消息");
                        }
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

        view.showLoadingView();
        model.agreeInvite(BaseApp.getInstance().getUser().getToken(), messageId)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        view.hiddenNoDataView();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        view.hiddenNoDataView();
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

    @Override
    public void deleteUserMessage(UserMessage userMessage) {
        if (!validateToken()) {
            return;
        }

        view.showLoadingView();
        model.deleteUserMessage(BaseApp.getInstance().getUser().getToken(), userMessage.getMessageId())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        view.hiddenNoDataView();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hiddenNoDataView();
                        e.printStackTrace();
                        if (e instanceof ApiException) {
                            view.onErrorTip(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        view.onDeleteUserMessage(userMessage, aBoolean);
                    }
                });
    }
}
