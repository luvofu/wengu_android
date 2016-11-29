package com.culturebud.net;

import com.culturebud.CommonConst;
import com.culturebud.bean.ApiResultBean;
import com.google.gson.JsonObject;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by XieWei on 2016/11/17.
 */

public interface ApiCollectedInterface {

    /**
     * String token;令牌
     * int page;页
     *
     * @param params
     * @return
     */
    @POST(CommonConst.PATH_COLLECTED_BOOKS)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> getBooks(@FieldMap Map<String, Object> params);

    /**
     * String token;令牌
     * long userId;（请求信息所属用户id）
     * int page;页
     *
     * @param params
     * @return
     */
    @POST(CommonConst.PATH_COLLECTED_BOOK_SHEETS)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> getBookSheets(@FieldMap Map<String, Object> params);

}
