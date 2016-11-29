package com.culturebud.model;

import com.culturebud.ApiErrorCode;
import com.culturebud.bean.ApiResultBean;
import com.culturebud.bean.User;
import com.culturebud.contract.RetrievePasswordContract;
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

public class RetrievePasswordModel extends RetrievePasswordContract.Model {
    private UserDAO userDAO;

    private void initDao() throws SQLException {
        if (userDAO == null) {
            userDAO = new UserDAO();
        }
    }

    @Override
    public Observable<User> forgotPassword(String regMobile, String password, String validcode) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            params.put("regMobile", regMobile);
            params.put("password", password);
            params.put("validcode", validcode);
            initRetrofit().create(ApiMeInterface.class).forgotPassword(params)
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
