package com.culturebud.presenter;

import com.culturebud.BaseApp;
import com.culturebud.CommonConst.ThumbUpType;
import com.culturebud.bean.BookCommunityDetail;
import com.culturebud.bean.Comment;
import com.culturebud.bean.User;
import com.culturebud.contract.BookCommunityContract;
import com.culturebud.model.BookCommunityModel;
import com.culturebud.util.ApiException;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XieWei on 2016/11/10.
 */

public class BookCommunityPresenter extends BookCommunityContract.Presenter {

    public BookCommunityPresenter() {
        setModel(new BookCommunityModel());
    }

    @Override
    public void getCommunityDetail(long communityId) {
        User user = BaseApp.getInstance().getUser();
        String token = null;
        if (user != null) {
            token = user.getToken();
        }
        model.getCommunityDetail(token, communityId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BookCommunityDetail>() {
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
                    public void onNext(BookCommunityDetail detail) {
                        if (detail != null) {
                            view.onCommunityDetail(detail);
                        } else {

                        }
                    }
                });
    }

    @Override
    public void getCommunityComments(int page, int sortType, long communityId) {
        User user = BaseApp.getInstance().getUser();
        String token = null;
        if (user != null) {
            token = user.getToken();
        }

        model.getComments(token, page, sortType, communityId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Comment>>() {
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
                    public void onNext(List<Comment> comments) {
                        if (comments != null && !comments.isEmpty()) {
                            view.onComments(comments);
                        }
                    }
                });
    }

    @Override
    public void thumbUp(long commentId, int position) {
        if (!validateToken()) {
            return;
        }
        model.thumbUp(BaseApp.getInstance().getUser().getToken(), ThumbUpType.TYPE_COMMENT, commentId)
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
                view.onThumbUp(aBoolean, commentId, position);
            }
        });
    }
}
