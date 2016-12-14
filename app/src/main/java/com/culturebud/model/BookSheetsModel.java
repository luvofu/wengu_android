package com.culturebud.model;

import android.text.TextUtils;

import com.culturebud.ApiErrorCode;
import com.culturebud.bean.ApiResultBean;
import com.culturebud.bean.BookSheet;
import com.culturebud.contract.BookSheetsContract;
import com.culturebud.net.ApiCollectedInterface;
import com.culturebud.util.ApiException;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by XieWei on 2016/12/13.
 */

public class BookSheetsModel extends BookSheetsContract.Model {
    @Override
    public Observable<Map<Integer, List<BookSheet>>> getMyFavoriteBookSheets(String token, int page, long userId) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }
            params.put("page", page);
            params.put("userId", userId);
            initRetrofit().create(ApiCollectedInterface.class).getMyFavoriteBookSheets(params)
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
                                JsonObject jobj = bean.getData();
                                List<BookSheet> bookSheets = null;
                                Map<Integer, List<BookSheet>> res = new HashMap<>();
                                Gson gson = new Gson();
                                int key = gson.fromJson(jobj.get("relationType"), int.class);
                                if (jobj.has("bookSheetList")) {
                                    JsonArray jarr = jobj.getAsJsonArray("bookSheetList");
                                    bookSheets = gson.fromJson(jarr, new TypeToken<List<BookSheet>>() {
                                    }.getType());
                                }
                                res.put(key, bookSheets);
                                subscriber.onNext(res);
                            } else {
                                subscriber.onError(new ApiException(code, bean.getMsg()));
                            }
                        }
                    });
        });
    }
}
