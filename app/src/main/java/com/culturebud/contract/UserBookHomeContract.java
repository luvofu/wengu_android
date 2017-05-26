package com.culturebud.contract;

import com.culturebud.bean.BookCircleDynamic;
import com.culturebud.bean.DynamicReply;
import com.culturebud.bean.User;
import com.culturebud.model.BookCircleModel;
import com.google.gson.JsonObject;

import java.util.List;

import rx.Observable;

/**
 * Created by XieWei on 2016/12/30.
 */

public interface UserBookHomeContract {
    abstract class Model extends BookCircleModel {
        public abstract Observable<User> getUserProfile(String token, long userId);

        public abstract Observable<List<BookCircleDynamic>> getDynamics(String token, long userId, int page);

        public abstract Observable<JsonObject> concern(String token, long friendId);
    }

    interface View extends BaseView {
        void onUser(User user);

        void onDynamics(List<BookCircleDynamic> dynamics);

        void onThumbUp(long dynamicId, boolean isGood);

        void onDynamicReply(DynamicReply dynamic);

        void onConcern(long concernNum, long fanNum, int status);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void getUserProfile(long userId);

        public abstract void getDynamics(long userId, int page);

        public abstract void thumbUpDynamic(long dynamicId);

        public abstract void replyDynamic(long dynamicId, String content, int replyType, long replyObjId);

        public abstract void concern(long friendId);
    }
}
