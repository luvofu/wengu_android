package com.culturebud.net;

import com.culturebud.CommonConst;
import com.culturebud.bean.ApiResultBean;
import com.google.gson.JsonObject;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by XieWei on 2016/11/3.
 */

public interface ApiFileInterface {

    @POST
    Observable<ResponseBody> downloadFile(@Url String fileUrl);

    /**
     * String token;令牌
     * int imageType;图片类型：
     * 用户头像UserAvatar(0), 用户背景UserBackground(1),
     * 书单封面BookSheetCover(2),笔记内容NotebookContent(3),
     * 动态内容DynamicContent(4);
     * long imageObjId;图片所属对象
     * File imageFile;图片文件
     *
     * @return
     */
    @POST(CommonConst.PATH_EDIT_IMG)
    @Multipart
    Observable<ApiResultBean<JsonObject>> uploadImage(@PartMap Map<String, RequestBody> params, @Part MultipartBody.Part file);
}
