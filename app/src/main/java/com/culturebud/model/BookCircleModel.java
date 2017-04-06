package com.culturebud.model;

import android.text.TextUtils;
import android.util.Log;

import com.culturebud.ApiErrorCode;
import com.culturebud.CommonConst;
import com.culturebud.bean.ApiResultBean;
import com.culturebud.bean.DynamicReply;
import com.culturebud.bean.User;
import com.culturebud.contract.BookCircleContract;
import com.culturebud.db.dao.UserDAO;
import com.culturebud.net.ApiBookHomeInterface;
import com.culturebud.util.ApiException;
import com.google.gson.JsonObject;

import java.sql.SQLException;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by XieWei on 2016/10/29.
 */

public class BookCircleModel extends BookCircleContract.Model {
    private static final String TAG = BookCircleModel.class.getSimpleName();

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
    public Observable<ApiResultBean<JsonObject>> getDynamics(
            int page, String token, long userId) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }
            params.put("page", page);
            params.put("userId", userId);
            initRetrofit().create(ApiBookHomeInterface.class).dynamic(params).subscribe(new Subscriber<ApiResultBean<JsonObject>>() {
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
                    subscriber.onNext(bean);
                }
            });
        });
    }

    @Override
    public Observable<DynamicReply> replyDynamic(String token, long dynamicId,
                                                                String content, int replyType,
                                                                long replyObjId) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }
            params.put("dynamicId", dynamicId);
            params.put("content", content);
            params.put("replyType", replyType);
            if (replyType == CommonConst.DynamicReplyType.TYPE_REPLY) {
                params.put("replyObjId", replyObjId);
            }
            initRetrofit().create(ApiBookHomeInterface.class).replyDynamic(params)
            .subscribe(new Subscriber<ApiResultBean<DynamicReply>>() {
                @Override
                public void onCompleted() {
                    subscriber.onCompleted();
                }

                @Override
                public void onError(Throwable e) {
                    subscriber.onError(e);
                }

                @Override
                public void onNext(ApiResultBean<DynamicReply> bean) {
                    Log.d(TAG, "" + bean);
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
