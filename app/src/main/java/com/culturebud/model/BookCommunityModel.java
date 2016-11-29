package com.culturebud.model;

import android.text.TextUtils;

import com.culturebud.ApiErrorCode;
import com.culturebud.bean.ApiResultBean;
import com.culturebud.bean.BookCommunityDetail;
import com.culturebud.bean.Comment;
import com.culturebud.contract.BookCommunityContract;
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
 * Created by XieWei on 2016/11/10.
 */

public class BookCommunityModel extends BookCommunityContract.Model {
    @Override
    public Observable<BookCommunityDetail> getCommunityDetail(String token, long communityId) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }
            params.put("communityId", communityId);
            initRetrofit().create(ApiCommunityInterface.class).communityDetail(params)
                    .subscribe(new Subscriber<ApiResultBean<BookCommunityDetail>>() {
                        @Override
                        public void onCompleted() {
                            subscriber.onCompleted();
                        }

                        @Override
                        public void onError(Throwable e) {
                            subscriber.onError(e);
                        }

                        @Override
                        public void onNext(ApiResultBean<BookCommunityDetail> bean) {
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
    public Observable<List<Comment>> getComments(String token, int page, int sortType, long communityId) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }
            params.put("page", page);
            params.put("sortType", sortType);
            params.put("communityId", communityId);
            initRetrofit().create(ApiCommunityInterface.class).communityComment(params)
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
                                if (jobj.has("commentList")) {
                                    JsonArray jarr = jobj.getAsJsonArray("commentList");
                                    Gson gson = new Gson();
                                    List<Comment> comments = gson.fromJson(jarr, new TypeToken<List<Comment>>() {
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
}
