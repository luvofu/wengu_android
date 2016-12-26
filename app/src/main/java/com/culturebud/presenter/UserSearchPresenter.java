package com.culturebud.presenter;

import android.text.TextUtils;

import com.culturebud.BaseApp;
import com.culturebud.bean.User;
import com.culturebud.contract.UserSearchContract;
import com.culturebud.model.UserSearchModel;
import com.culturebud.util.ApiException;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XieWei on 2016/12/26.
 */

public class UserSearchPresenter extends UserSearchContract.Presenter {

    public UserSearchPresenter() {
        setModel(new UserSearchModel());
    }

    @Override
    public void search(String keyword, int page) {
        if (!validateToken()) {
            view.onToLogin();
            return;
        }
        if (TextUtils.isEmpty(keyword)) {
            view.onErrorTip("搜索内容不能为空");
            return;
        }
        view.onClearOldData();
        view.showProDialog();
        model.search(BaseApp.getInstance().getUser().getToken(), keyword, page)
        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<List<User>>() {
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
            public void onNext(List<User> users) {
                view.onUsers(users);
            }
        });
    }
}
