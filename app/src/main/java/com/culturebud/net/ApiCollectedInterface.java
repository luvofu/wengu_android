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
    Observable<ApiResultBean<JsonObject>> getMyFavoriteBookSheets(@FieldMap Map<String, Object> params);

    /**
     * token	是	string	令牌
     * userBookIds	是	string	藏书id list，多个用【竖杠】连接
     * readStatus	是	int	阅读状态：完成Finish(0), 未读NotRead(1),正读Reading(2);
     *
     * @param params
     * @return
     */
    @POST(CommonConst.PATH_ALTER_BOOK_READ_STATUS)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> alterBookReadStatus(@FieldMap Map<String, Object> params);

    /**
     * token	是	string	令牌
     * bookType	否	number	书籍类型
     * getType	否	number	获取方式
     * enterType	否	number	录入类型
     * permission	否	number	权限
     * rating	否	number	评分
     * readStatus	否	number	读状态
     * remark	否	string	书评
     * isLease	否	boolean	可租借
     * isSale	否	boolean	可出售
     * evaluation	否	number	估价
     * dayRentGold	否	number	日租金
     * bookDescription	否	String	书籍描述
     *
     * @param params
     * @return
     */
    @POST(CommonConst.PATH_EDIT_USER_BOOK_INFO)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> editUserBookInfo(@FieldMap Map<String, Object> params);

}
