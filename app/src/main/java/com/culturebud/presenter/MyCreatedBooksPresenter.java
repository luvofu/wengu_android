package com.culturebud.presenter;

import com.culturebud.BaseApp;
import com.culturebud.bean.CheckedBook;
import com.culturebud.contract.MyCreatedBooksContract;
import com.culturebud.model.MyCreatedBooksModel;
import com.culturebud.util.ApiException;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XieWei on 2017/3/30.
 */

public class MyCreatedBooksPresenter extends MyCreatedBooksContract.Presenter {
    public MyCreatedBooksPresenter() {
        setModel(new MyCreatedBooksModel());
    }


    @Override
    public void myCreatedBooks(long page) {
        if (!validateToken()) {
            return;
        }
        model.myCreatedBooks(BaseApp.getInstance().getUser().getToken(), page)
        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<List<CheckedBook>>() {
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
            public void onNext(List<CheckedBook> books) {
                view.onMyBooks(books);
            }
        });
    }
}
