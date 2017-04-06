package com.culturebud.contract;

import android.graphics.Bitmap;
import android.net.Uri;

import com.culturebud.bean.ApiResultBean;
import com.culturebud.bean.BookCircleDynamic;
import com.culturebud.bean.DynamicReply;
import com.culturebud.bean.User;
import com.culturebud.model.CommunityBaseModel;
import com.culturebud.net.ApiBookHomeInterface;
import com.google.gson.JsonObject;

import java.util.List;

import rx.Observable;

/**
 * Created by XieWei on 2016/10/28.
 */

public interface BookCircleContract {
    abstract class Model extends CommunityBaseModel {
        public abstract Observable<ApiResultBean<JsonObject>> getDynamics(int page, String token, long userId);

        public abstract Observable<DynamicReply> replyDynamic(String token, long dynamicId, String content, int
                replyType, long replyObjId);

        public abstract Observable<Boolean> updateLocelUser(User user);
    }

    interface View extends BaseView {
        void onDynamics(List<BookCircleDynamic> dynamics);

        void onBgImg(Bitmap bitmap);

        void onUploadBgImg(String url);

        void onThumbUp(long dynamicId, boolean result);

        void onDynamicReply(DynamicReply dynamicReply);

        void onDeleteResult(long dynamicId, int deleteType, long deleteObjId, boolean res);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void loadDynamics(int page);

        public abstract void downloadBgImg();

        public abstract void uploadBgImg(Uri imgUri, boolean isJPG);

        public abstract void thumbUp(long dynamicId);

        public abstract void replyDynamic(long dynamicId, String content, int replyType, long replyObjId);

        public abstract void deleteDynamicOrReply(long dynamicId, int deleteType, long deleteObjId);

    }

}
