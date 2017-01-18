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
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void dynamicDetail(long dynamicId);

        public abstract void processReplies(List<DynamicReply> src);

        public abstract void thumbUP(long dynamicId);
    }
}
