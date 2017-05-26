package com.culturebud.contract;

import com.culturebud.bean.User;

import rx.Observable;

/**
 * Created by XieWei on 2016/10/25.
 */

public interface MeContract {

    interface View extends BaseView {
        void showLoginPage();

        void showLoginOut();

        void showUser(User user);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void login();

        public abstract void loadLastUser();

        public abstract void processLoginResult(User user);
    }

    abstract class Model extends BaseModel {

        public abstract Observable<User> loadLastUser();

        public abstract Observable<Boolean> saveUser(User user);
    }
}
