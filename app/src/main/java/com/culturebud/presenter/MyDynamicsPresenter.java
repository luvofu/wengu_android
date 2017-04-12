package com.culturebud.presenter;

import android.text.TextUtils;

import com.culturebud.BaseApp;
import com.culturebud.bean.BookCircleDynamic;
import com.culturebud.bean.BookCircleDynamicRelationMe;
import com.culturebud.bean.DynamicReply;
import com.culturebud.contract.MyDynamicsContract;
import com.culturebud.model.MyDynamicsModel;
import com.culturebud.util.ApiException;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XieWei on 2017/1/12.
 */

public class MyDynamicsPresenter extends MyDynamicsContract.Presenter {

    public MyDynamicsPresenter() {
        setModel(new MyDynamicsModel());
    }


    @Override
    public void myPublished(int page) {
        if (!validateToken()) {
            return;
        }
        model.myPublished(BaseApp.getInstance().getUser().getToken(), page)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<BookCircleDynamic>>() {
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
                    public void onNext(List<BookCircleDynamic> dynamics) {
                        view.onDynamics(dynamics);
                    }
                });
    }

    @Override
    public void myRelations(int page) {
        if (!validateToken()) {
            return;
        }
        model.myRelations(BaseApp.getInstance().getUser().getToken(), page)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<BookCircleDynamicRelationMe>>() {
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
                    public void onNext(List<BookCircleDynamicRelationMe> dynamics) {
                        view.onRelations(dynamics);
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
                    public void onNext(DynamicReply bean) {
                        view.onDynamicReply(bean);
                    }
                });
    }
}
