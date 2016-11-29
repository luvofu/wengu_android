package com.culturebud.presenter;

import android.text.TextUtils;

import com.culturebud.BaseApp;
import com.culturebud.contract.FeedbackContract;
import com.culturebud.model.FeedbackModel;
import com.culturebud.util.ApiException;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XieWei on 2016/11/16.
 */

public class FeedbackPresenter extends FeedbackContract.Presenter {

    public FeedbackPresenter() {
        setModel(new FeedbackModel());
    }

    @Override
    public void feedback(String questions, String contact) {
        if (TextUtils.isEmpty(questions)) {
            view.onFeedback("请输入反馈信息后再提交");
            return;
        }
        if (!validateToken()) {
            return;
        }
        model.feedback(BaseApp.getInstance().getUser().getToken(), questions, contact)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e instanceof ApiException) {
                            view.onFeedback(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        view.onFeedback(null);
                    }
                });
    }
}
