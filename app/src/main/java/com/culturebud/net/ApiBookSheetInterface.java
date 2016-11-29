package com.culturebud.net;

import com.culturebud.CommonConst;
import com.culturebud.bean.ApiResultBean;
import com.google.gson.JsonObject;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by XieWei on 2016/11/4.
 */

public interface ApiBookSheetInterface {

    /**
     * String token;令牌
     * int sortType;排序类型：收藏数collectionNum(0),创建时间createdTime(1);
     * String filterType;筛选条件：最多复选三个，格式如：|分割，如“哲学|文学|小说”
     * int page;页
     *
     * @param params
     * @return
     */
    @POST(CommonConst.PATH_BOOK_SHEETS)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> getBookSheets(@FieldMap Map<String, Object> params);

    /**
     * String token;令牌
     * long sheetId;书单id
     *
     * @param params
     * @return
     */
    @POST(CommonConst.PATH_BOOK_SHEET_DETAIL)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> getBookSheetDetail(@FieldMap Map<String, Object> params);

    @POST(CommonConst.PATH_USER_BOOK_SHEETS)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> getCollectedBookSheets(@FieldMap Map<String, Object> params);
}
