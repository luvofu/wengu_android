package com.culturebud.presenter;

import com.culturebud.BaseApp;
import com.culturebud.bean.Book;
import com.culturebud.bean.BookSheet;
import com.culturebud.bean.User;
import com.culturebud.contract.BookStoreContract;
import com.culturebud.model.BookStoreModel;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XieWei on 2016/11/3.
 */

public class BookStorePresenter extends BookStoreContract.Presenter {

    public BookStorePresenter() {
        setModel(new BookStoreModel());
    }
    @Override
    public void getBooks(int page, int sortType, String filterType) {
        User user = BaseApp.getInstance().getUser();
        String token = null;
        if (user != null) {
            token = user.getToken();
        }
        model.getBooks(token, page, sortType, filterType).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<List<Book>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<Book> books) {
                if (books != null && books.size() > 0) {
                    view.onShowBooks(books);
                    cacheBooks(books);
                } else {

                }
            }
        });
    }

    @Override
    public void getBookSheets(int page, int sortType, String filterType) {
        User user = BaseApp.getInstance().getUser();
        String token = null;
        if (user != null) {
            token = user.getToken();
        }
        model.getBookSheets(token, page, sortType, filterType).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<BookSheet>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<BookSheet> sheets) {
                        if (sheets != null && sheets.size() > 0) {
                            view.onShowBookSheets(sheets);
                        } else {

                        }
                    }
                });
    }

    @Override
    public void cacheBooks(List<Book> books) {
        model.cacheBooks(books).subscribeOn(Schedulers.io())
        .subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Boolean aBoolean) {

            }
        });
    }

    @Override
    public void getFiltersByIsBookSheets(boolean isBookSheets) {
        if(isBookSheets){
            model.getBookSheetFilters().subscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<List<String>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(List<String> filters) {
                            view.initFilters(filters);
                        }
                    });
        }else{
            model.getBookFilters().subscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<List<String>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(List<String> filters) {
                            view.initFilters(filters);
                        }
                    });
        }
    }
}
