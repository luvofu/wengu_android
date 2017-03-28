package com.culturebud.presenter;

import com.culturebud.BaseApp;
import com.culturebud.bean.BookCategoryGroup;
import com.culturebud.bean.CollectedBook;
import com.culturebud.bean.User;
import com.culturebud.contract.CollectedBooksContract;
import com.culturebud.model.CollectedBooksModel;
import com.culturebud.util.ApiException;

import java.util.List;
import java.util.Set;

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
        if (!validateToken()) {
            return;
        }
        User user = BaseApp.getInstance().getUser();
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
        if (!validateToken()) {
            return;
        }
        User user = BaseApp.getInstance().getUser();
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

    @Override
    public void deleteUserBooks(Set<CollectedBook> userBooks) {
        if (!validateToken()) {
            return;
        }
        model.deleteUserBooks(BaseApp.getInstance().getUser().getToken(), userBooks)
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
                view.onDeleteUserBooks(userBooks, aBoolean);
            }
        });
    }

    @Override
    public void alterReadStatus(Set<CollectedBook> userBooks, int readStatus) {
        if (!validateToken()) {
            return;
        }
        model.alterReadStatus(BaseApp.getInstance().getUser().getToken(), userBooks, readStatus)
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

            }
        });
    }
}
