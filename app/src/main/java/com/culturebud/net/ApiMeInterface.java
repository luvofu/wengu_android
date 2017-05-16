package com.culturebud.net;

import com.culturebud.bean.ApiResultBean;
import com.culturebud.bean.User;
import com.culturebud.CommonConst;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by XieWei on 2016/10/25.
 */

public interface ApiMeInterface {

    /**
     * String platform;平台标识
     * String deviceToken;设备标识
     * String userName;用户名、手机号
     * String password;密码
     */
    @POST(CommonConst.PATH_LOGIN)
    @FormUrlEncoded
    Observable<ApiResultBean<User>> login(@FieldMap Map<String, Object> params);

    @POST(CommonConst.PATH_AUTO_LOGIN)
    @FormUrlEncoded
    Observable<ApiResultBean<User>> autoLogin(@FieldMap Map<String, Object> params);

    @POST(CommonConst.PATH_THIRD_LOGIN)
    @FormUrlEncoded
    Observable<ApiResultBean<User>> thirdLogin(@FieldMap Map<String, Object> params);

    @POST(CommonConst.PATH_THIRD_BIND_LOGIN)
    @FormUrlEncoded
    Observable<ApiResultBean<User>> thirdBindLogin(@FieldMap Map<String, Object> params);

    /**
     * String deviceToken;设备标识
     * String regMobile;注册手机号
     * String validcode;验证码
     * String password;密码
     *
     * @param params
     * @return
     */
    @POST(CommonConst.PATH_REGIST)
    @FormUrlEncoded
    Observable<ApiResultBean<User>> regist(@FieldMap Map<String, Object> params);

    /**
     * String regMobile;手机号
     * String password;原密码
     * String validcode;验证码
     * String platform;平台标识
     * String deviceToken;设备标识
     *
     * @param params
     * @return
     */
    @POST(CommonConst.PATH_RETRIEVE_PWD)
    @FormUrlEncoded
    Observable<ApiResultBean<User>> forgotPassword(@FieldMap Map<String, Object> params);

    /**
     * String token;令牌（查验手机号、绑定新手机号）
     * String deviceToken;设备标识
     * String regMobile;请求验证码手机号
     * int useCondition;使用场景：
     * 注册Signup(0),忘记密码ForgetPsw(1),
     * 查验手机号CheckMobile(2),绑定手机号BindMobile(3);
     *
     * @param params
     * @return
     */
    @POST(CommonConst.PATH_SECURITY_CODE)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> getSucrityCode(@FieldMap Map<String, Object> params);

    /**
     * String token;令牌
     * String password;原密码
     * String newPassword;新密码
     *
     * @param params
     * @return
     */
    @POST(CommonConst.PATH_CHANGE_PASSWORD)
    @FormUrlEncoded
    Observable<ApiResultBean<User>> changePwd(@FieldMap Map<String, Object> params);

    /**
     * String token;令牌
     * String mailbox;邮箱
     * String nickname;昵称
     * int sex;性别
     * String avatar;头像
     * String background;背景图
     * String autograph;个性签名
     * String country;国家
     * String province;省
     * String city;市
     * String birthday;生日
     * String tag;标签
     *
     * @param params
     * @return
     */
    @POST(CommonConst.PATH_USER_EDIT_PROFILE)
    @FormUrlEncoded
    Observable<ApiResultBean<User>> editProfile(@FieldMap Map<String, Object> params);

    /**
     * 编辑文芽号
     * String userName; 文芽号
     * @param params
     * @return
     */
    @POST(CommonConst.PATH_USER_EDIT_USERNAME)
    @FormUrlEncoded
    Observable<ApiResultBean<User>> editUserName(@FieldMap Map<String, Object> params);

    /**
     * String token;令牌
     * String content;反馈内容
     * String connection;联系方式
     *
     * @param params
     * @return
     */
    @POST(CommonConst.PATH_USER_FEEDBACK)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> feedback(@FieldMap Map<String, Object> params);

    /**
     * String token;令牌
     * int page;页
     *
     * @param params
     * @return
     */
    @POST(CommonConst.PATH_MSG_INVITE)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> inviteMsgs(@FieldMap Map<String, Object> params);

    /**
     * String token;令牌
     * long messageId;消息id
     *
     * @param params
     * @return
     */
    @POST(CommonConst.PATH_MSG_DELETE)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> deleteMsg(@FieldMap Map<String, Object> params);

    @POST(CommonConst.PATH_MY_FRIENDS)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonArray>> myFriends(@FieldMap Map<String, Object> params);

    /**
     * int page;页
     * String keyword;关键字
     *
     * @param params
     * @return
     */
    @POST(CommonConst.PATH_USER_SEARCH)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> searchUser(@FieldMap Map<String, Object> params);

    /**
     * String token;令牌
     * long userId;用户id（返回信息所属用户id）
     *
     * @param params
     * @return
     */
    @POST(CommonConst.PATH_USER_PROFILE)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> getUserProfile(@FieldMap Map<String, Object> params);

    @POST(CommonConst.PATH_USER_MSG_INVITE_FRIEND)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> inviteFriend(@FieldMap Map<String, Object> params);

    @POST(CommonConst.PATH_USER_MSG_AGREE_INVITE)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> agreeInvite(@FieldMap Map<String, Object> params);


    /**
     * 验证绑定手机（手机号）
     * validcode 验证码
     * regMobile 手机号
     * @param params
     * @return
     */
    @POST(CommonConst.PATH_CHECK_MOBILE)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> checkMobile(@FieldMap Map<String, Object> params);

    /**
     * 更换绑定手机（手机号）
     * validcode 验证码
     * regMobile 手机号
     * @param params
     * @return
     */
    @POST(CommonConst.PATH_CHANGE_MOBILE)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> changeMobile(@FieldMap Map<String, Object> params);

    @POST(CommonConst.PATH_THIRD_BIND)
    @FormUrlEncoded
    Observable<ApiResultBean<User>> bindThird(@FieldMap Map<String, Object> params);

    @POST(CommonConst.PATH_THIRD_UNBIND)
    @FormUrlEncoded
    Observable<ApiResultBean<User>> unbindThird(@FieldMap Map<String, Object> params);

    @POST(CommonConst.PATH_LOGIN_OUT)
    @FormUrlEncoded
    Observable<ApiResultBean<JsonObject>> logout(@FieldMap Map<String, Object> params);

}
