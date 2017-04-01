package com.culturebud.presenter;

import com.culturebud.BaseApp;
import com.culturebud.bean.CollectedBook;
import com.culturebud.bean.User;
import com.culturebud.contract.SelectBookContract;
import com.culturebud.model.SelectBookModel;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XieWei on 2017/4/1.
 */

public class SelectBookPresenter extends SelectBookContract.Presenter {

    public SelectBookPresenter() {
        setModel(new SelectBookModel());
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
}
