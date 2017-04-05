package com.culturebud.presenter;

import com.culturebud.BaseApp;
import com.culturebud.bean.Book;
import com.culturebud.contract.BookScanResultContract;
import com.culturebud.model.BookScanResultModel;
import com.culturebud.util.ApiException;

import java.util.List;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XieWei on 2017/4/5.
 */

public class BookScanResultPresenter extends BookScanResultContract.Presenter {

    public BookScanResultPresenter() {
        setModel(new BookScanResultModel());
    }

    @Override
    public void addScanBooks(Map<String, List<Book>> books) {
        if (!validateToken()) {
            return;
        }
        view.showProDialog();
        model.addScanBooks(BaseApp.getInstance().getUser().getToken(), books)
        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<Boolean>() {
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
            public void onNext(Boolean aBoolean) {
                view.onAdd(aBoolean);
            }
        });
    }
}
