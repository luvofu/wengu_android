package com.culturebud.net;

import com.culturebud.CommonConst;
import com.culturebud.bean.ApiResultBean;
import com.culturebud.bean.BookCategoryGroup;
import com.culturebud.bean.BookDetail;
import com.culturebud.bean.UserBookInfo;
import com.google.gson.JsonObject;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by XieWei on 2016/11/3.
 */

public interface ApiBookInterface {

    /**
     * int page;页
     * int sortType;排序方式：评分rating(0),录入数entryNum(1);
     * String filterType;筛选条件：(默认不传)
     * 最多复选三个，格式如：|分割，如”哲学|文学|小说”
     *
     * @return
     */
    @POST(CommonConst.PATH_BOOKS)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> getBooks(@FieldMap Map<String, Object> params);

    /**
     * int page;页
     * String keyword;关键字
     *
     * @param params
     * @return
     */
    @POST(CommonConst.PATH_BOOK_SEARCH)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> searchBooks(@FieldMap Map<String, Object> params);

    /**
     * int page;页
     * String keyword;关键字
     *
     * @param params
     * @return
     */
    @POST(CommonConst.IMPORT_BOOK_SEARCH)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> importSearchBooks(@FieldMap Map<String, Object> params);
    /**
     * String token;用户令牌
     * long bookId;书籍id
     *
     * @return
     */
    @POST(CommonConst.PATH_BOOK_DETAIL)
    @FormUrlEncoded
    Observable<ApiResultBean<BookDetail>> getBookDetail(@FieldMap Map<String, Object> params);

    /**
     * String token;令牌
     * long userId;用户id（请求信息所属用户id）
     *
     * @return
     */
    @POST(CommonConst.PATH_USER_BOOKS)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> getUserCollectedBooks(@FieldMap Map<String, Object> params);

    @POST(CommonConst.PATH_USER_BOOKS_V104)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> getUserCollectedBooksNew(@FieldMap Map<String, Object> params);

    @POST(CommonConst.PATH_COLLECT_ADD)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> collectAdd(@FieldMap Map<String, Object> params);

    @POST(CommonConst.PATH_COLLECT_DELETE)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> collectDel(@FieldMap Map<String, Object> params);

    /**
     * userBookId
     *
     * @param params
     * @return
     */
    @POST(CommonConst.PATH_USER_BOOK_DETAIL)
    @FormUrlEncoded
    Observable<ApiResultBean<UserBookInfo>> myBookInfos(@FieldMap Map<String, Object> params);

    @POST(CommonConst.PATH_BOOK_TAGS)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> bookTags(@FieldMap Map<String, Object> params);

    @POST(CommonConst.PATH_USER_BOOK_CATEGORY_STATISTICS)
    @FormUrlEncoded
    Observable<ApiResultBean<BookCategoryGroup>> getCategoryStatistics(@FieldMap Map<String, Object> params);

    @POST(CommonConst.PATH_USER_BOOK_DELETE)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> deleteUserBooks(@FieldMap Map<String, Object> params);

    @POST(CommonConst.BOOK_SEARCH_FILTER)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> getBookFilters(@FieldMap Map<String, Object> params);

    @POST(CommonConst.PATH_BOOK_SHEET_TAGS)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> getBookSheetFilters(@FieldMap Map<String, Object> params);
}

