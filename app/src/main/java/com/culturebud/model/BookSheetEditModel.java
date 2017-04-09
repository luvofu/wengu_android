package com.culturebud.model;

import android.text.TextUtils;

import com.culturebud.ApiErrorCode;
import com.culturebud.bean.ApiResultBean;
import com.culturebud.contract.BookSheetEditContract;
import com.culturebud.net.ApiBookSheetInterface;
import com.culturebud.util.ApiException;
import com.google.gson.JsonObject;

import java.util.Map;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by XieWei on 2017/4/8.
 */

public class BookSheetEditModel extends BookSheetEditContract.Model {
    @Override
    public Observable<Boolean> editBookSheet(String token, long bookSheetId, String bsName, String desc, String tag) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }
            params.put("sheetId", bookSheetId);
            if (!TextUtils.isEmpty(bsName)) {
                params.put("name", bsName);
            }
            if (!TextUtils.isEmpty(desc)) {
                params.put("description", desc);
            }
            if (!TextUtils.isEmpty(tag)) {
                params.put("tag", tag);
            }
            initRetrofit().create(ApiBookSheetInterface.class).editBookSheet(params)
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
