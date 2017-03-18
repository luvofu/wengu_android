package com.culturebud.model;

import android.text.TextUtils;
import android.util.Log;

import com.culturebud.ApiErrorCode;
import com.culturebud.bean.ApiResultBean;
import com.culturebud.bean.BookMark;
import com.culturebud.contract.BookHomeContract;
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
 * Created by XieWei on 2017/3/18.
 */

public class BookHomeModel extends BookHomeContract.Model {
    @Override
    public Observable<List<BookMark>> getBookMarks(String token) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }
            initRetrofit().create(ApiBookHomeInterface.class).getMyBookMarks(params)
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
                                Log.d("xwlljj", bean.toString());
                                if (bean.getData().has("bookmarkList")) {
                                    List<BookMark> bookMarks = new Gson().fromJson(bean.getData().getAsJsonArray
                                            ("bookmarkList"), new
                                            TypeToken<List<BookMark>>() {
                                            }.getType());
                                    subscriber.onNext(bookMarks);
                                }
                            } else {
                                subscriber.onError(new ApiException(code, bean.getMsg()));
                            }
                        }
                    });
        });
    }
}
