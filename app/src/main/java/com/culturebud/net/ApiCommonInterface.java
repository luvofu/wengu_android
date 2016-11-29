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
 * Created by XieWei on 2016/11/15.
 */

public interface ApiCommonInterface {

    /**
     * String token;令牌
     * int deleteType ;删除类型：
     * 评论Comment(0),评论回复CommentReply(1),
     * 动态Dynamic(2),动态回复DynamicReply(3);
     * long deleteObjId ;删除对象id
     *
     * @param params
     * @return
     */
    @POST(CommonConst.PATH_COMMON_DEL)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> commonDelete(@FieldMap Map<String, Object> params);

}
