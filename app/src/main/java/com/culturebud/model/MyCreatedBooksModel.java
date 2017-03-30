package com.culturebud.model;

import android.text.TextUtils;

import com.culturebud.ApiErrorCode;
import com.culturebud.bean.ApiResultBean;
import com.culturebud.bean.CheckedBook;
import com.culturebud.contract.MyCreatedBooksContract;
import com.culturebud.net.ApiBookHomeInterface;
import com.culturebud.util.ApiException;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by XieWei on 2017/3/30.
 */

public class MyCreatedBooksModel extends MyCreatedBooksContract.Model {

    @Override
    public Observable<List<CheckedBook>> myCreatedBooks(String token, long page) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }
            params.put("page", page);
            initRetrofit().create(ApiBookHomeInterface.class).myCreatedBooks(params)
            .subscribe(new Subscriber<ApiResultBean<JsonObject>>() {
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
                        if (bean.getData().has("bookCheckList")) {
                            JsonArray jarr = bean.getData().getAsJsonArray("bookCheckList");
                            List<CheckedBook> books = new Gson().fromJson(jarr, new TypeToken<List<CheckedBook>>() {
                            }.getType());
                            subscriber.onNext(books);
                        } else {
                            subscriber.onNext(null);
                        }
                    } else {
                        subscriber.onError(new ApiException(code, bean.getMsg()));
                    }
                }
            });
        });
    }
}
