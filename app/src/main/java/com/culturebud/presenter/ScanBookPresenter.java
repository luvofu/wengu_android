package com.culturebud.presenter;

import android.text.TextUtils;

import com.culturebud.ApiErrorCode;
import com.culturebud.BaseApp;
import com.culturebud.bean.Book;
import com.culturebud.contract.ScanBookContract;
import com.culturebud.model.ScanBookModel;
import com.culturebud.util.ApiException;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XieWei on 2016/11/26.
 */

public class ScanBookPresenter extends ScanBookContract.Presenter {

    public ScanBookPresenter() {
        setModel(new ScanBookModel());
    }

    @Override
    public void scanBook(String isbn) {
        if (!validateToken()) {
            return;
        }
        if (TextUtils.isEmpty(isbn)) {

            return;
        }
        model.scanBook(BaseApp.getInstance().getUser().getToken(), isbn)
        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<Book>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (e instanceof ApiException) {
                    int code = ((ApiException) e).getCode();
                    if (code == ApiErrorCode.BOOK_SCAN_NOT_EXIST) {
                        view.onNotExitsBook(e.getMessage(), isbn);
                    } else {
                        view.onErrorTip(e.getMessage());
                    }
                } else {
                    view.onScanFail();
                }
            }

            @Override
            public void onNext(Book book) {
                view.onScanBook(book);
            }
        });
    }
}
