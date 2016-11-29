package com.culturebud.contract;

import com.culturebud.bean.User;

import rx.Observable;

/**
 * Created by XieWei on 2016/11/17.
 */

public interface RegistContract {
    abstract class Model extends BaseModel {

        /**
         * String deviceToken;设备标识
         * String regMobile;注册手机号
         * String validcode;验证码
         * String password;密码
         *
         * @return
         */
        public abstract Observable<User> regist(String phoneNumber, String validcode, String password);
    }

    interface View extends BaseView {
        void onRegist(User user);

        void onInvalidate(String msg);

        void onObtainedCode(boolean hasSend);

        void onCountDown(int second);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void regist(String phoneNumber, String validcode, String pwd, String pwdConfirm);

        public abstract void getSucrityCode(String phoneNumber);
    }
}
