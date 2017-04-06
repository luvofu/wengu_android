package com.culturebud.presenter;

import android.net.Uri;
import android.text.TextUtils;

import com.culturebud.BaseApp;
import com.culturebud.contract.CreateBookSheetContract;
import com.culturebud.model.CreateBookSheetModel;
import com.culturebud.util.ApiException;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XieWei on 2016/12/14.
 */

public class CreateBookSheetPresenter extends CreateBookSheetContract.Presenter {

    public CreateBookSheetPresenter() {
        setModel(new CreateBookSheetModel());
    }

    @Override
    public void createBookSheet(String bookSheetName, String bookSheetDesc, Uri imgUri) {
        if (!validateToken()) {
            return;
        }
        if (TextUtils.isEmpty(bookSheetName)) {
            view.onErrorTip("书单名不能为空");
             return;
        }
        view.showProDialog();
        model.createBookSheet(BaseApp.getInstance().getUser().getToken(), bookSheetName, bookSheetDesc, imgUri)
        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<Integer>() {
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
            public void onNext(Integer res) {
                view.onCreateSuccess(res);
            }
        });
    }
}
