package com.culturebud.presenter;

import com.culturebud.bean.BookCommunity;
import com.culturebud.bean.SearchKeyword;
import com.culturebud.contract.SearchBookCommunityContract;
import com.culturebud.model.SearchBookCommunityModel;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XieWei on 2016/11/6.
 */

public class SearchBookCommunityPresenter extends SearchBookCommunityContract.Presenter {

    public SearchBookCommunityPresenter() {
        setModel(new SearchBookCommunityModel());
    }

    @Override
    public void search(String keyword, int page) {
        model.search(keyword, page).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<BookCommunity>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<BookCommunity> bookCommunities) {
                        if (bookCommunities != null && bookCommunities.size() > 0) {
                            view.onBookCommunities(bookCommunities);
                        }
                    }
                });
    }

    @Override
    public void getHistorySearchKeyword() {
        model.getKeywords(SearchKeyword.SKType.TYPE_COMMUNITY).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<List<SearchKeyword>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
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
        sk.setKeyword(keyword);
        sk.setType(SearchKeyword.SKType.TYPE_COMMUNITY);
        model.saveKeyword(sk).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<SearchKeyword>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
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
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Boolean res) {
                        if (res) {
                            view.onClearHistory();
                        }
                    }
                });
    }
}
