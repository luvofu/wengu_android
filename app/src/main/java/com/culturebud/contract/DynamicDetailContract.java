package com.culturebud.contract;

import com.culturebud.bean.BookCircleDynamic;
import com.culturebud.bean.DynamicReply;
import com.culturebud.model.BookCircleModel;

import java.util.List;

import rx.Observable;

/**
 * Created by XieWei on 2017/1/4.
 */

public interface DynamicDetailContract {
    abstract class Model extends BookCircleModel {
        public abstract Observable<BookCircleDynamic> dynamicDetail(String token, long dynamicId);
    }

    interface View extends BaseView {
        void onDynamic(BookCircleDynamic dynamic);

        void onReplies(List<DynamicReply> replies);

        void onThumbUp(long dynamicId, boolean result);

        void onDeleteReply(long dynamicId, long replyId, boolean result);

        void onDeleteReplyReply(long dynamicId, long replyId, long deleteReplyId, boolean result);

        void onDeleteDynamic(long dynamicId, boolean result);

        void onReply(long dynamicId, long replyId, DynamicReply dynamicReply);

        void onReplyDynamic(long dynamicId, DynamicReply dynamicReply);

    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void dynamicDetail(long dynamicId);

        public abstract void processReplies(List<DynamicReply> src);

        public abstract void thumbUP(long dynamicId);

        public abstract void deleteDynamicOrReply(int deleteType, long dynamicId, long replyId);

        public abstract void deleteReplyReply(long dynamicId, long replyId, long deleteReplyId);

        public abstract void reply(int replyType, long dynamicId, long replyId, String content);
    }
}
