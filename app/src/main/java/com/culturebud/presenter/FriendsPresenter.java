package com.culturebud.presenter;

import com.culturebud.BaseApp;
import com.culturebud.bean.Friend;
import com.culturebud.contract.FriendsContract;
import com.culturebud.model.FriendsModel;
import com.culturebud.util.ApiException;
import com.google.gson.JsonObject;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XieWei on 2016/12/5.
 */

public class FriendsPresenter extends FriendsContract.Presenter {
    private static final String TAG = FriendsPresenter.class.getSimpleName();

    public FriendsPresenter() {
        setModel(new FriendsModel());
    }

    @Override
    public void friends(boolean isConcern, long userId) {
        if (!validateToken()) {
            return;
        }
        view.showLoadingView();
        Observable<List<Friend>> observable = isConcern ?
                model.concers(BaseApp.getInstance().getUser().getToken(), userId)
                : model.fans(BaseApp.getInstance().getUser().getToken(), userId);

        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Friend>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        view.hiddenNoDataView();

                        String errorMessage = ApiException.getErrorMessage(e);
                        view.showErrorView(errorMessage);
                    }

                    @Override
                    public void onNext(List<Friend> friends) {
                        view.hiddenNoDataView();

                        if (friends.isEmpty()) {
                            view.showNoDataView("还没有好友，赶快添加吧");
                        }

                        view.onFriends(friends);
                    }
                });
    }

    @Override
    public void concern(Friend friend) {
        if (!validateToken()) {
            return;
        }
        model.concern(BaseApp.getInstance().getUser().getToken(), friend.getUserId())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<JsonObject>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        String errorMessage = ApiException.getErrorMessage(e);
                        view.onErrorTip(errorMessage);
                    }

                    @Override
                    public void onNext(JsonObject jsonObject) {
                        long concernNum = jsonObject.get("concernNum").getAsLong();
                        long fanNum = jsonObject.get("fanNum").getAsLong();
                        int status = jsonObject.get("concernStatus").getAsInt();
                        view.onConcern(friend, concernNum, fanNum, status);
                    }
                });
    }
}
