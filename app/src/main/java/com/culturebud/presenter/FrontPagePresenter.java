package com.culturebud.presenter;

import android.graphics.Bitmap;

import com.culturebud.ApiErrorCode;
import com.culturebud.BaseApp;
import com.culturebud.bean.ApiResultBean;
import com.culturebud.bean.Book;
import com.culturebud.bean.BookSheet;
import com.culturebud.bean.Comment;
import com.culturebud.bean.User;
import com.culturebud.contract.FrontPageContract;
import com.culturebud.model.CommunityModel;
import com.culturebud.model.FrontPageModel;
import com.culturebud.util.ApiException;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XieWei on 2016/10/24.
 */

public class FrontPagePresenter extends FrontPageContract.Presenter {

    public FrontPagePresenter() {
        setModel(new FrontPageModel());
    }

    @Override
    public void loadDatas() {
        model.getFrontPageData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiResultBean<JsonObject>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();

                        String errorMessage = ApiException.getErrorMessage(e);
                        view.showErrorView(errorMessage);

                        view.onRequestError();


//                        if (e instanceof ApiException) {
//                            int code = ((ApiException) e).getCode();
//                            if (code == ApiErrorCode.EXPIRETIME_TOKEN
//                                    || code == ApiErrorCode.EXCEPT_ACC) {
//                                loadDatas();
//                            }
//                        }
                    }

                    @Override
                    public void onNext(ApiResultBean<JsonObject> bean) {
                        view.hiddenNoDataView();

                        JsonObject jobj = bean.getData();
                        Gson gson = new Gson();
                        if (jobj.has("bookList")) {
                            JsonArray jarr = jobj.getAsJsonArray("bookList");
                            List<Book> books = gson.fromJson(jarr, new TypeToken<List<Book>>() {
                            }.getType());
                            view.showBooks(books);
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
                                        public void onNext(Boolean o) {

                                        }
                                    });
                        }
                        if (jobj.has("bookSheetList")) {
                            JsonArray jarr = jobj.getAsJsonArray("bookSheetList");
                            view.showBookSheets(gson.fromJson(jarr, new TypeToken<List<BookSheet>>() {
                            }.getType()));
                        }
                        if (jobj.has("commentList")) {//TODO 暂时关闭此功能
//                            JsonArray jarr = jobj.getAsJsonArray("commentList");
//                            view.showComment(gson.fromJson(jarr, new TypeToken<List<Comment>>() {
//                            }.getType()));
                        }
                    }
                });

    }

    @Override
    public void thumbUp(int goodType, long goodObjId) {
        User user = BaseApp.getInstance().getUser();
        if (user == null) {
            return;
        }
        String token = user.getToken();
        model.thumbUp(token, goodType, goodObjId)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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
                        view.onThumbUp(goodObjId, res);
                    }
                });
    }

}
