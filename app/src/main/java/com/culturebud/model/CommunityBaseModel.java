package com.culturebud.model;

import android.text.TextUtils;

import com.culturebud.ApiErrorCode;
import com.culturebud.bean.ApiResultBean;
import com.culturebud.contract.BaseModel;
import com.culturebud.net.ApiCommunityInterface;
import com.culturebud.util.ApiException;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by XieWei on 2016/11/3.
 */

public abstract class CommunityBaseModel extends BaseModel {

    protected ApiCommunityInterface getCommunityInterface() {
        return initRetrofit().create(ApiCommunityInterface.class);
    }

    public Observable<Boolean> thumbUp(String token, int goodType, long goodObjId) {
        return Observable.create((subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }
            params.put("goodType", goodType);
            params.put("goodObjId", goodObjId);
            getCommunityInterface().thumbUp(params).subscribe(new Subscriber<ApiResultBean<JsonObject>>() {
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
                        Gson gson = new Gson();
                        JsonObject jobj = bean.getData();
                        if (jobj.has("good")) {
                            JsonElement je = jobj.get("good");
                            boolean res = gson.fromJson(je, boolean.class);
                            subscriber.onNext(res);
                        }
                    } else {
                        subscriber.onError(new ApiException(code, bean.getMsg()));
                    }
                }
            });
        }));
    }

    public Observable<Boolean> addComment(String token, long communityId, String content) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }
            params.put("communityId", communityId);
            params.put("content", content);
            getCommunityInterface().addComment(params).subscribe(new Subscriber<ApiResultBean<JsonObject>>() {
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
