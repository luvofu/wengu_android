package com.culturebud.contract;

import com.culturebud.bean.ApiResultBean;
import com.culturebud.bean.Comment;
import com.culturebud.model.CommunityBaseModel;
import com.google.gson.JsonObject;

import java.util.List;

import rx.Observable;

/**
 * Created by XieWei on 2016/10/27.
 */

public interface CommunityContract {
    abstract class Model extends CommunityBaseModel {
        public abstract Observable<ApiResultBean<JsonObject>> getComments(int page, String token);
    }

    interface View extends BaseView {
        void onShowComments(List<Comment> comments);

        void onThumbUp(long commentId, boolean isGood);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void loadComments(int page);

        public abstract void thumbUp(int goodType, long goodObjId);
    }
}
