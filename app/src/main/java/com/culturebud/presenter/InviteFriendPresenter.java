package com.culturebud.presenter;

import com.culturebud.BaseApp;
import com.culturebud.contract.InviteFriendContract;
import com.culturebud.model.InviteFriendModel;
import com.culturebud.util.ApiException;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XieWei on 2016/12/28.
 */

public class InviteFriendPresenter extends InviteFriendContract.Presenter {
    public InviteFriendPresenter() {
        setModel(new InviteFriendModel());
    }

    @Override
    public void invite(long friendId, String content) {
        if (!validateToken()) {
            return;
        }
        view.showProDialog();
        model.invite(BaseApp.getInstance().getUser().getToken(), friendId, content)
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
            public void onNext(Boolean aBoolean) {
                view.onInviteSuccess(friendId);
            }
        });
    }
}
