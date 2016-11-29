package com.culturebud.presenter;

import android.util.Log;

import com.culturebud.BaseApp;
import com.culturebud.bean.Comment;
import com.culturebud.bean.MyRelatedComment;
import com.culturebud.contract.MyCommentContract;
import com.culturebud.model.MyCommentModel;
import com.culturebud.util.ApiException;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XieWei on 2016/11/19.
 */

public class MyCommentPresenter extends MyCommentContract.Presenter {
    private static final String TAG = MyCommentPresenter.class.getSimpleName();

    public MyCommentPresenter() {
        setModel(new MyCommentModel());
    }

    @Override
    public void getMyPublished(int page) {
        if (!validateToken()) {
            return;
        }
        model.myPublished(BaseApp.getInstance().getUser().getToken(), page)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Comment>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e instanceof ApiException) {

                        }
                    }

                    @Override
                    public void onNext(List<Comment> comments) {
                        view.onComments(comments);
                    }
                });
    }

    @Override
    public void getMyRelated(int page) {
        if (!validateToken()) {
            return;
        }
        model.myRelated(BaseApp.getInstance().getUser().getToken(), page)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<MyRelatedComment>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<MyRelatedComment> comments) {
                        view.onMyRelatedComments(comments);
                    }
                });
    }

    @Override
    public void thumbUp(long goodObjId) {
        if (!validateToken()) {
            return;
        }
        model.thumbUp(BaseApp.getInstance().getUser().getToken(), 0, goodObjId)
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
                view.onThumbUp(res, goodObjId);
            }
        });
    }
}
