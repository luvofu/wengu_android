package com.culturebud.model;

import android.text.TextUtils;
import android.util.Log;

import com.culturebud.ApiErrorCode;
import com.culturebud.bean.ApiResultBean;
import com.culturebud.bean.Friend;
import com.culturebud.contract.MyFriendsContract;
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
 * Created by XieWei on 2016/12/5.
 */

public class MyFriendsModel extends MyFriendsContract.Model {
    private static final String TAG = MyFriendsModel.class.getSimpleName();

    public Observable<Boolean> concern(String token, long friendId) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            params.put("friendId", friendId);
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }

            initRetrofit().create(ApiMeInterface.class).concern(params)
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
                            Log.d(TAG, bean.toString());
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
    public Observable<List<Friend>> concers(String token, long userId) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }
            params.put("userId", userId);

            initRetrofit().create(ApiMeInterface.class).myConcers(params)
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
                            Log.d(TAG, bean.toString());
                            int code = bean.getCode();
                            if (code == ApiErrorCode.CODE_SUCCESS) {
                                Log.d(TAG, bean.getData().toString());
                                List<Friend> friends = new Gson().fromJson(
                                        bean.getData().getAsJsonArray("concernList"), new TypeToken<List<Friend>>() {
                                        }.getType());
                                subscriber.onNext(friends);
                            } else {
                                subscriber.onError(new ApiException(code, bean.getMsg()));
                            }
                        }
                    });
        });
    }

    @Override
    public Observable<List<Friend>> fans(String token, long userId) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }
            params.put("userId", userId);

            initRetrofit().create(ApiMeInterface.class).myFans(params)
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
                            Log.d(TAG, bean.toString());
                            int code = bean.getCode();
                            if (code == ApiErrorCode.CODE_SUCCESS) {
                                Log.d(TAG, bean.getData().toString());
                                List<Friend> friends = new Gson().fromJson(
                                        bean.getData().getAsJsonArray("fanList"), new TypeToken<List<Friend>>() {
                                        }.getType());
                                subscriber.onNext(friends);
                            } else {
                                subscriber.onError(new ApiException(code, bean.getMsg()));
                            }
                        }
                    });
        });
    }
}
