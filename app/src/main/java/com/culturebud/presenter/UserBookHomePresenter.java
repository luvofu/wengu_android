package com.culturebud.presenter;

import android.text.TextUtils;

import com.culturebud.BaseApp;
import com.culturebud.CommonConst;
import com.culturebud.bean.BookCircleDynamic;
import com.culturebud.bean.DynamicReply;
import com.culturebud.bean.User;
import com.culturebud.contract.UserBookHomeContract;
import com.culturebud.model.UserBookHomeModel;
import com.culturebud.util.ApiException;
import com.google.gson.JsonObject;

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

    @Override
    public void thumbUpDynamic(long dynamicId) {
        if (!validateToken()) {
            return;
        }
        model.thumbUp(BaseApp.getInstance().getUser().getToken(), CommonConst.ThumbUpType.TYPE_DYNAMIC, dynamicId)
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
                        view.onThumbUp(dynamicId, aBoolean);
                    }
                });
    }

    @Override
    public void replyDynamic(long dynamicId, String content, int replyType, long replyObjId) {
        if (!validateToken()) {
            return;
        }
        if (TextUtils.isEmpty(content)) {
            view.onErrorTip("回复内容不能为空");
            return;
        }
        model.replyDynamic(BaseApp.getInstance().getUser().getToken(), dynamicId, content, replyType, replyObjId)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DynamicReply>() {
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
                    public void onNext(DynamicReply dynamicReply) {
                        view.onDynamicReply(dynamicReply);
                    }
                });
    }

    @Override
    public void concern(long friendId) {
        if (!validateToken()) {
            return;
        }
        model.concern(BaseApp.getInstance().getUser().getToken(), friendId)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<JsonObject>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        String errorMessage = ApiException.getErrorMessage(e);
                        view.showErrorView(errorMessage);
                    }

                    @Override
                    public void onNext(JsonObject jsonObject) {
                        long concernNum = jsonObject.get("concernNum").getAsLong();
                        long fanNum = jsonObject.get("fanNum").getAsLong();
                        int status = jsonObject.get("concernStatus").getAsInt();
                        view.onConcern(concernNum, fanNum, status);
                    }
                });
    }
}
