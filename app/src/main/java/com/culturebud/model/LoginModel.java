package com.culturebud.model;

import android.text.TextUtils;

import com.culturebud.ApiErrorCode;
import com.culturebud.BaseApp;
import com.culturebud.bean.ApiResultBean;
import com.culturebud.bean.User;
import com.culturebud.contract.LoginContract;
import com.culturebud.db.dao.UserDAO;
import com.culturebud.util.ApiException;
import com.google.gson.JsonObject;

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
    public Observable<Boolean> updateLocalUser(User user) {
        return Observable.create(subscriber -> {
            try {
                initDAO();
                subscriber.onNext(userDAO.updateUser(user));
                subscriber.onCompleted();
            } catch (SQLException e) {
                e.printStackTrace();
                subscriber.onError(new Exception("数据库错误"));
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

    @Override
    public Observable<User> thirdLogin(String uid, String nickname, int thirdType) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            params.put("uid", uid);
            params.put("nickname", nickname);
            params.put("thirdType", thirdType);
            getMeInterface().thirdLogin(params)
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
    public Observable<User> thirdBindLogin(String validCode, String regMobile, int thirdType, String uid, String
            nickname, int sex, String autograph, String birthday, String avatar) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(validCode)) {
                params.put("validcode", validCode);
            }
            if (!TextUtils.isEmpty(regMobile)) {
                params.put("regMobile", regMobile);
            }
            params.put("thirdType", thirdType);
            if (!TextUtils.isEmpty(uid)) {
                params.put("uid", uid);
            }
            if (!TextUtils.isEmpty(nickname)) {
                params.put("nickname", nickname);
            }
            params.put("sex", sex);
            if (!TextUtils.isEmpty(autograph)) {
                params.put("autograph", autograph);
            }
            if (!TextUtils.isEmpty(birthday)) {
                params.put("birthday", birthday);
            }
            if (!TextUtils.isEmpty(avatar)) {
                params.put("avatar", avatar);
            }
            getMeInterface().thirdBindLogin(params)
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
    public Observable<User> autoLogin(String token) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }

            getMeInterface().autoLogin(params)
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
    public Observable<Boolean> logoutRemote(String token) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }

            getMeInterface().logout(params)
                    .subscribe(new Subscriber<ApiResultBean<JsonObject>>() {
                        @Override
                        public void onCompleted() {
                            subscriber.onCompleted();
                        }

                        @Override
                        public void onError(Throwable e) {
                            subscriber.onError(e);
                        }

                        @Override
                        public void onNext(ApiResultBean<JsonObject> bean) {
                            int code = bean.getCode();
                            if (code == ApiErrorCode.CODE_SUCCESS) {
                                subscriber.onNext(true);
                            } else {
                                subscriber.onError(new ApiException(code, bean.getMsg()));
                            }
                        }
                    });
        });
    }
}
