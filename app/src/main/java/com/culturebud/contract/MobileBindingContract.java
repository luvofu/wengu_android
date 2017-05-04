package com.culturebud.contract;

import com.culturebud.bean.User;
import com.culturebud.model.AccountBindingModel;

import rx.Observable;

public interface MobileBindingContract {
    interface View extends BaseView {

        void onInvalidate(String message);

        void onObtainedCode(boolean result);

        void onBindingMobile(boolean result);

        void onCheckMobile(boolean result);

        void onCountDown(int count);

    }

    abstract class Model extends AccountBindingModel {

        public abstract Observable<Boolean> checkMobile(String token, String mobile, String validcode);

        public abstract Observable<Boolean> changeMobile(String token, String mobile, String validcode);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void getValidCode(String mobile, int type);

        public abstract void checkMobile(String mobile, String validcode);

        public abstract void changeMobile(String mobile, String validcode);

        public abstract void updateLocalUser(User user);
    }
}