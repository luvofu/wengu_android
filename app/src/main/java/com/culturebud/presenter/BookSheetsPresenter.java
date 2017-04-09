package com.culturebud.presenter;

import com.culturebud.BaseApp;
import com.culturebud.bean.BookSheet;
import com.culturebud.bean.User;
import com.culturebud.contract.BookSheetsContract;
import com.culturebud.model.BookSheetsModel;
import com.culturebud.util.ApiException;

import java.util.List;
import java.util.Map;

import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XieWei on 2016/12/13.
 */

public class BookSheetsPresenter extends BookSheetsContract.Presenter {

    public BookSheetsPresenter() {
        setModel(new BookSheetsModel());
    }

    @Override
    public void getUserCreatedSheets(long userId) {
        if (!validateToken()) {
            return;
        }
        User user = BaseApp.getInstance().getUser();
        model.getMySheets(user.getToken(), userId == -1 ? user.getUserId() : userId)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<BookSheet>>() {
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
                    public void onNext(List<BookSheet> bookSheets) {
                        view.onMyCreatedSheets(bookSheets);
                    }
                });
    }

    @Override
    public void getUserFavoriteSheets(long userId) {
        if (!validateToken()) {
            return;
        }
        User user = BaseApp.getInstance().getUser();
        model.getMyFavoriteBookSheets(user.getToken(), 0, userId == -1 ? user.getUserId() : userId)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Map<Integer, List<BookSheet>>>() {
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
                    public void onNext(Map<Integer, List<BookSheet>> map) {
                        if (map.size() > 0) {
                            for (int key : map.keySet()) {
                                List<BookSheet> sheets = map.get(key);
                                view.onMyFavoriteSheets(sheets);
                                break;
                            }
                        }
                    }
                });
    }

    @Override
    public void deleteBookSheet(BookSheet bookSheet) {
        if (!validateToken()) {
            return;
        }
        User user = BaseApp.getInstance().getUser();
        view.showProDialog();
        if (user.getUserId() == bookSheet.getUserId()) {
            model.deleteBookSheet(user.getToken(), bookSheet.getSheetId())
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
                            view.onDeleteMyCreated(aBoolean, bookSheet);
                        }
                    });
        } else {
            model.collectDel(user.getToken(), 1, bookSheet.getSheetId())
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
                            view.onDeleteMyFavorite(aBoolean, bookSheet);
                        }
                    });
        }
    }
}
