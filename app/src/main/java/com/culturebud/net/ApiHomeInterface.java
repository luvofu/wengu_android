package com.culturebud.net;

import com.culturebud.CommonConst;
import com.culturebud.bean.ApiResultBean;
import com.google.gson.JsonObject;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by XieWei on 2016/10/24.
 */

public interface ApiHomeInterface {

    @GET(CommonConst.PATH_HOME)
    Observable<ApiResultBean<JsonObject>> getFrontPageList(@QueryMap Map<String, Object> params);
}
