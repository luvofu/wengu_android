package com.culturebud.model;

import android.text.TextUtils;

import com.culturebud.ApiErrorCode;
import com.culturebud.bean.ApiResultBean;
import com.culturebud.bean.User;
import com.culturebud.contract.ChangePasswordContract;
import com.culturebud.db.dao.UserDAO;
import com.culturebud.net.ApiMeInterface;
import com.culturebud.util.ApiException;

import java.sql.SQLException;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by XieWei on 2016/11/18.
 */

public class ChangePasswordModel extends ChangePasswordContract.Model {
    private UserDAO userDAO;

    private void initDAO() throws SQLException {
        if (userDAO == null) {
            userDAO = new UserDAO();
        }
    }

    @Override
    public Observable<User> changePwd(String token, String password, String newPassword) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }
            if (!TextUtils.isEmpty(password)) {
                params.put("password", password);
            }
            params.put("newPassword", newPassword);
            initRetrofit().create(ApiMeInterface.class).changePwd(params)
            .subscribe(new Subscriber<ApiResultBean<User>>() {
                @Override
                public void onCompleted() {
                    subscriber.onCompleted();
                }

                @Override
                public void onError(Throwable e) {
                    subscriber.onError(e);
                }

                @Override
                public void onNext(ApiResultBean<User> bean) {
                    int code = bean.getCode();
                    if (code == ApiErrorCode.CODE_SUCCESS) {
                        try {
                            initDAO();
                            boolean res = userDAO.updateUser(bean.getData());
                        } catch (SQLException e) {
                            e.printStackTrace();
                            subscriber.onError(e);
                        }
                        subscriber.onNext(bean.getData());
                    } else {
                        subscriber.onError(new ApiException(code, bean.getMsg()));
                    }
                }
            });
        });
    }
}
