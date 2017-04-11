package com.culturebud.model;

import android.text.TextUtils;

import com.culturebud.ApiErrorCode;
import com.culturebud.bean.ApiResultBean;
import com.culturebud.bean.BookCircleDynamic;
import com.culturebud.bean.BookCircleDynamicRelationMe;
import com.culturebud.contract.MyDynamicsContract;
import com.culturebud.net.ApiBookHomeInterface;
import com.culturebud.util.ApiException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by XieWei on 2017/1/12.
 */

public class MyDynamicsModel extends MyDynamicsContract.Model {

    @Override
    public Observable<List<BookCircleDynamic>> myPublished(String token, int page) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }
            params.put("page", page);
            initRetrofit().create(ApiBookHomeInterface.class).myPublishedDynamics(params)
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
                                if (bean.getData().has("dynamicPublishList")) {
                                    List<BookCircleDynamic> dynamics = new Gson()
                                            .fromJson(bean.getData().getAsJsonArray("dynamicPublishList"),
                                                    new TypeToken<List<BookCircleDynamic>>() {
                                                    }.getType());
                                    subscriber.onNext(dynamics);
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
    public Observable<List<BookCircleDynamicRelationMe>> myRelations(String token, int page) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }
            params.put("page", page);
            initRetrofit().create(ApiBookHomeInterface.class).myRelationDynamics(params)
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
                                if (bean.getData().has("dynamicRelativeToMeList")) {
                                    List<BookCircleDynamicRelationMe> dynamics = new Gson()
                                            .fromJson(bean.getData().getAsJsonArray("dynamicRelativeToMeList"),
                                                    new TypeToken<List<BookCircleDynamicRelationMe>>() {
                                                    }.getType());
                                    subscriber.onNext(dynamics);
                                    try {
                                        FileOutputStream fis = new FileOutputStream("/mnt/sdcard/relation.json");
                                        fis.write(bean.toString().getBytes());
                                        fis.close();
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                subscriber.onError(new ApiException(code, bean.getMsg()));
                            }
                        }
                    });
        });
    }
}
