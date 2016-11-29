package com.culturebud.contract;

import com.culturebud.bean.User;

import rx.Observable;

/**
 * Created by XieWei on 2016/11/18.
 */

public interface RetrievePasswordContract {

    abstract class Model extends BaseModel {
        /**
         * String regMobile;手机号
         * String password;原密码
         * String validcode;验证码
         * String platform;平台标识
         * String deviceToken;设备标识
         *
         * @return
         */
        public abstract Observable<User> forgotPassword(String regMobile, String password, String validcode);
    }

    interface View extends BaseView {
        void onRetrieve(User user);

        void onObtainedCode(boolean res);

        void onCountDown(int scend);

    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void retrievePassword(String phoneNumber, String sucrityCode, String pwd, String pwdConfirm);

        public abstract void getSucrityCode(String phoneNumber);
    }
}
