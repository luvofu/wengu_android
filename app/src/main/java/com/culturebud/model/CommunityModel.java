package com.culturebud.model;

import android.text.TextUtils;

import com.culturebud.bean.ApiResultBean;
import com.culturebud.contract.CommunityContract;
import com.google.gson.JsonObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;

/**
 * Created by XieWei on 2016/10/27.
 */

public class CommunityModel extends CommunityContract.Model {

    @Override
    public Observable<ApiResultBean<JsonObject>> getComments(int page, String token) {
        return Observable.create((subscriber -> {
            Map<String, Object> params = getCommonParams();
            params.put("page", page);
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }
            getCommunityInterface().comment(params)
                    .enqueue(new Callback<ApiResultBean<JsonObject>>() {
                        @Override
                        public void onResponse(Call<ApiResultBean<JsonObject>> call, Response<ApiResultBean<JsonObject>> response) {
                            subscriber.onNext(response.body());
                            subscriber.onCompleted();
                        }

                        @Override
                        public void onFailure(Call<ApiResultBean<JsonObject>> call, Throwable t) {
                            subscriber.onError(t);
                        }
                    });
        }));
    }

}
