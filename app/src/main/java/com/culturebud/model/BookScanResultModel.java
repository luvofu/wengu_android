package com.culturebud.model;

import android.text.TextUtils;

import com.culturebud.ApiErrorCode;
import com.culturebud.bean.ApiResultBean;
import com.culturebud.bean.Book;
import com.culturebud.contract.BookScanResultContract;
import com.culturebud.net.ApiBookHomeInterface;
import com.culturebud.util.ApiException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by XieWei on 2017/4/5.
 */

public class BookScanResultModel extends BookScanResultContract.Model {

    @Override
    public Observable<Boolean> addScanBooks(String token, Map<String, List<Book>> scanBooks) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }
            Set<String> categories = scanBooks.keySet();
            List<Map<String, Object>> list = new ArrayList<>();
            for (String category : categories) {
                Map<String, Object> json = new HashMap<>();
                List<Book> books = scanBooks.get(category);
                List<Long> ids = new ArrayList<>();
                for (Book b : books) {
                    ids.add(b.getBookId());
                }
                json.put("category", category);
                json.put("bookIds", ids);
                list.add(json);
            }
            params.put("scanBooks", new Gson().toJson(list));
            initRetrofit().create(ApiBookHomeInterface.class).addScanBook(params)
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
