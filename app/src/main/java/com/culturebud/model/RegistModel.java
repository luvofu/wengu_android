package com.culturebud.model;

import com.culturebud.ApiErrorCode;
import com.culturebud.bean.ApiResultBean;
import com.culturebud.bean.User;
import com.culturebud.contract.RegistContract;
import com.culturebud.db.dao.UserDAO;
import com.culturebud.net.ApiMeInterface;
import com.culturebud.util.ApiException;

import java.sql.SQLException;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by XieWei on 2016/11/17.
 */

public class RegistModel extends RegistContract.Model {

    private UserDAO userDAO;

    private void initDao() throws SQLException {
        if (userDAO == null) {
            userDAO = new UserDAO();
        }
    }

    @Override
    public Observable<User> regist(String phoneNumber, String validcode, String password) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            params.put("regMobile", phoneNumber);
            params.put("validcode", validcode);
            params.put("password", password);
            initRetrofit().create(ApiMeInterface.class).regist(params)
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
                            initDao();
                            userDAO.addUser(bean.getData());
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
