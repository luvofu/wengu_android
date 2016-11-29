package com.culturebud.model;

import android.text.TextUtils;

import com.culturebud.bean.ApiResultBean;
import com.culturebud.contract.BookSheetDetailContract;
import com.culturebud.net.ApiBookSheetInterface;
import com.culturebud.util.ApiException;
import com.google.gson.JsonObject;

import java.util.Map;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by XieWei on 2016/11/7.
 */

public class BookSheetDetailModel extends BookSheetDetailContract.Model {
    @Override
    public Observable<JsonObject> getBookSheetDetail(String token, long sheetId) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }
            params.put("sheetId", sheetId);
            initRetrofit().create(ApiBookSheetInterface.class)
                    .getBookSheetDetail(params).subscribe(new Subscriber<ApiResultBean<JsonObject>>() {
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
                        subscriber.onNext(jobj);
                    } else {
                        subscriber.onError(new ApiException(code, bean.getMsg()));
                    }
                }
            });
        });
    }
}
