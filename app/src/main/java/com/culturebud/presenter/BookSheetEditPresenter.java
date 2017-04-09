package com.culturebud.presenter;

import android.net.Uri;

import com.culturebud.BaseApp;
import com.culturebud.CommonConst;
import com.culturebud.contract.BookSheetEditContract;
import com.culturebud.model.BookSheetEditModel;
import com.culturebud.util.ApiException;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XieWei on 2017/4/8.
 */

public class BookSheetEditPresenter extends BookSheetEditContract.Presenter {

    public BookSheetEditPresenter() {
        setModel(new BookSheetEditModel());
    }

    @Override
    public void editBookSheet(long bookSheetId, String bsName, String desc, String tag) {
        if (!validateToken()) {
            return;
        }
        view.showProDialog();
        model.editBookSheet(BaseApp.getInstance().getUser().getToken(), bookSheetId, bsName, desc, tag)
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
                        view.onEdit(aBoolean);
                    }
                });
    }

    @Override
    public void editCover(Uri uri, long bookSheetId) {
        if (!validateToken()) {
            return;
        }
        view.showProDialog();
        model.uploadImage(BaseApp.getInstance().getUser().getToken(), CommonConst.UploadImgType
                .TYPE_USER_BOOK_SHEET_COVER, bookSheetId, uri, true)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
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
                    public void onNext(String s) {

                    }
                });
    }
}
