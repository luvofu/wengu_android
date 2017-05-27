package com.culturebud.contract;

import com.culturebud.bean.UserMessage;

import java.util.List;

import rx.Observable;

/**
 * Created by XieWei on 2016/11/16.
 */

public interface MyMsgsContract {
    abstract class Model extends BaseModel {
        public abstract Observable<List<UserMessage>> getMsgs(String token, int msgGetType, String messageIds, String messageTypes, int page);

        public abstract Observable<Boolean> agreeInvite(String token, long messageId);

        public abstract Observable<Boolean> deleteUserMessage(String token, long messageId);
    }

    interface View extends BaseView {
        void onMsgs(List<UserMessage> msgs);

        void onAgreeInvite(UserMessage userMessage);

        void onDeleteUserMessage(UserMessage userMessage, boolean success);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void getMsgs(int msgGetType, String messageIds, String messageTypes, int page);

        public abstract void agreeInvite(UserMessage userMessage);

        public abstract void deleteUserMessage(UserMessage userMessage);
    }
}
