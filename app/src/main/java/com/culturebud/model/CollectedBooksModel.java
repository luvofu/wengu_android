package com.culturebud.model;

import android.text.TextUtils;

import com.culturebud.bean.ApiResultBean;
import com.culturebud.bean.CollectedBook;
import com.culturebud.contract.CollectedBooksContract;
import com.culturebud.net.ApiBookInterface;
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
 * Created by XieWei on 2016/11/9.
 */

public class CollectedBooksModel extends CollectedBooksContract.Model {
    @Override
    public Observable<List<CollectedBook>> getCollectedBooks(String token, long userId, int page) {
        Map<String, Object> params = getCommonParams();
        if (!TextUtils.isEmpty(token)) {
            params.put(TOKEN_KEY, token);
        }
        params.put("userId", userId);
        params.put("page", page);

        return Observable.create(subscriber -> {
            initRetrofit().create(ApiBookInterface.class).getUserCollectedBooks(params)
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
                        public void onNext(ApiResultBean<JsonObject> res) {
                            int code = res.getCode();
                            if (code == 200) {
                                JsonObject jobj = res.getData();
                                if (jobj.has("userBookList")) {
                                    JsonArray jarr = jobj.getAsJsonArray("userBookList");
                                    List<CollectedBook> books = new Gson().fromJson(jarr, new TypeToken<List<CollectedBook>>() {
                                    }.getType());
                                    subscriber.onNext(books);
                                } else {
                                    subscriber.onNext(null);
                                }
                            } else {
                                subscriber.onError(new ApiException(code, res.getMsg()));
                            }
                        }
                    });
        });
    }
}
