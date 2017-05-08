package com.culturebud.presenter;

import com.culturebud.bean.Book;
import com.culturebud.bean.SearchKeyword;
import com.culturebud.contract.ImportBookContract;
import com.culturebud.model.ImportBookModel;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ImportBookPresenter extends ImportBookContract.Presenter {

    public ImportBookPresenter() {
        setModel(new ImportBookModel());
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
                        view.onBooks(books);
                        if (books != null && books.size() > 0) {
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
    public void addBook(final Book book) {
        model.addBook(book).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Boolean ok) {

                        if (ok) {
                           view.afterAddBook(book.getBookId());
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
