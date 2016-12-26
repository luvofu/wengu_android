package com.culturebud.model;

import android.text.TextUtils;
import android.util.Log;

import com.culturebud.ApiErrorCode;
import com.culturebud.bean.ApiResultBean;
import com.culturebud.bean.User;
import com.culturebud.contract.UserSearchContract;
import com.culturebud.net.ApiMeInterface;
import com.culturebud.util.ApiException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by XieWei on 2016/12/26.
 */

public class UserSearchModel extends UserSearchContract.Model {
    @Override
    public Observable<List<User>> search(String token, String keyword, int page) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }
            params.put("keyword", keyword);
            params.put("page", page);
            initRetrofit().create(ApiMeInterface.class).searchUser(params)
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
                    Log.d("xwlljj", bean.toString());
                    int code = bean.getCode();
                    if (code == ApiErrorCode.CODE_SUCCESS) {
                        JsonObject jobj = bean.getData();
                        if (jobj.has("userList")) {
                            List<User> users = new Gson().fromJson(jobj.getAsJsonArray("userList"), new TypeToken<List<User>>() {
                            }.getType());
                            subscriber.onNext(users);
                        } else {
                            subscriber.onNext(new ArrayList<User>());
                        }
                    } else {
                        subscriber.onError(new ApiException(code, bean.getMsg()));
                    }
                }
            });
        });
    }
}
