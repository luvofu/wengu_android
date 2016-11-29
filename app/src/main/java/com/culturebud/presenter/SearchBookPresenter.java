package com.culturebud.presenter;

import com.culturebud.bean.Book;
import com.culturebud.bean.SearchKeyword;
import com.culturebud.contract.SearchBookContract;
import com.culturebud.model.SearchBookModel;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XieWei on 2016/11/5.
 */

public class SearchBookPresenter extends SearchBookContract.Presenter {

    public SearchBookPresenter() {
        setModel(new SearchBookModel());
    }

    @Override
    public void searchBook(String keyword, int page) {
        model.searchBooks(keyword, page).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Book>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<Book> books) {
                        if (books != null && books.size() > 0) {
                            view.onBooks(books);
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
                                        public void onNext(Boolean res) {

                                        }
                                    });
                        }
                    }
                });
    }

    @Override
    public void getHistorySearchKeyword() {
        model.getKeywords(SearchKeyword.SKType.TYPE_BOOK)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<SearchKeyword>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<SearchKeyword> keywords) {
                        if (keywords != null && keywords.size() > 0) {
                            view.onKeywords(keywords);
                        }
                    }
                });
    }

    @Override
    public void cacheKeyworkd(String keyword) {
        SearchKeyword sk = new SearchKeyword();
        sk.setType(SearchKeyword.SKType.TYPE_BOOK);
        sk.setKeyword(keyword);
        model.saveKeyword(sk).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SearchKeyword>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(SearchKeyword keyword) {
                        if (keyword != null) {
                            view.onKeyword(keyword);
                        }
                    }
                });
    }

    @Override
    public void clearHistory(List<SearchKeyword> keywords) {
        model.clearHistory(keywords).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Boolean isDel) {
                        if (isDel) {
                            view.onClearHistory();
                        }
                    }
                });
    }
}
