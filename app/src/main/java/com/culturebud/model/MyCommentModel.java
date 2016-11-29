package com.culturebud.model;

import android.text.TextUtils;
import android.util.Log;

import com.culturebud.ApiErrorCode;
import com.culturebud.bean.ApiResultBean;
import com.culturebud.bean.Comment;
import com.culturebud.bean.MyRelatedComment;
import com.culturebud.contract.MyCommentContract;
import com.culturebud.net.ApiCommunityInterface;
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
 * Created by XieWei on 2016/11/19.
 */

public class MyCommentModel extends MyCommentContract.Model {
    private static final String TAG = MyCommentModel.class.getSimpleName();

    @Override
    public Observable<List<Comment>> myPublished(String token, int page) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }
            params.put("page", page);
            initRetrofit().create(ApiCommunityInterface.class).myPublish(params)
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
                                Log.d(TAG, bean.getData().toString());
                                JsonObject jobj = bean.getData();
                                if (jobj.has("commentPublishList")) {
                                    JsonArray jarr = jobj.getAsJsonArray("commentPublishList");
                                    List<Comment> comments = new Gson().fromJson(jarr, new TypeToken<List<Comment>>() {
                                    }.getType());
                                    subscriber.onNext(comments);
                                }
                            } else {
                                subscriber.onError(new ApiException(code, bean.getMsg()));
                            }
                        }
                    });
        });
    }

    @Override
    public Observable<List<MyRelatedComment>> myRelated(String token, int page) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }
            params.put("page", page);
            initRetrofit().create(ApiCommunityInterface.class).myRelated(params)
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
                                Log.d(TAG, bean.getData().toString());
                                JsonObject jobj = bean.getData();
                                if (jobj.has("commentRelativeToMeList")) {
                                    JsonArray jarr = jobj.getAsJsonArray("commentRelativeToMeList");
                                    List<MyRelatedComment> mrComments = new Gson().fromJson(jarr, new TypeToken<List<MyRelatedComment>>() {
                                    }.getType());
                                    subscriber.onNext(mrComments);
                                }
                            } else {
                                subscriber.onError(new ApiException(code, bean.getMsg()));
                            }
                        }
                    });
        });
    }
}
