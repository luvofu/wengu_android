package com.culturebud.presenter;

import android.net.Uri;
import android.text.TextUtils;

import com.culturebud.BaseApp;
import com.culturebud.bean.CheckedBook;
import com.culturebud.contract.ManualAddBookContract;
import com.culturebud.model.ManualAddBookModel;
import com.culturebud.util.ApiException;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XieWei on 2016/12/10.
 */

public class ManualAddBookPresenter extends ManualAddBookContract.Presenter {

    public ManualAddBookPresenter() {
        setModel(new ManualAddBookModel());
    }

    @Override
    public void submitBook(Uri imgUri, String bookName, String nameOrgin,
                           String subTitle, String isbn, List<String> authors,
                           List<String> translators, String price, String publisher,
                           String pubDate, String binding, String pages,
                           String summary, String authorInfo) {
        if (!validateToken()) {
            return;
        }
        if (TextUtils.isEmpty(bookName) || authors == null || authors.isEmpty()) {
            view.onErrorTip("书名和作者不能为空");
            return;
        }
        String author = "";
        for (String a : authors) {
            author = author + a + "|";
        }
        if (author.endsWith("|")) {
            author = author.substring(0, author.length() - 1);
        }
        String translator = "";
        for (String t : translators) {
            translator = translator + t + "|";
        }
        if (translator.endsWith("|")) {
            translator = translator.substring(0, translator.length() - 1);
        }
        view.showProDialog();
        model.addBook(BaseApp.getInstance().getUser().getToken(), imgUri,
                bookName, nameOrgin, subTitle, isbn, author, translator,
                price, publisher, pubDate, binding, pages, summary, authorInfo)
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
                view.onAddResult(aBoolean);
            }
        });
    }

    @Override
    public void checkBook(CheckedBook checkedBook, Uri imgUri) {
        if (!validateToken()) {
            return;
        }
        if (checkedBook == null) {
            view.onErrorTip("书籍信息为空");
            return;
        }
        model.checkBook(BaseApp.getInstance().getUser().getToken(), checkedBook, imgUri)
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
            public void onNext(Boolean res) {
                view.onCheckResult(res);
            }
        });
    }
}
