package com.culturebud.contract;

import com.culturebud.bean.UserMessage;

import java.util.List;

import rx.Observable;

/**
 * Created by XieWei on 2016/11/16.
 */

public interface MyMsgsContract {
    abstract class Model extends BaseModel {
        public abstract Observable<List<UserMessage>> getInviteMsgs(String token, int page);

        public abstract Observable<Boolean> agreeInvite(String token, long messageId);
    }

    interface View extends BaseView {
        void onInviteMsgs(List<UserMessage> msgs);

        void onAgreeInvite(long messageId, boolean success);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void getInviteMsgs(int page);

        public abstract void agreeInvite(long messageId);
    }
}
