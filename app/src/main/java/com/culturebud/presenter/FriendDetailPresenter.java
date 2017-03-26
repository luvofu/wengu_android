package com.culturebud.presenter;

import com.culturebud.BaseApp;
import com.culturebud.bean.User;
import com.culturebud.contract.FriendDetailContract;
import com.culturebud.model.FriendDetailModel;
import com.culturebud.util.ApiException;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XieWei on 2016/12/27.
 */

public class FriendDetailPresenter extends FriendDetailContract.Presenter {

    public FriendDetailPresenter() {
        setModel(new FriendDetailModel());
    }

    @Override
    public void getFriendDetail(long userId) {
        if (!validateToken()) {
            return;
        }
        view.showProDialog();
        model.getUserProfile(BaseApp.getInstance().getUser().getToken(), userId)
        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<User>() {
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
            public void onNext(User user) {
                view.onFriend(user);
            }
        });
    }
}
