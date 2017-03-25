package com.culturebud.presenter;

import android.text.TextUtils;

import com.culturebud.BaseApp;
import com.culturebud.bean.BookCategoryGroup;
import com.culturebud.bean.CollectedBook;
import com.culturebud.bean.User;
import com.culturebud.contract.CollectedBooksContract;
import com.culturebud.model.CollectedBooksModel;
import com.google.gson.JsonObject;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XieWei on 2016/11/9.
 */

public class CollectedBooksPresenter extends CollectedBooksContract.Presenter {

    public CollectedBooksPresenter() {
        setModel(new CollectedBooksModel());
    }

    @Override
    public void getMyBooks(int page) {
        User user = BaseApp.getInstance().getUser();
        if (user == null) {
            view.onToLogin();
            return;
        }

        model.getCollectedBooks(user.getToken(), user.getUserId(), page).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<CollectedBook>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<CollectedBook> collectedBooks) {
                        if (collectedBooks != null && !collectedBooks.isEmpty()) {
                            view.onBooks(collectedBooks);
                        }
                    }
                });
    }

    @Override
    public void getMyBooks(int page, int categoryType, String category) {
        User user = BaseApp.getInstance().getUser();
        if (user == null) {
            view.onToLogin();
            return;
        }

        model.getCollectedBooks(user.getToken(), user.getUserId(), page, categoryType, category)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<CollectedBook>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<CollectedBook> collectedBooks) {
                        view.onBooks(collectedBooks);
                    }
                });
    }

    @Override
    public void getCategoryStatistics() {
        if (!validateToken()) {
            view.onToLogin();
            return;
        }
        User user = BaseApp.getInstance().getUser();
        model.getCategoryStatistics(user.getToken(), user.getUserId())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BookCategoryGroup>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(BookCategoryGroup res) {
                        view.onCategoryStatistics(res);
                    }
                });
    }
}
