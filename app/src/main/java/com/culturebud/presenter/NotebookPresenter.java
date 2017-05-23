package com.culturebud.presenter;

import android.text.TextUtils;

import com.culturebud.BaseApp;
import com.culturebud.bean.CollectedBook;
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

        view.showLoadingView(page != 0);
        model.userNotebooks(user.getToken(), userId, page)
        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<List<Notebook>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                view.hiddenNoDataView();

                String errorMessage = ApiException.getErrorMessage(e);
                if (page == 0) {
                    view.showErrorView(errorMessage);
                } else {
                    view.onErrorTip(errorMessage);
                }
            }

            @Override
            public void onNext(List<Notebook> notebooks) {
                view.hiddenNoDataView();
                view.onNotebooks(notebooks);
            }
        });
    }

    @Override
    public void deleteNotebook(Notebook notebook) {
        if (!validateToken()) {
            return;
        }

        view.showLoadingView(true);
        model.deleteNotebook(BaseApp.getInstance().getUser().getToken(), notebook.getNotebookId())
        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                String errorMessage = ApiException.getErrorMessage(e);
                view.onErrorTip(errorMessage);
            }

            @Override
            public void onNext(Boolean aBoolean) {
                view.hiddenNoDataView();
                view.onDeleteNotebook(notebook, aBoolean);
            }
        });
    }

    @Override
    public void createNotebook(long bookId) {
        if (!validateToken()) {
            return;
        }

        model.createNotebook(BaseApp.getInstance().getUser().getToken(), bookId)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hiddenNoDataView();
                        e.printStackTrace();
                        String errorMessage = ApiException.getErrorMessage(e);
                        view.onErrorTip(errorMessage);
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        view.onCreateNotebook(aBoolean, bookId);
                    }
                });
    }
}
