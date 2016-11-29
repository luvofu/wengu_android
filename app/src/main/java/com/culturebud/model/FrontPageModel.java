package com.culturebud.model;

import android.text.TextUtils;
import android.util.Log;

import com.culturebud.ApiErrorCode;
import com.culturebud.BaseApp;
import com.culturebud.bean.ApiResultBean;
import com.culturebud.bean.Book;
import com.culturebud.bean.User;
import com.culturebud.contract.FrontPageContract;
import com.culturebud.db.dao.BookDAO;
import com.culturebud.db.dao.UserDAO;
import com.culturebud.util.ApiException;
import com.google.gson.JsonObject;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by XieWei on 2016/10/24.
 */

public class FrontPageModel extends FrontPageContract.Model {
    private static final String TAG = "FrontPageModel";
    private BookDAO bookDao;
    private UserDAO userDAO;

    private void initBookDAO() throws SQLException {
        if (bookDao == null) {
            bookDao = new BookDAO();
        }
        if (userDAO == null) {
            userDAO = new UserDAO();
        }
    }

    @Override
    public Observable<ApiResultBean<JsonObject>> getFrontPageData() {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            User user = BaseApp.getInstance().getUser();
            if (user != null && !TextUtils.isEmpty(user.getToken())) {
                params.put(TOKEN_KEY, user.getToken());
            }
            getHomeInterface().getFrontPageList(params).subscribe(new Subscriber<ApiResultBean<JsonObject>>() {
                @Override
                public void onCompleted() {
                    subscriber.onCompleted();
                }

                @Override
                public void onError(Throwable e) {
                    subscriber.onError(e);
                }

                @Override
                public void onNext(ApiResultBean<JsonObject> bean) {
                    int code = bean.getCode();
                    if (code == ApiErrorCode.CODE_SUCCESS) {
                        subscriber.onNext(bean);
                    } else if (code == ApiErrorCode.EXPIRETIME_TOKEN
                            || code == ApiErrorCode.EXCEPT_ACC) {
                        try {
                            clearUser();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } finally {
                            subscriber.onError(new ApiException(code, bean.getMsg()));
                        }
                    } else {
                        subscriber.onError(new ApiException(code, bean.getMsg()));
                    }
                }
            });

        });
    }

    @Override
    public Observable<Boolean> cacheBooks(List<Book> books) {
        return Observable.create((subscriber) -> {
            try {
                initBookDAO();
                for (Book book : books) {
                    boolean res = bookDao.addBook(book);
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
