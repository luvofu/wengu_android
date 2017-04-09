package com.culturebud.model;

import android.text.TextUtils;
import android.util.Log;

import com.culturebud.ApiErrorCode;
import com.culturebud.bean.ApiResultBean;
import com.culturebud.contract.BaseModel;
import com.culturebud.net.ApiBookInterface;
import com.culturebud.net.ApiBookSheetInterface;
import com.culturebud.util.ApiException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by XieWei on 2016/11/11.
 */

public abstract class BookBaseModel extends BaseModel {
    public Observable<Boolean> collectAdd(String token, int collectType, long collectObjId) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }
            params.put("collectType", collectType);
            params.put("collectObjId", collectObjId);
            initRetrofit().create(ApiBookInterface.class).collectAdd(params)
                    .subscribe(new Subscriber<ApiResultBean<JsonObject>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            subscriber.onError(e);
                        }

                        @Override
                        public void onNext(ApiResultBean<JsonObject> bean) {
                            int code = bean.getCode();
                            Log.e("xwlljj", bean.toString());
                            if (code == ApiErrorCode.CODE_SUCCESS) {
                                subscriber.onNext(true);
                            } else {
                                subscriber.onError(new ApiException(code, bean.getMsg()));
                            }
                        }
                    });
        });
    }

    public Observable<Boolean> collectDel(String token, int collectType, long collectObjId) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }
            params.put("collectType", collectType);
            params.put("collectObjId", collectObjId);
            initRetrofit().create(ApiBookInterface.class).collectDel(params)
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
                                subscriber.onNext(false);
                            } else {
                                subscriber.onError(new ApiException(code, bean.getMsg()));
                            }
                        }
                    });
        });
    }

    public Observable<Boolean> bookSheetAddBook(String token, long sheetId, long bookId) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }
            params.put("sheetId", sheetId);
            params.put("bookId", bookId);
            initRetrofit().create(ApiBookSheetInterface.class).bookSheetAddBook(params)
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
                                subscriber.onNext(true);
                            } else {
                                subscriber.onError(new ApiException(code, bean.getMsg()));
                            }
                        }
                    });
        });
    }

    public Observable<Boolean> bookSheetDelBook(String token, long sheetBookId) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }
            params.put("sheetBookId", sheetBookId);
            initRetrofit().create(ApiBookSheetInterface.class).bookSheetDelBook(params)
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
                                subscriber.onNext(true);
                            } else {
                                subscriber.onError(new ApiException(code, bean.getMsg()));
                            }
                        }
                    });
        });
    }

    public Observable<List<String>> getBookTags() {
        return Observable.create(subscriber -> {
            initRetrofit().create(ApiBookInterface.class).bookTags(getCommonParams())
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
                                if (bean.getData().has("bookTagList")) {
                                    Gson gson = new Gson();
                                    List<String> tags = gson.fromJson(bean.getData().getAsJsonArray("bookTagList"), new
                                            TypeToken<List<String>>() {
                                            }.getType());
                                    subscriber.onNext(tags);
                                } else {
                                    subscriber.onNext(new ArrayList<String>());
                                }
                            } else {
                                subscriber.onError(new ApiException(code, bean.getMsg()));
                            }
                        }
                    });
        });
    }
}
