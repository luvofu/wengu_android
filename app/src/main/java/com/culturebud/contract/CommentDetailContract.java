package com.culturebud.contract;

import com.culturebud.bean.CommentReply;
import com.culturebud.model.CommunityBaseModel;

import java.util.List;

import rx.Observable;

/**
 * Created by XieWei on 2016/11/9.
 */

public interface CommentDetailContract {
    abstract class Model extends CommunityBaseModel {
        public abstract Observable<List<CommentReply>> getCommentReplies(String token, long commentId, int page);

        /**
         * @param token      令牌
         * @param commentId  评论id
         * @param content    内容
         * @param replyType  回复类型：评论Comment(0), 回复Reply(1)
         * @param replyObjId 回复对象id
         * @return
         */
        public abstract Observable<CommentReply> addCommentReply(String token, long commentId, String content, int replyType, long replyObjId);
    }

    interface View extends BaseView {
        void onReplies(List<CommentReply> replies);

        void onReply(CommentReply reply);

        void onThumbUp(boolean res);

        void onDelReply(boolean opera, int deleteType, long deleteObjId);
    }

    abstract class Presenter extends BasePresenter<View, Model> {

        public abstract void thumbUp(long goodObjId);

        public abstract void getReplies(long commentId, int page);

        public abstract void addReply(long commentId, int replyType, long replyObjId, String content);

        public abstract void delReply(int deleteType, long deleteObjId);
    }
}
