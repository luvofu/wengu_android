package com.culturebud.model;

import android.text.TextUtils;

import com.culturebud.ApiErrorCode;
import com.culturebud.CommonConst;
import com.culturebud.bean.ApiResultBean;
import com.culturebud.bean.User;
import com.culturebud.contract.AccountBindingContract;
import com.culturebud.db.dao.UserDAO;
import com.culturebud.net.ApiMeInterface;
import com.culturebud.util.ApiException;

import java.sql.SQLException;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by XieWei on 2017/5/1.
 */

public class AccountBindingModel extends AccountBindingContract.Model {
    private UserDAO userDAO;

    private void initUserDAO() throws SQLException {
        if (userDAO == null) {
            synchronized (AccountBindingModel.class) {
                if (userDAO == null) {
                    userDAO = new UserDAO();
                }
            }
        }
    }
    @Override
    public Observable<User> thirdBinding(String token, String uid, String nickname, int thirdType) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }
            if (!TextUtils.isEmpty(uid)) {
                params.put("uid", uid);
            }
            if (!TextUtils.isEmpty(nickname)) {
                params.put("nickname", nickname);
            }
            if (thirdType > CommonConst.ThirdType.TYPE_NONE) {
                params.put("thirdType", thirdType);
            }
            initRetrofit().create(ApiMeInterface.class).bindThird(params)
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
    public Observable<User> thirdUnbinding(String token, String uid, int thirdType) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }
            if (!TextUtils.isEmpty(uid)) {
                params.put("uid", uid);
            }
            if (thirdType > CommonConst.ThirdType.TYPE_NONE) {
                params.put("thirdType", thirdType);
            }
            initRetrofit().create(ApiMeInterface.class).unbindThird(params)
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
    public Observable<Boolean> updateLocalUser(User user) {
        return Observable.create(subscriber -> {
            try {
                initUserDAO();
                subscriber.onNext(userDAO.updateUser(user));
                subscriber.onCompleted();
            } catch (SQLException e) {
                e.printStackTrace();
                subscriber.onError(new Exception("数据库错误"));
            }
        });
    }
}
