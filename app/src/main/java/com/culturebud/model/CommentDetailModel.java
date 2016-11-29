package com.culturebud.model;

import android.text.TextUtils;

import com.culturebud.ApiErrorCode;
import com.culturebud.bean.ApiResultBean;
import com.culturebud.bean.CommentReply;
import com.culturebud.contract.CommentDetailContract;
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
 * Created by XieWei on 2016/11/9.
 */

public class CommentDetailModel extends CommentDetailContract.Model {


    @Override
    public Observable<List<CommentReply>> getCommentReplies(String token, long commentId, int page) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put("token", token);
            }
            params.put("commentId", commentId);
            params.put("page", page);
            initRetrofit().create(ApiCommunityInterface.class).commentReplies(params)
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
                            if (code == 200) {
                                JsonObject jobj = bean.getData();
                                if (jobj.has("commentReplyList")) {
                                    JsonArray jarr = jobj.getAsJsonArray("commentReplyList");
                                    Gson gson = new Gson();
                                    List<CommentReply> replies = gson.fromJson(jarr, new TypeToken<List<CommentReply>>() {
                                    }.getType());
                                    subscriber.onNext(replies);
                                }
                            } else {
                                subscriber.onError(new ApiException(code, bean.getMsg()));
                            }
                        }
                    });
        });
    }

    @Override
    public Observable<CommentReply> addCommentReply(String token, long commentId, String content, int replyType, long replyObjId) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }
            params.put("commentId", commentId);
            params.put("content", content);
            params.put("replyType", replyType);
            params.put("replyObjId", replyObjId);
            initRetrofit().create(ApiCommunityInterface.class).replyComment(params)
            .subscribe(new Subscriber<ApiResultBean<CommentReply>>() {
                @Override
                public void onCompleted() {
                    subscriber.onCompleted();
                }

                @Override
                public void onError(Throwable e) {
                    subscriber.onError(e);
                }

                @Override
                public void onNext(ApiResultBean<CommentReply> bean) {
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
}
