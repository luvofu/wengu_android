package com.culturebud.contract;

import com.culturebud.bean.User;

import rx.Observable;

/**
 * Created by XieWei on 2016/12/27.
 */

public interface FriendDetailContract {
    abstract class Model extends BaseModel {
        public abstract Observable<User> getUserProfile(String token, long userId);
    }

    interface View extends BaseView {
        void onFriend(User user);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void getFriendDetail(long userId);
    }
}
