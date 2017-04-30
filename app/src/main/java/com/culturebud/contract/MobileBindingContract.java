package com.culturebud.contract;

import rx.Observable;

public interface MobileBindingContract {
    interface View extends BaseView {

        void onInvalidate(String message);

        void onObtainedCode(boolean result);

        void onBindingMobile(boolean result);

        void onCheckMobile(boolean result);


    }

    abstract class Model extends BaseModel {

        public abstract Observable<Boolean> checkMobile(String token, String mobile, String validcode);

        public abstract Observable<Boolean> changeMobile(String token, String mobile, String validcode);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void  getValidcode(String token,String mobile,int type);

        public abstract void checkMobile(String token, String mobile, String validcode);

        public abstract void changeMobile(String token, String mobile, String validcode);
    }
}