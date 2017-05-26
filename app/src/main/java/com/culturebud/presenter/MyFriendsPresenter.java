package com.culturebud.presenter;

import com.culturebud.BaseApp;
import com.culturebud.bean.Friend;
import com.culturebud.contract.MyFriendsContract;
import com.culturebud.model.MyFriendsModel;
import com.culturebud.util.ApiException;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XieWei on 2016/12/5.
 */

public class MyFriendsPresenter extends MyFriendsContract.Presenter {
    private static final String TAG = MyFriendsPresenter.class.getSimpleName();

    public MyFriendsPresenter() {
        setModel(new MyFriendsModel());
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
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        String errorMessage = ApiException.getErrorMessage(e);
                        view.showErrorView(errorMessage);
                    }

                    @Override
                    public void onNext(Boolean aboolean) {

                        view.onConcern(friend);
                    }
                });
    }
}
