package com.culturebud.net;

import com.culturebud.CommonConst;
import com.culturebud.bean.ApiResultBean;
import com.culturebud.bean.BookCommunityDetail;
import com.culturebud.bean.CommentReply;
import com.google.gson.JsonObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by XieWei on 2016/10/27.
 */

public interface ApiCommunityInterface {

    /**
     * int page;页
     * String token ;令牌
     */
    @POST(CommonConst.PATH_COMMUNITY_COMMENTS)
    @FormUrlEncoded
    Call<ApiResultBean<JsonObject>> comment(@FieldMap Map<String, Object> params);

    /**
     * int page;页
     * String keyword;关键字
     */
    @POST(CommonConst.PATH_BOOK_COMMUNITY_SEARCH)
    @FormUrlEncoded
    Call<ApiResultBean<JsonObject>> search(@FieldMap Map<String, Object> params);

    /**
     * int page;页
     * long communityId;社区id
     */
    @POST(CommonConst.PATH_BOOK_COMMUNITY_BOOK)
    @FormUrlEncoded
    Call<ApiResultBean<JsonObject>> books(@FieldMap Map<String, Object> params);

    /**
     * long communityId; 社区id
     */
    @POST(CommonConst.PATH_BOOK_COMMUNITY_DETAIL)
    @FormUrlEncoded
    Observable<ApiResultBean<BookCommunityDetail>> communityDetail(@FieldMap Map<String, Object> params);

    /**
     * int page;页
     * int sortType;排序类型：回复数ReplyNum(0),创建时间CreatedTime(1);
     * long communityId;社区id
     *
     * @param params
     * @return
     */
    @POST(CommonConst.PATH_BOOK_COMMUNITY_COMMENTS)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> communityComment(@FieldMap Map<String, Object> params);

    /**
     * String token;令牌
     * long communityId;社区id
     * String content;内容
     *
     * @param params
     */
    @POST(CommonConst.PATH_BOOK_COMMUNITY_ADD_COMMENT)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> addComment(@FieldMap Map<String, Object> params);

    /**
     * String token;令牌
     * int goodType;点赞类型：
     * 评论Comment(0),动态 Dynamic(1);
     * long goodObjId;点赞对象
     *
     * @param params
     * @return
     */
    @POST(CommonConst.PATH_GOOD)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> thumbUp(@FieldMap Map<String, Object> params);

    /**
     * long commentId;评论id
     * int page;页码
     *
     * @param params
     * @return
     */
    @POST(CommonConst.PATH_COMMENT_REPLY)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> commentReplies(@FieldMap Map<String, Object> params);

    /**
     * String token;令牌
     * long commentId;评论id
     * String content;内容
     * int replyType;回复类型：评论Comment(0), 回复Reply(1)
     * long replyObjId;回复对象id
     *
     * @param params
     * @return
     */
    @POST(CommonConst.PATH_COMMENT_ADD_REPLY)
    @FormUrlEncoded
    Observable<ApiResultBean<CommentReply>> replyComment(@FieldMap Map<String, Object> params);

    /**
     * int page;页
     * String token;令牌
     *
     * @param params
     * @return
     */
    @POST(CommonConst.PATH_COMMENT_MY_PUBLISHED)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> myPublish(@FieldMap Map<String, Object> params);

    /**
     * String token;令牌
     * int page;页
     *
     * @param params
     * @return
     */
    @POST(CommonConst.PATH_COMMENT_MY_RELATED)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> myRelated(@FieldMap Map<String, Object> params);
}
