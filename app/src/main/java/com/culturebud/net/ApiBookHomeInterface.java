package com.culturebud.net;

import com.culturebud.CommonConst;
import com.culturebud.bean.ApiResultBean;
import com.culturebud.bean.Book;
import com.culturebud.bean.BookCircleDynamic;
import com.culturebud.bean.DynamicReply;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import rx.Observable;

/**
 * Created by XieWei on 2016/10/28.
 */

public interface ApiBookHomeInterface {

    /**
     * String platform
     * String deviceToken
     * int page;页
     * String token;令牌
     * long userId ;用户id（请求信息所属用户id）
     */
    @POST(CommonConst.PATH_BOOK_CIRCLE_DYNAMIC)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> dynamic(@FieldMap Map<String, Object> params);

    /**
     * String token;令牌
     * long dynamicId ;动态id
     *
     * @param params
     * @return
     */
    @POST(CommonConst.PATH_BOOK_CIRCLE_DYNAMIC_DETAIL)
    @FormUrlEncoded
    Observable<ApiResultBean<BookCircleDynamic>> dynamicDetail(@FieldMap Map<String, Object> params);

    @POST(CommonConst.PATH_BOOK_CIRCLE_DYNAMIC_ADD)
    @Multipart
    Observable<ApiResultBean<JsonObject>> addDynamic(@PartMap Map<String, RequestBody> params, @Part MultipartBody
            .Part imageFile);

    @POST(CommonConst.PATH_BOOK_CIRCLE_DYNAMIC_MY_PUBLISHED)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> myPublishedDynamics(@FieldMap Map<String, Object> params);

    @POST(CommonConst.PATH_BOOK_CIRCLE_DYNAMIC_MY_RELATIONS)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> myRelationDynamics(@FieldMap Map<String, Object> params);

    /**
     * String token;令牌
     * long dynamicId;动态id
     * String content;内容
     * int replyType;回复类型：
     * 动态Dynamic(0), 回复Reply(1)
     * long replyObjId;回复对象id
     *
     * @param params
     * @return
     */
    @POST(CommonConst.PATH_BOOK_CIRCLE_DYNAMIC_REPLY)
    @FormUrlEncoded
    Observable<ApiResultBean<DynamicReply>> replyDynamic(@FieldMap Map<String, Object> params);

    @POST(CommonConst.PATH_USER_NOTEBOOKS)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> myNoteBooks(@FieldMap Map<String, Object> params);

    /**
     * String token;令牌
     * String name;笔记本名
     * long bookId;书籍id
     *
     * @param params
     * @return
     */
    @POST(CommonConst.PATH_NOTEBOOK_CREATE)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> createNoteBook(@FieldMap Map<String, Object> params);

    @POST(CommonConst.PATH_NOTEBOOK_DELETE)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> deleteNotebook(@FieldMap Map<String, Object> params);

    /**
     * String token;令牌
     * long notebookId;笔记本id
     *
     * @param params
     * @return
     */
    @POST(CommonConst.PATH_NOTEBOOK_DETAIL)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> noteBookDetail(@FieldMap Map<String, Object> params);

    /**
     * String token;令牌
     * long notebookId;笔记本id
     * int page;页
     *
     * @param params
     * @return
     */
    @POST(CommonConst.PATH_NOTES_FOR_NOTEBOOK)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> notesForNotebook(@FieldMap Map<String, Object> params);

    /**
     * String token;令牌
     * long notebookId;笔记本id
     * int permission;笔记本权限：公开Open(0),好友Friend(1),个人 Personal(2);
     *
     * @param params
     * @return
     */
    @POST(CommonConst.PATH_NOTEBOOK_PERMISSION_EDIT)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> notebookPermissionEdit(@FieldMap Map<String, Object> params);

    /**
     * String token;令牌
     * long notebookId;笔记本id
     * String name;笔记本名
     * String description;笔记本描述
     *
     * @param params
     * @return
     */
    @POST(CommonConst.PATH_NOTEBOOK_EDIT)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> notebookEdit(@FieldMap Map<String, Object> params);

