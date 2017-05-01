package com.culturebud.contract;

import com.culturebud.bean.User;
import com.culturebud.model.MeModel;

import rx.Observable;

/**
 * Created by XieWei on 2017/5/1.
 */

public interface AccountBindingContract {

    abstract class Model extends MeModel {
        public abstract Observable<User> thirdBinding(String token, String uid, String nickname, int thirdType);

        public abstract Observable<User> thirdUnbinding(String token, String uid, int thirdType);

        public abstract Observable<Boolean> updateLocalUser(User user);
    }

    interface View extends BaseView {
        void onBindingRes(boolean success, int thirdType);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        /**
         * parameters : uid(微信则用union)、nickname、thirdType
         * return : 用户实体信息
         */
        public abstract void thirdBinding(String uid, String nickname, int thirdType);

        /**
         * parameters : uid、thirdType
         * return : 用户实体信息
         */
        public abstract void thirdUnbinding(String uid, int thirdType);

        public abstract void updateLocalUser(User user);
    }
}
