package com.culturebud.contract;

import com.culturebud.bean.Comment;
import com.culturebud.bean.MyRelatedComment;
import com.culturebud.model.CommunityBaseModel;

import java.util.List;

import rx.Observable;

/**
 * Created by XieWei on 2016/11/19.
 */

public interface MyCommentContract {

    abstract class Model extends CommunityBaseModel {
        public abstract Observable<List<Comment>> myPublished(String token, int page);

        public abstract Observable<List<MyRelatedComment>> myRelated(String token, int page);

    }

    interface View extends BaseView {
        void onComments(List<Comment> comments);

        void onMyRelatedComments(List<MyRelatedComment> comments);

        void onThumbUp(boolean res, long goodObjId);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void getMyPublished(int page);

        public abstract void getMyRelated(int page);

        public abstract void thumbUp(long goodObjId);
    }

}
