package com.culturebud.model;

import android.text.TextUtils;

import com.culturebud.ApiErrorCode;
import com.culturebud.bean.ApiResultBean;
import com.culturebud.contract.InviteFriendContract;
import com.culturebud.net.ApiMeInterface;
import com.culturebud.util.ApiException;
import com.google.gson.JsonObject;

import java.util.Map;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by XieWei on 2016/12/28.
 */

public class InviteFriendModel extends InviteFriendContract.Model {

    @Override
    public Observable<Boolean> invite(String token, long acceptUserId, String content) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }
            params.put("acceptUserId", acceptUserId);
            if (!TextUtils.isEmpty(content)) {
                params.put("content", content);
            }
            initRetrofit().create(ApiMeInterface.class).inviteFriend(params)
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
