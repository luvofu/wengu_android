package com.culturebud.model;

import android.text.TextUtils;

import com.culturebud.ApiErrorCode;
import com.culturebud.bean.ApiResultBean;
import com.culturebud.bean.Category;
import com.culturebud.contract.CustomCategoriesContract;
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
 * Created by XieWei on 2017/3/31.
 */

public class CustomCategoriesModel extends CustomCategoriesContract.Model {
    @Override
    public Observable<List<Category>> customCategories(String token) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }
            initRetrofit().create(ApiBookHomeInterface.class).customCategories(params)
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
                                if (bean.getData().has("categoryStatisList")) {
                                    JsonArray jarr = bean.getData().getAsJsonArray("categoryStatisList");
                                    List<Category> categories = new Gson().fromJson(jarr, new
                                            TypeToken<List<Category>>() {
                                    }.getType());
                                    for (Category c : categories) {
                                        c.setType(2);
                                    }
                                    subscriber.onNext(categories);
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
