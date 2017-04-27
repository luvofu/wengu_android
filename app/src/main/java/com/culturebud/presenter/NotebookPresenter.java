package com.culturebud.presenter;

import com.culturebud.BaseApp;
import com.culturebud.bean.Notebook;
import com.culturebud.bean.User;
import com.culturebud.contract.NotebookContract;
import com.culturebud.model.NotebookModel;
import com.culturebud.util.ApiException;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XieWei on 2016/11/20.
 */

public class NotebookPresenter extends NotebookContract.Presenter {

    public NotebookPresenter() {
        setModel(new NotebookModel());
    }

    @Override
    public void userNotebooks(int page, long userId) {
        if (!validateToken()) {
            return;
        }
        User user = BaseApp.getInstance().getUser();
        model.userNotebooks(user.getToken(), userId, page)
        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<List<Notebook>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(List<Notebook> notebooks) {
                view.onNotebooks(notebooks);
            }
        });
    }

    @Override
    public void deleteNotebook(Notebook notebook) {
        if (!validateToken()) {
            return;
        }
        model.deleteNotebook(BaseApp.getInstance().getUser().getToken(), notebook.getNotebookId())
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
                view.onDeleteNotebook(notebook, aBoolean);
            }
        });
    }
}
