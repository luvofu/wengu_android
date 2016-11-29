package com.culturebud.model;

import android.text.TextUtils;

import com.culturebud.ApiErrorCode;
import com.culturebud.bean.ApiResultBean;
import com.culturebud.bean.User;
import com.culturebud.contract.UserInfoContract;
import com.culturebud.db.dao.UserDAO;
import com.culturebud.net.ApiMeInterface;
import com.culturebud.util.ApiException;

import java.sql.SQLException;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by XieWei on 2016/11/16.
 */

public class UserInfoModel extends UserInfoContract.Model {
    private UserDAO userDAO;

    private void initUserDAO() throws SQLException {
        if (userDAO == null) {
            userDAO = new UserDAO();
        }
    }

    @Override
    public Observable<Boolean> updateLocelUser(User user) {
        return Observable.create(subscriber -> {
            try {
                initUserDAO();
                subscriber.onNext(userDAO.updateUser(user));
                subscriber.onCompleted();
            } catch (SQLException e) {
                e.printStackTrace();
                subscriber.onError(e);
            }
        });
    }

    @Override
    public Observable<User> alterNick(String token, String nick) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }
            params.put("nickname", nick);
            initRetrofit().create(ApiMeInterface.class).editProfile(params)
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
                                subscriber.onNext(bean.getData());
                            } else {
                                subscriber.onError(new ApiException(code, bean.getMsg()));
                            }
                        }
                    });
        });
    }

    @Override
    public Observable<User> alterEmail(String token, String email) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }
            params.put("mailbox", email);
            initRetrofit().create(ApiMeInterface.class).editProfile(params)
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
                                subscriber.onNext(bean.getData());
                            } else {
                                subscriber.onError(new ApiException(code, bean.getMsg()));
                            }
                        }
                    });
        });
    }

    @Override
    public Observable<User> alterAutograph(String token, String autograph) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }
            params.put("autograph", autograph);
            initRetrofit().create(ApiMeInterface.class).editProfile(params)
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
                                subscriber.onNext(bean.getData());
                            } else {
                                subscriber.onError(new ApiException(code, bean.getMsg()));
                            }
                        }
                    });
        });
    }

}
