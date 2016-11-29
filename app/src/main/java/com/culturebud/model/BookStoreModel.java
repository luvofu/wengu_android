package com.culturebud.model;

import android.text.TextUtils;

import com.culturebud.bean.ApiResultBean;
import com.culturebud.bean.Book;
import com.culturebud.bean.BookSheet;
import com.culturebud.contract.BookStoreContract;
import com.culturebud.db.dao.BookDAO;
import com.culturebud.net.ApiBookInterface;
import com.culturebud.net.ApiBookSheetInterface;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

/**
 * Created by XieWei on 2016/11/3.
 */

public class BookStoreModel extends BookStoreContract.Model {

    private BookDAO bookDAO;

    private void initBookDAO() throws SQLException {
        if (bookDAO == null) {
            bookDAO = new BookDAO();
        }
    }

    @Override
    public Observable<List<Book>> getBooks(String token, int page, int sortType, String filterType) {
        Map<String, Object> params = getCommonParams();
        if (!TextUtils.isEmpty(token)) {
            params.put(TOKEN_KEY, token);
        }
        params.put("page", page);
        params.put("sortType", sortType);
        if (!TextUtils.isEmpty(filterType)) {
            params.put("filterType", filterType);
        }
        return Observable.create(new Observable.OnSubscribe<List<Book>>() {
            @Override
            public void call(Subscriber<? super List<Book>> subscriber) {
                initRetrofit().create(ApiBookInterface.class).getBooks(params).subscribe(new Subscriber<ApiResultBean<JsonObject>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ApiResultBean<JsonObject> bean) {
                        int code = bean.getCode();
                        if (code != 200) {
                            subscriber.onError(new Exception("请求错误"));
                            return;
                        }
                        JsonObject jobj = bean.getData();
                        if (jobj.has("bookList")) {
                            JsonArray jarr = jobj.getAsJsonArray("bookList");
                            Gson gson = new Gson();
                            List<Book> books = gson.fromJson(jarr, new TypeToken<List<Book>>() {
                            }.getType());
                            subscriber.onNext(books);
                        } else {
                            subscriber.onNext(null);
                        }
                        subscriber.onCompleted();
                    }

                });

            }
        });
    }

    @Override
    public Observable<List<BookSheet>> getBookSheets(String token, int page, int sortType, String filterType) {
        Map<String, Object> params = getCommonParams();
        if (!TextUtils.isEmpty(token)) {
            params.put(TOKEN_KEY, token);
        }
        params.put("page", page);
        params.put("sortType", sortType);
        if (!TextUtils.isEmpty(filterType)) {
            params.put("filterType", filterType);
        }
        return Observable.create(new Observable.OnSubscribe<List<BookSheet>>() {
            @Override
            public void call(Subscriber<? super List<BookSheet>> subscriber) {
                initRetrofit().create(ApiBookSheetInterface.class).getBookSheets(params).subscribe(new Subscriber<ApiResultBean<JsonObject>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ApiResultBean<JsonObject> bean) {
                        int code = bean.getCode();
                        if (code != 200) {
                            subscriber.onError(new Exception("请求错误"));
                            return;
                        }
                        JsonObject jobj = bean.getData();
                        if (jobj.has("bookSheetList")) {
                            JsonArray jarr = jobj.getAsJsonArray("bookSheetList");
                            Gson gson = new Gson();
                            List<BookSheet> sheets = gson.fromJson(jarr, new TypeToken<List<BookSheet>>() {
                            }.getType());
                            subscriber.onNext(sheets);
                        } else {
                            subscriber.onNext(null);
                        }
                        subscriber.onCompleted();
                    }

                });

            }
        });
    }

    @Override
    public Observable<Boolean> cacheBooks(List<Book> books) {
        return Observable.create(subscriber -> {
            try {
                initBookDAO();
                for (Book book : books) {
                    boolean res = bookDAO.addBook(book);
                    subscriber.onNext(res);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                subscriber.onError(e);
            }
            subscriber.onCompleted();
        });
    }
}
