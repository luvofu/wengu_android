package com.culturebud.model;

import android.text.TextUtils;

import com.culturebud.ApiErrorCode;
import com.culturebud.bean.ApiResultBean;
import com.culturebud.bean.BookCircleDynamic;
import com.culturebud.contract.DynamicDetailContract;
import com.culturebud.net.ApiBookHomeInterface;
import com.culturebud.util.ApiException;
import com.google.gson.JsonObject;

import java.util.Map;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by XieWei on 2017/1/4.
 */

public class DynamicDetailModel extends DynamicDetailContract.Model {
    @Override
    public Observable<BookCircleDynamic> dynamicDetail(String token, long dynamicId) {
        return Observable.create(subscriber -> {
           Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }
            params.put("dynamicId", dynamicId);
            initRetrofit().create(ApiBookHomeInterface.class).dynamicDetail(params)
            .subscribe(new Subscriber<ApiResultBean<BookCircleDynamic>>() {
                @Override
                public void onCompleted() {
                    subscriber.onCompleted();
                }

                @Override
                public void onError(Throwable e) {
                    subscriber.onError(e);
                }

                @Override
                public void onNext(ApiResultBean<BookCircleDynamic> bean) {
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
