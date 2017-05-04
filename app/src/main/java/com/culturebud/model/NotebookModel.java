package com.culturebud.model;

import android.text.TextUtils;

import com.culturebud.ApiErrorCode;
import com.culturebud.bean.ApiResultBean;
import com.culturebud.bean.Notebook;
import com.culturebud.contract.NotebookContract;
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
 * Created by XieWei on 2016/11/20.
 */

public class NotebookModel extends NotebookContract.Model {
    @Override
    public Observable<List<Notebook>> userNotebooks(String token, long userId, int page) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }
            params.put("userId", userId);
            params.put("page", page);
            initRetrofit().create(ApiBookHomeInterface.class).myNoteBooks(params)
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
                        JsonObject jobj = bean.getData();
                        if (jobj.has("noteBookList")) {//noteBookList, 服务器返回b为大写，api与文档不一至
                            JsonArray jarr = jobj.getAsJsonArray("noteBookList");
                            List<Notebook> notebooks = new Gson().fromJson(jarr, new TypeToken<List<Notebook>>() {
                            }.getType());
                            subscriber.onNext(notebooks);
                        }
                    } else {
                        subscriber.onError(new ApiException(code, bean.getMsg()));
                    }
                }
            });
        });
    }

    @Override
    public Observable<Boolean> deleteNotebook(String token, long notebookId) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }
            params.put("notebookId", notebookId);
            initRetrofit().create(ApiBookHomeInterface.class).deleteNotebook(params)
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

    @Override
    public Observable<Boolean> createNotebook(String token, long bookId) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }
            params.put("bookId", bookId);
            initRetrofit().create(ApiBookHomeInterface.class).createNoteBook(params)
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

}
