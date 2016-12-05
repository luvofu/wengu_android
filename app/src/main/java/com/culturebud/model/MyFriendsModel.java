package com.culturebud.model;

import android.text.TextUtils;
import android.util.Log;

import com.culturebud.ApiErrorCode;
import com.culturebud.bean.ApiResultBean;
import com.culturebud.bean.User;
import com.culturebud.contract.MyFriendsContract;
import com.culturebud.net.ApiMeInterface;
import com.culturebud.util.ApiException;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import net.sourceforge.pinyin4j.PinyinHelper;

import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by XieWei on 2016/12/5.
 */

public class MyFriendsModel extends MyFriendsContract.Model {
    private static final String TAG = MyFriendsModel.class.getSimpleName();
    @Override
    public Observable<List<User>> myFriends(String token) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }

            initRetrofit().create(ApiMeInterface.class).myFriends(params)
            .subscribe(new Subscriber<ApiResultBean<JsonArray>>() {
                @Override
                public void onCompleted() {
                    subscriber.onCompleted();
                }

                @Override
                public void onError(Throwable e) {
                    subscriber.onError(e);
                }

                @Override
                public void onNext(ApiResultBean<JsonArray> bean) {
                    Log.d(TAG, bean.toString());
                    int code = bean.getCode();
                    if (code == ApiErrorCode.CODE_SUCCESS) {
                        Log.d(TAG, bean.getData().toString());
                        List<User> users = new Gson().fromJson(bean.getData(), new TypeToken<List<User>>() {
                        }.getType());
                        subscriber.onNext(users);
                    } else {
                        subscriber.onError(new ApiException(code, bean.getMsg()));
                    }
                }
            });
        });
    }
}
