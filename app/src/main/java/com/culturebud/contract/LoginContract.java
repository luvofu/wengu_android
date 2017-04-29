package com.culturebud.contract;

import com.culturebud.bean.ApiResultBean;
import com.culturebud.bean.User;
import com.culturebud.model.MeModel;
import com.culturebud.net.ApiMeInterface;

import rx.Observable;

/**
 * Created by XieWei on 2016/10/25.
 */

public interface LoginContract {

    interface View extends BaseView {
        void loginSuccess(User user);

        void onLogout(boolean success);

        void onNeedBindPhone();

        void onObtainedCode(boolean success);

        void onCountDown(int count);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void login(String userName, String password);

        public abstract void logout();

        public abstract void loadLocalUser();

        public abstract void thirdLogin(String uid, String nick, int thirdType);

        public abstract void thirdBindLogin(String validCode, String regMobile, int thirdType, String uid, String
                nickname, int sex, String autograph, String birthday, String avatar);

        public abstract void getSecurityCode(String phoneNumber, int thirdType);
    }

    abstract class Model extends MeModel {
        protected ApiMeInterface getMeInterface() {
            return initRetrofit().create(ApiMeInterface.class);
        }

        public abstract Observable<User> login(String platform, String deviceToken,
                                               String userName, String password);

        public abstract Observable<Boolean> saveUser(User user);

        public abstract Observable<Boolean> logout(User user);

        public abstract Observable<User> thirdLogin(String uid, String nickname, int thirdType);

        public abstract Observable<User> thirdBindLogin(String validCode, String regMobile, int thirdType, String uid,
                                                        String nickname, int sex, String autograph, String birthday,
                                                        String avatar);

    }
}
