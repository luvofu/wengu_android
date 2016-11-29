package com.culturebud.contract;

import com.culturebud.bean.User;

import rx.Observable;

/**
 * Created by XieWei on 2016/11/18.
 */

public interface ChangePasswordContract {
    abstract class Model extends BaseModel {
        /**
         * String token;令牌
         * String password;原密码
         * String newPassword;新密码
         *
         * @param token
         * @param password
         * @param newPassword
         * @return
         */
        public abstract Observable<User> changePwd(String token, String password, String newPassword);
    }

    interface View extends BaseView {
        void onUser(User user);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void changePwd(String currPwd, String newPwd, String newPwdConfirm);
    }
}
