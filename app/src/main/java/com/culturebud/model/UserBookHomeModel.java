package com.culturebud.model;

import android.text.TextUtils;

import com.culturebud.ApiErrorCode;
import com.culturebud.bean.ApiResultBean;
import com.culturebud.bean.BookCircleDynamic;
import com.culturebud.bean.DynamicReply;
import com.culturebud.bean.User;
import com.culturebud.contract.UserBookHomeContract;
import com.culturebud.net.ApiBookHomeInterface;
import com.culturebud.net.ApiMeInterface;
import com.culturebud.util.ApiException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by XieWei on 2016/12/30.
 */

public class UserBookHomeModel extends UserBookHomeContract.Model {

    @Override
    public Observable<User> getUserProfile(String token, long userId) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }
            params.put("userId", userId);
            initRetrofit().create(ApiMeInterface.class).getUserProfile(params)
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
                                if (bean.getData().has("userProfile")) {
                                    Gson gson = new Gson();
                                    User user = gson.fromJson(bean.getData().getAsJsonObject("userProfile"), User.class);
                                    if (bean.getData().has("relationType")) {
                                        user.setRelationType(gson.fromJson(bean.getData().get("relationType"), int.class));
                                    }
                                    subscriber.onNext(user);
                                } else {

                                }
                            } else {
                                subscriber.onError(new ApiException(code, bean.getMsg()));
                            }
                        }
                    });
        });
    }

    @Override
    public Observable<List<BookCircleDynamic>> getDynamics(String token, long userId, int page) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }
            params.put("userId", userId);
            params.put("page", page);
            initRetrofit().create(ApiBookHomeInterface.class).dynamic(params)
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
                                if (bean.getData().has("bookCircleDynamicList")) {
                                    List<BookCircleDynamic> bcds = new Gson().fromJson(bean.getData()
                                                    .getAsJsonArray("bookCircleDynamicList"),
                                            new TypeToken<List<BookCircleDynamic>>() {
                                            }.getType());
                                    subscriber.onNext(bcds);
                                } else {

                                }
                            } else {
                                subscriber.onError(new ApiException(code, bean.getMsg()));
                            }
                        }
                    });
        });
    }
}
