package com.culturebud.presenter;

import com.culturebud.BaseApp;
import com.culturebud.bean.BookSheet;
import com.culturebud.bean.BookSheetDetail;
import com.culturebud.bean.User;
import com.culturebud.contract.BookSheetDetailContract;
import com.culturebud.model.BookSheetDetailModel;
import com.culturebud.util.ApiException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XieWei on 2016/11/7.
 */

public class BookSheetDetailPresenter extends BookSheetDetailContract.Presenter {
    private int relationType;

    public BookSheetDetailPresenter() {
        setModel(new BookSheetDetailModel());
    }

    @Override
    public void getBookSheetDetail(long sheetId) {
        User user = BaseApp.getInstance().getUser();
        String token = null;
        if (user != null) {
            token = user.getToken();
        }
        model.getBookSheetDetail(token, sheetId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<JsonObject>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(JsonObject result) {
                        if (result != null) {
                            Gson gson = new Gson();
                            if (result.has("relationType")) {
                                relationType = gson.fromJson(result.get("relationType"), int.class);
                                view.onRelationType(relationType);
                            }
                            if (result.has("bookSheetDetail")) {
                                BookSheetDetail detail = gson.fromJson(result.getAsJsonObject("bookSheetDetail"),
                                        new TypeToken<BookSheetDetail>() {
                                        }.getType());
                                if (detail != null) {
                                    view.onBookSheetDetail(detail);
                                }
                            }
                        }
                    }
                });
    }

    @Override
    public void collectAdd(long sheetId) {
        User user = BaseApp.getInstance().getUser();
        if (!validateToken()) {
            return;
        }

        model.collectAdd(user.getToken(), 1, sheetId).subscribeOn(Schedulers.io())
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
                        view.onCollect(res);
                    }
                });
    }

    @Override
    public void collectDel(long sheetId) {
        User user = BaseApp.getInstance().getUser();
        if (!validateToken()) {
            return;
        }
        model.collectDel(user.getToken(), 1, sheetId).subscribeOn(Schedulers.io())
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
                        view.onCollect(res);
                    }
                });
    }

    @Override
    public void getMySheets() {
        if (!validateToken()) {
            return;
        }
        User user = BaseApp.getInstance().getUser();
        model.getMySheets(user.getToken(), user.getUserId())
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
                view.onMySheets(bookSheets);
            }
        });
    }
}
