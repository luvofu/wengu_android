package com.culturebud.model;

import android.text.TextUtils;

import com.culturebud.ApiErrorCode;
import com.culturebud.bean.ApiResultBean;
import com.culturebud.bean.HistoryTag;
import com.culturebud.contract.GeneralAddTagsContract;
import com.culturebud.db.dao.HistoryTagDAO;
import com.culturebud.net.ApiBookInterface;
import com.culturebud.net.ApiBookSheetInterface;
import com.culturebud.util.ApiException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by XieWei on 2017/4/8.
 */

public class GeneralAddTagsModel extends GeneralAddTagsContract.Model {
    private HistoryTagDAO historyTagDAO;

    private void initHistoryTagDao() {
        if (historyTagDAO == null) {
            synchronized (GeneralAddTagsModel.class) {
                if (historyTagDAO == null) {
                    try {
                        historyTagDAO = new HistoryTagDAO();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public Observable<List<String>> getBookTags(String token) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }
            initRetrofit().create(ApiBookInterface.class).bookTags(params)
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
                                if (bean.getData().has("bookTagList")) {
                                    List<String> tags = new Gson().fromJson(bean.getData().getAsJsonArray
                                            ("bookTagList"), new
                                            TypeToken<List<String>>() {
                                            }.getType());
                                    subscriber.onNext(tags);
                                } else {
                                    subscriber.onNext(null);
                                }
                            } else {
                                subscriber.onError(new ApiException(code, bean.getMsg()));
                            }
                        }
                    });
        });
    }

    @Override
    public Observable<List<String>> getBookSheetTags(String token) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }
            initRetrofit().create(ApiBookSheetInterface.class).getTags(params)
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
                                if (bean.getData().has("bookSheetTagList")) {
                                    List<String> tags = new Gson().fromJson(bean.getData().getAsJsonArray
                                            ("bookSheetTagList"), new
                                            TypeToken<List<String>>() {
                                            }.getType());
                                    subscriber.onNext(tags);
                                } else {
                                    subscriber.onNext(null);
                                }
                            } else {
                                subscriber.onError(new ApiException(code, bean.getMsg()));
                            }
                        }
                    });
        });
    }

    @Override
    public Observable<List<HistoryTag>> getLocalHistory(byte type, long userId) {
        return Observable.create(subscriber -> {
            initHistoryTagDao();
            try {
                List<HistoryTag> tags = historyTagDAO.findByTypeAndUserId(type, userId);
                subscriber.onNext(tags);
                subscriber.onCompleted();
            } catch (SQLException e) {
                e.printStackTrace();
                subscriber.onError(e);
            }
        });
    }

    @Override
    public Observable<Boolean> saveHistoryToLocal(HistoryTag tag) {
        return Observable.create(subscriber -> {
           initHistoryTagDao();
            try {
                subscriber.onNext(historyTagDAO.addTag(tag));
                subscriber.onCompleted();
            } catch (SQLException e) {
                e.printStackTrace();
                subscriber.onError(e);
            }
        });
    }
}
