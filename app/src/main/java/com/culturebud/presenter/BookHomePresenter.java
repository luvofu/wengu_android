package com.culturebud.presenter;

import com.culturebud.BaseApp;
import com.culturebud.bean.BookMark;
import com.culturebud.contract.BookHomeContract;
import com.culturebud.model.BookHomeModel;
import com.culturebud.util.ApiException;

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
        if (!validateToken(false)) {
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

    @Override
    public void addBookMark(long userBookId, int pages, int totalPage) {
        if (!validateToken()) {
            return;
        }
        if (userBookId <= 0) {
            view.onErrorTip("请选择书籍");
             return;
        }
        if (totalPage <= 0) {
            view.onErrorTip("请输入页码");
             return;
        }
        model.addBookMark(BaseApp.getInstance().getUser().getToken(), userBookId, pages, totalPage)
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
                view.onAddBookMark(aBoolean);
            }
        });
    }

    @Override
    public void alterBookMark(long bookmarkId, int pages, int totalPage) {
        if (!validateToken()) {
            return;
        }
        model.alterBookMark(BaseApp.getInstance().getUser().getToken(), bookmarkId, pages, totalPage)
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
                view.onAlterBookMark(aBoolean);
            }
        });
    }

    @Override
    public void delBookMark(BookMark bookMark) {
        if (!validateToken()) {
            return;
        }
        if (bookMark == null) {
            //TODO
            return;
        }
        model.delBookMark(BaseApp.getInstance().getUser().getToken(), bookMark.getBookmarkId())
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
                view.onDelBookMark(aBoolean, bookMark);
            }
        });
    }
}
