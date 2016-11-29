package com.culturebud.contract;

import rx.Observable;

/**
 * Created by XieWei on 2016/11/3.
 */

public interface FeedbackContract {

    abstract class Model extends BaseModel {
        public abstract Observable<Boolean> feedback(String token, String questions, String contact);
    }

    interface View extends BaseView {
        void onFeedback(String tips);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void feedback(String questions, String contact);
    }
}
