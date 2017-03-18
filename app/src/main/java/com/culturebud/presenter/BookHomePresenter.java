package com.culturebud.presenter;

import com.culturebud.BaseApp;
import com.culturebud.bean.BookMark;
import com.culturebud.contract.BookHomeContract;
import com.culturebud.model.BookHomeModel;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XieWei on 2016/10/28.
 */

public class BookHomePresenter extends BookHomeContract.Presenter {

    public BookHomePresenter() {
        setModel(new BookHomeModel());
    }

    @Override
    public void getMyBookMarks() {
        if (!validateToken()) {
            return;
        }
        model.getBookMarks(BaseApp.getInstance().getUser().getToken())
        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<List<BookMark>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(List<BookMark> bookMarks) {
                view.onBookMarks(bookMarks);
            }
        });
    }
}
