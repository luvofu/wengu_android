package com.culturebud.presenter;

import android.text.TextUtils;

import com.culturebud.BaseApp;
import com.culturebud.bean.ApiResultBean;
import com.culturebud.bean.CommentReply;
import com.culturebud.bean.User;
import com.culturebud.contract.CommentDetailContract;
import com.culturebud.model.CommentDetailModel;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XieWei on 2016/11/9.
 */

public class CommentDetailPresenter extends CommentDetailContract.Presenter {

    public CommentDetailPresenter() {
        setModel(new CommentDetailModel());
    }

    @Override
    public void thumbUp(long goodObjId) {
        User user = BaseApp.getInstance().getUser();
        if (!validateToken()) {
            return;
        }
        model.thumbUp(user.getToken(), 0, goodObjId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Boolean res) {
                view.onThumbUp(res);
            }
        });
    }

    @Override
    public void getReplies(long commentId, int page) {
        User user = BaseApp.getInstance().getUser();
        String token = null;
        if (user != null) {
            token = user.getToken();
        }
        model.getCommentReplies(token, commentId, page).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<CommentReply>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<CommentReply> replies) {
                        if (replies != null && replies.size() > 0) {
                            view.onReplies(replies);
                        } else {

                        }
                    }
                });
    }

    @Override
    public void addReply(long commentId, int replyType, long replyObjId, String content) {
        User user = BaseApp.getInstance().getUser();
        if (!validateToken()) {
            return;
        }
        if (TextUtils.isEmpty(content)) {

            return;
        }
        model.addCommentReply(user.getToken(), commentId, content, replyType, replyObjId)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommentReply>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(CommentReply reply) {
                        if (reply != null) {
                            view.onReply(reply);
                        } else {

                        }
                    }
                });
    }

    @Override
    public void delReply(int deleteType, long deleteObjId) {
        User user = BaseApp.getInstance().getUser();
        if (!validateToken()) {
            return;
        }
        model.delete(user.getToken(), deleteType, deleteObjId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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
                        view.onDelReply(res, deleteType, deleteObjId);
                    }
                });
    }
}
