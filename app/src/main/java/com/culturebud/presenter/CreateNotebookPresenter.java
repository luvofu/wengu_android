package com.culturebud.presenter;

import android.text.TextUtils;

import com.culturebud.BaseApp;
import com.culturebud.bean.CollectedBook;
import com.culturebud.contract.CreateNotebookContract;
import com.culturebud.model.CreateNotebookModel;
import com.culturebud.util.ApiException;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XieWei on 2016/11/22.
 */

public class CreateNotebookPresenter extends CreateNotebookContract.Presenter {

    public CreateNotebookPresenter() {
        setModel(new CreateNotebookModel());
    }


    @Override
    public void createNotebook(String notebookName, CollectedBook book) {
        if (!validateToken()) {
            return;
        }
        if (TextUtils.isEmpty(notebookName)) {
            return;
        }
        if (book == null) {
            view.onErrorTip("请选择需要做笔记的书");
            return;
        }
        model.createNotebook(BaseApp.getInstance().getUser().getToken(), notebookName, book.getBookId())
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
                view.onCreateNotebook(aBoolean, book.getBookId());
            }
        });
    }
}
