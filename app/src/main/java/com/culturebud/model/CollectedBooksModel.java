package com.culturebud.model;

import android.text.TextUtils;
import android.util.Log;

import com.culturebud.ApiErrorCode;
import com.culturebud.bean.ApiResultBean;
import com.culturebud.bean.BookCategoryGroup;
import com.culturebud.bean.CollectedBook;
import com.culturebud.contract.CollectedBooksContract;
import com.culturebud.net.ApiBookHomeInterface;
import com.culturebud.net.ApiBookInterface;
import com.culturebud.net.ApiCollectedInterface;
import com.culturebud.util.ApiException;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by XieWei on 2016/11/9.
 */

public class CollectedBooksModel extends CollectedBooksContract.Model {
    @Override
    public Observable<List<CollectedBook>> getCollectedBooks(String token, long userId, int page) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }
            params.put("userId", userId);
            params.put("page", page);
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

    @Override
    public Observable<List<CollectedBook>> getCollectedBooks(String token, long userId, int page, int
            categoryType, String category) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }
            params.put("userId", userId);
            params.put("page", page);
            params.put("categoryType", categoryType);
            params.put("category", category);
            initRetrofit().create(ApiBookInterface.class).getUserCollectedBooksNew(params)
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

    @Override
    public Observable<BookCategoryGroup> getCategoryStatistics(String token, long userId) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }
            params.put("userId", userId);
            initRetrofit().create(ApiBookInterface.class).getCategoryStatistics(params)
            .subscribe(new Subscriber<ApiResultBean<BookCategoryGroup>>() {
                @Override
                public void onCompleted() {
                    subscriber.onCompleted();
                }

                @Override
                public void onError(Throwable e) {
                    subscriber.onError(e);
                }

                @Override
                public void onNext(ApiResultBean<BookCategoryGroup> bean) {
                    int code = bean.getCode();
                    if (code == ApiErrorCode.CODE_SUCCESS) {
                        subscriber.onNext(bean.getData());
                    } else {
                        subscriber.onError(new ApiException(code, bean.getMsg()));
                    }
                }
            });
        });
    }

    @Override
    public Observable<Boolean> deleteUserBooks(String token, Set<CollectedBook> userBooks) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }
            String userBookIdList = "";
            if (userBooks != null && userBooks.size() > 0) {
                Iterator<CollectedBook> cbs = userBooks.iterator();
                while (cbs.hasNext()) {
                    CollectedBook cb = cbs.next();
                    userBookIdList = cb.getUserBookId() + "|";
                }
                userBookIdList = userBookIdList.substring(0, userBookIdList.lastIndexOf("|"));
            }
            params.put("userBookIdList", userBookIdList);
            initRetrofit().create(ApiBookInterface.class).deleteUserBooks(params)
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
    public Observable<Boolean> alterReadStatus(String token, Set<CollectedBook> userBooks, int readStatus) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }
            Iterator<CollectedBook> iterator = userBooks.iterator();
            String ids = "";
            while (iterator.hasNext()) {
                CollectedBook cb = iterator.next();
                ids = ids + cb.getUserBookId() + "|";
            }
            ids = ids.substring(0, ids.lastIndexOf("|"));
            params.put("userBookIds", ids);
            params.put("readStatus", readStatus);
            initRetrofit().create(ApiCollectedInterface.class).alterBookReadStatus(params)
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
