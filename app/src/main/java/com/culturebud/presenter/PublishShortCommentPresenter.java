package com.culturebud.presenter;

import android.text.TextUtils;

import com.culturebud.BaseApp;
import com.culturebud.bean.User;
import com.culturebud.contract.PublishShortCommentContract;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XieWei on 2016/11/11.
 */

public class PublishShortCommentPresenter extends PublishShortCommentContract.Presenter {

    public PublishShortCommentPresenter() {
        setModel(new PublishShortCommentContract.Model() {
        });
    }

    @Override
    public void publish(long communityId, String content) {
        User user = BaseApp.getInstance().getUser();
        if (user == null || TextUtils.isEmpty(user.getToken())) {
            view.onToLogin();
            return;
        }
        if (TextUtils.isEmpty(content)) {
            view.onContentEmpty();
            return;
        }
        model.addComment(user.getToken(), communityId, content)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Boolean res) {
                        view.onPublisResult(res);
                    }
                });
    }
}
