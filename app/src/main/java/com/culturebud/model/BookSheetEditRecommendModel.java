package com.culturebud.model;

import android.text.TextUtils;

import com.culturebud.ApiErrorCode;
import com.culturebud.bean.ApiResultBean;
import com.culturebud.contract.BookSheetEditRecommendContract;
import com.culturebud.net.ApiBookSheetInterface;
import com.culturebud.util.ApiException;
import com.google.gson.JsonObject;

import java.util.Map;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by XieWei on 2017/1/20.
 */

public class BookSheetEditRecommendModel extends BookSheetEditRecommendContract.Model {
    @Override
    public Observable<Boolean> editRecommend(String token, long sheetBookId, String recommend) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }
            params.put("sheetBookId", sheetBookId);
            params.put("recommend", recommend);
            initRetrofit().create(ApiBookSheetInterface.class).bookSheetEditRecommend(params)
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
