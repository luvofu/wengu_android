package com.culturebud.contract;

import com.culturebud.bean.User;

import java.util.List;

import rx.Observable;

/**
 * Created by XieWei on 2016/12/5.
 */

public interface MyFriendsContract {
    abstract class Model extends BaseModel {
        public abstract Observable<List<User>> myFriends(String token);
    }

    interface View extends BaseView {
        void onFriends(List<User> friends);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void myFriends();
    }
}
