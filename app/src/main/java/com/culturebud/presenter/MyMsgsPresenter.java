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
    public void getMsgs(int msgGetType, String messageIds, String messageTypes, int page) {
        if (!validateToken()) {
            return;
        }

        view.showLoadingView();
        model.getMsgs(BaseApp.getInstance().getUser().getToken(), msgGetType, messageIds, messageTypes, page)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<UserMessage>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hiddenNoDataView();
                        e.printStackTrace();

                        String errorMessage = ApiException.getErrorMessage(e);

                        if (page == 0) {
                            //第一页.
                            view.showErrorView(errorMessage);
                        } else {
                            view.onErrorTip(errorMessage);
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
                            view.onMsgs(msgs);
                        }
                    }
                });
    }

    @Override
    public void agreeInvite(UserMessage userMessage) {
        if (!validateToken()) {
            return;
        }

        view.showLoadingView(true);
        model.agreeInvite(BaseApp.getInstance().getUser().getToken(), userMessage.getMessageId())
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

                        String errorMessage = ApiException.getErrorMessage(e);

                        view.onErrorTip(errorMessage);
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        view.hiddenNoDataView();
                        if (aBoolean)
                            view.onAgreeInvite(userMessage);
                    }
                });
    }

    @Override
    public void deleteUserMessage(UserMessage userMessage) {
        if (!validateToken()) {
            return;
        }

        view.showLoadingView(true);
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
                        String errorMessage = ApiException.getErrorMessage(e);
                        view.onErrorTip(errorMessage);
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        view.hiddenNoDataView();
                        view.onDeleteUserMessage(userMessage, aBoolean);
                    }
                });
    }
}
