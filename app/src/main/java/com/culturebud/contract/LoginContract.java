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

    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void login(String userName, String password);

        public abstract void logout();

        public abstract void loadLocalUser();
    }

    abstract class Model extends MeModel {
        protected ApiMeInterface getMeInterface() {
            return initRetrofit().create(ApiMeInterface.class);
        }

        public abstract Observable<User> login(String platform, String deviceToken,
                                               String userName, String password);

        public abstract Observable<Boolean> saveUser(User user);

        public abstract Observable<Boolean> logout(User user);

    }
}
