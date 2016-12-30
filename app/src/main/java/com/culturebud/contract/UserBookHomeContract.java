package com.culturebud.contract;

import com.culturebud.bean.BookCircleDynamic;
import com.culturebud.bean.DynamicReply;
import com.culturebud.bean.User;

import java.util.List;

import rx.Observable;

/**
 * Created by XieWei on 2016/12/30.
 */

public interface UserBookHomeContract {
    abstract class Model extends BaseModel {
        public abstract Observable<User> getUserProfile(String token, long userId);

        public abstract Observable<List<BookCircleDynamic>> getDynamics(String token, long userId, int page);
     }

    interface View extends BaseView {
        void onUser(User user);

        void onDynamics(List<BookCircleDynamic> dynamics);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void getUserProfile(long userId);

        public abstract void getDynamics(long userId, int page);
    }
}
