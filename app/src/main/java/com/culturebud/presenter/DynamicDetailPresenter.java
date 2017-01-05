package com.culturebud.presenter;

import android.util.Log;

import com.culturebud.BaseApp;
import com.culturebud.bean.BookCircleDynamic;
import com.culturebud.bean.DynamicReply;
import com.culturebud.contract.DynamicDetailContract;
import com.culturebud.model.DynamicDetailModel;
import com.culturebud.util.ApiException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XieWei on 2017/1/4.
 */

public class DynamicDetailPresenter extends DynamicDetailContract.Presenter {
    private static final String TAG = DynamicDetailPresenter.class.getSimpleName();

    public DynamicDetailPresenter() {
        setModel(new DynamicDetailModel());
    }

    @Override
    public void dynamicDetail(long dynamicId) {
        if (!validateToken()) {
            view.onToLogin();
            return;
        }
        view.showProDialog();
        model.dynamicDetail(BaseApp.getInstance().getUser().getToken(), dynamicId)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BookCircleDynamic>() {
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
                    public void onNext(BookCircleDynamic dynamic) {
                        view.onDynamic(dynamic);
                    }
                });
    }

    @Override
    public void processReplies(List<DynamicReply> src) {
        Observable<List<DynamicReply>> observable = Observable.create(subscriber -> {
            List<DynamicReply> dynamicReplies = new LinkedList<>();
            List<DynamicReply> replies = new LinkedList<>();
            for (DynamicReply dr : src) {
                dr.setReplies(new ArrayList<>());
                switch (dr.getReplyType()) {
                    case 0:
                        dynamicReplies.add(dr);
                        break;
                    case 1:
                        replies.add(dr);
                        break;
                }
            }
            for (DynamicReply dr : dynamicReplies) {
                getChildReply(dr, dr.getReplies(), replies);
                Collections.sort(dr.getReplies(), (dr01, dr02) -> {
                    if (dr01.getCreatedTime() > dr02.getCreatedTime()) {
                        return 1;
                    } else if (dr01.getCreatedTime() == dr02.getCreatedTime()) {
                        return 0;
                    } else {
                        return -1;
                    }
                });
                time = 0;
            }
            subscriber.onNext(dynamicReplies);
        });
        observable.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<DynamicReply>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<DynamicReply> replies) {
                        view.onReplies(replies);
                    }
                });
    }

    private int time = 0;

    private void getChildReply(DynamicReply reply, List<DynamicReply> dest, List<DynamicReply> src) {
        time++;
        for (int i = src.size() - 1; i >= 0; i--) {
            if (i > src.size() - 1) {
                i = src.size() - 1;
            }
            DynamicReply dr = src.get(i);
            Log.d(TAG, "第" + time + "次调用方法 " + "i = " + i + " \t " + dr);
            if (dr.getReplyObj().getReplyId() == reply.getReplyId()) {
                dest.add(dr);
                src.remove(i);
                if (src.size() > 0) {
                    getChildReply(dr, dest, src);
                }
            }
        }
    }
}
