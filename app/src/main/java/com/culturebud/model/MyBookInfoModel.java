package com.culturebud.model;

import android.text.TextUtils;
import android.util.Log;

import com.culturebud.ApiErrorCode;
import com.culturebud.bean.ApiResultBean;
import com.culturebud.bean.UserBookInfo;
import com.culturebud.contract.MyBookInfoContract;
import com.culturebud.net.ApiBookInterface;
import com.culturebud.util.ApiException;
import com.google.gson.JsonObject;

import java.util.Map;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by XieWei on 2017/3/2.
 */

public class MyBookInfoModel extends MyBookInfoContract.Model {
    @Override
    public Observable<UserBookInfo> myBookInfo(String token, long userBookId) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }
            params.put("userBookId", userBookId);
            initRetrofit().create(ApiBookInterface.class).myBookInfos(params)
            .subscribe(new Subscriber<ApiResultBean<UserBookInfo>>() {
                @Override
                public void onCompleted() {
                    subscriber.onCompleted();
                }

                @Override
                public void onError(Throwable e) {
                    subscriber.onError(e);
                }

                @Override
                public void onNext(ApiResultBean<UserBookInfo> bean) {
                    int code = bean.getCode();
                    if (code == ApiErrorCode.CODE_SUCCESS) {
                        Log.d("xwlljj", bean.getData().toString());
                        subscriber.onNext(bean.getData());
                    } else {
                        subscriber.onError(new ApiException(code, bean.getMsg()));
                    }
                }
            });
        });
    }
}