    @POST(CommonConst.PATH_NOTE_CREATE)
    @Multipart
    Observable<ApiResultBean<JsonObject>> createNote(@PartMap Map<String, RequestBody> params, @Part MultipartBody
            .Part file);

    /**
     * String token;令牌
     * long noteId;笔记id
     *
     * @param params
     * @return
     */
    @POST(CommonConst.PATH_NOTE_DELETE)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> deleteNote(@FieldMap Map<String, Object> params);

    /**
     * String token;令牌
     * long noteId;笔记id
     * String content;内容
     * String chapter;章节
     * int pages;页码
     * String otherLocation;其他位置信息
     *
     * @param params
     * @return
     */
    @POST(CommonConst.PATH_NOTE_EDIT)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> editNote(@FieldMap Map<String, Object> params);

    @POST(CommonConst.PATH_SCAN_BOOK_ENTRY)
    @FormUrlEncoded
    Observable<ApiResultBean<Book>> scanBook(@FieldMap Map<String, Object> params);

    @POST(CommonConst.PATH_MANUAL_BOOK_ENTRY)
    @Multipart
    Observable<ApiResultBean<JsonObject>> manualAddBook(@PartMap Map<String, RequestBody> params, @Part MultipartBody
            .Part file);

    @POST(CommonConst.PATH_MANUAL_BOOK_CHECK)
    @Multipart
    Observable<ApiResultBean<JsonObject>> manualBookCheck(@PartMap Map<String, RequestBody> params, @Part
            MultipartBody.Part file);

    /**
     * @param params token
     * @return
     */
    @POST(CommonConst.PATH_MY_BOOK_MARKS)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> getMyBookMarks(@FieldMap Map<String, Object> params);

    @POST(CommonConst.PATH_ADD_BOOK_MARK)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> addBookMark(@FieldMap Map<String, Object> params);

    @POST(CommonConst.PATH_ALTER_BOOK_MARK)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> alterBookMark(@FieldMap Map<String, Object> params);

    @POST(CommonConst.PATH_DEL_BOOK_MARK)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> delBookMark(@FieldMap Map<String, Object> params);

    @POST(CommonConst.PATH_USER_MANUAL_ENTERING_BOOKS)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> myCreatedBooks(@FieldMap Map<String, Object> params);

    @POST(CommonConst.PATH_CUSTOM_CATEGORIES)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> customCategories(@FieldMap Map<String, Object> params);

    @POST(CommonConst.PATH_CUSTOM_CATEGORY_ADD)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> addCustomCategory(@FieldMap Map<String, Object> params);

    @POST(CommonConst.PATH_CUSTOM_CATEGORY_DEL)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> deleteCustomCategory(@FieldMap Map<String, Object> params);

    @POST(CommonConst.PATH_CUSTOM_CATEGORY_EDIT)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> editCustomCategory(@FieldMap Map<String, Object> params);

    @POST(CommonConst.PATH_MOVE_BOOK_SORT_CATEGORY)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> sortCustomCategory(@FieldMap Map<String, Object> params);


    /**
     * String token;令牌
     * private String userBookIdList;;藏书ids(“1000|1002|10003”形式)
     * private String category;类别（已存在的或新建的）
     *
     * @param params
     * @return
     */
    @POST(CommonConst.PATH_MOVE_BOOK_TO_CATEGORY)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> moveBookToCategory(@FieldMap Map<String, Object> params);

    @POST(CommonConst.PATH_SCAN_BOOK_ADD)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> addScanBook(@FieldMap Map<String, Object> params);

    @POST(CommonConst.PATH_IMPORT_BOOK_ENTRY)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> addSearchBook(@FieldMap Map<String, Object> params);

    @POST(CommonConst.PATH_BOOK_SHEET_DELETE)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> deleteBookSheet(@FieldMap Map<String, Object> params);
}
