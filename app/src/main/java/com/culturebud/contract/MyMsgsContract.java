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
    }

    interface View extends BaseView {
        void onInviteMsgs(List<UserMessage> msgs);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void getInviteMsgs(int page);
    }
}
