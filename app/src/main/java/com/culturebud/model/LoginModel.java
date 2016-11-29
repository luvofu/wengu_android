package com.culturebud.model;

import com.culturebud.ApiErrorCode;
import com.culturebud.BaseApp;
import com.culturebud.bean.ApiResultBean;
import com.culturebud.bean.User;
import com.culturebud.contract.LoginContract;
import com.culturebud.db.dao.UserDAO;
import com.culturebud.util.ApiException;

import java.sql.SQLException;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by XieWei on 2016/10/27.
 */

public class LoginModel extends LoginContract.Model {

    private UserDAO userDAO;

    public void initDAO() throws SQLException {
        if (userDAO == null) {
            userDAO = new UserDAO();
        }
    }

    @Override
    public Observable<User> login(String platform, String deviceToken,
                                  String userName, String password) {
        return Observable.create(subcriber -> {
            Map<String, Object> params = getCommonParams();
            params.put("userName", userName);
            params.put("password", password);
            getMeInterface().login(params).subscribe(new Subscriber<ApiResultBean<User>>() {
                @Override
                public void onCompleted() {
                    subcriber.onCompleted();
                }

                @Override
                public void onError(Throwable e) {
                    subcriber.onError(e);
                }

                @Override
                public void onNext(ApiResultBean<User> bean) {
                    int code = bean.getCode();
                    if (code == ApiErrorCode.CODE_SUCCESS) {
                        subcriber.onNext(bean.getData());
                    } else {
                        subcriber.onError(new ApiException(code, bean.getMsg()));
                    }
                }
            });
        });
    }

    @Override
    public Observable<Boolean> saveUser(User user) {
        return Observable.create(subscriber -> {
            try {
                initDAO();
                boolean res = userDAO.addUser(user);
                subscriber.onNext(res);
                subscriber.onCompleted();
            } catch (SQLException e) {
                e.printStackTrace();
                subscriber.onError(e);
            }
        });
    }

    @Override
    public Observable<Boolean> logout(User user) {
        return Observable.create(subscriber -> {
            try {
                initDAO();
                boolean res = userDAO.delUser(user);
                if (res) {
                    BaseApp.getInstance().setUser(null);
                }
                subscriber.onNext(res);
                subscriber.onCompleted();
            } catch (SQLException e) {
                e.printStackTrace();
                subscriber.onError(e);
            }
        });
    }
}
