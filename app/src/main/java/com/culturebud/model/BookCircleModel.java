package com.culturebud.model;

import android.text.TextUtils;
import android.util.Log;

import com.culturebud.ApiErrorCode;
import com.culturebud.bean.ApiResultBean;
import com.culturebud.contract.BookCircleContract;
import com.culturebud.net.ApiBookHomeInterface;
import com.culturebud.util.ApiException;
import com.google.gson.JsonObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by XieWei on 2016/10/29.
 */

public class BookCircleModel extends BookCircleContract.Model {
    private static final String TAG = BookCircleModel.class.getSimpleName();

    @Override
    public Observable<ApiResultBean<JsonObject>> getDynamics(
            int page, String token, long userId) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }
            params.put("page", page);
            params.put("userId", userId);
            initRetrofit().create(ApiBookHomeInterface.class).dynamic(params).subscribe(new Subscriber<ApiResultBean<JsonObject>>() {
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
                    subscriber.onNext(bean);
                }
            });
        });
    }
}
