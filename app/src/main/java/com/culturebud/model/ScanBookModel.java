package com.culturebud.model;

import android.text.TextUtils;

import com.culturebud.ApiErrorCode;
import com.culturebud.bean.ApiResultBean;
import com.culturebud.bean.Book;
import com.culturebud.contract.ScanBookContract;
import com.culturebud.net.ApiBookHomeInterface;
import com.culturebud.util.ApiException;

import java.util.Map;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by XieWei on 2016/11/26.
 */

public class ScanBookModel extends ScanBookContract.Model {
    @Override
    public Observable<Book> scanBook(String token, String isbn) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }
            params.put("isbn", isbn);
            initRetrofit().create(ApiBookHomeInterface.class).scanBook(params)
            .subscribe(new Subscriber<ApiResultBean<Book>>() {
                @Override
                public void onCompleted() {
                    subscriber.onCompleted();
                }

                @Override
                public void onError(Throwable e) {
                    subscriber.onError(e);
                }

                @Override
                public void onNext(ApiResultBean<Book> bean) {
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
