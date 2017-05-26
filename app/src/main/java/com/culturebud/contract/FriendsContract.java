package com.culturebud.contract;

import com.culturebud.bean.Friend;
import com.google.gson.JsonObject;

import java.util.List;

import rx.Observable;

/**
 * Created by XieWei on 2016/12/5.
 */

public interface FriendsContract {
    abstract class Model extends BaseModel {

        public abstract Observable<List<Friend>> concers(String token, long userId);

        public abstract Observable<List<Friend>> fans(String token, long userId);

        public abstract Observable<JsonObject> concern(String token, long friendId);
    }

    interface View extends BaseView {
        void onFriends(List<Friend> friends);

        void onConcern(Friend friend, long concernNum, long fanNum, int status);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void friends(boolean isConcern, long userId);

        public abstract void concern(Friend friend);
    }
}
