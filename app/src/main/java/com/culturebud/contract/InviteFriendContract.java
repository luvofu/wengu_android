package com.culturebud.contract;

import rx.Observable;

/**
 * Created by XieWei on 2016/12/28.
 */

public interface InviteFriendContract {
    abstract class Model extends BaseModel {
        public abstract Observable<Boolean> invite(String token, long acceptUserId, String content);
    }

    interface View extends BaseView {
        void onInviteSuccess(long userId);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void invite(long friendId, String content);
    }
}
