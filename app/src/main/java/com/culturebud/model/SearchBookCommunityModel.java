package com.culturebud.model;

import com.culturebud.bean.ApiResultBean;
import com.culturebud.bean.BookCommunity;
import com.culturebud.bean.SearchKeyword;
import com.culturebud.contract.SearchBookCommunityContract;
import com.culturebud.db.dao.BookCommunityDAO;
import com.culturebud.db.dao.SearchKeywordDAO;
import com.culturebud.net.ApiCommunityInterface;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by XieWei on 2016/11/6.
 */

public class SearchBookCommunityModel extends SearchBookCommunityContract.Model {
    private BookCommunityDAO bcDao;
    private SearchKeywordDAO skDao;

    @Override
    public Observable<List<BookCommunity>> search(String keyword, int page) {
        return Observable.create((subscriber) -> {
            Map<String, Object> params = getCommonParams();
            params.put("keyword", keyword);
            params.put("page", page);

            initRetrofit().create(ApiCommunityInterface.class).search(params)
                    .enqueue(new Callback<ApiResultBean<JsonObject>>() {
                        @Override
                        public void onResponse(Call<ApiResultBean<JsonObject>> call, Response<ApiResultBean<JsonObject>> response) {
                            ApiResultBean<JsonObject> bean = response.body();
                            if (bean.getCode() == 200) {
                                JsonObject jobj = bean.getData();
                                if (jobj.has("bookCommunityList")) {
                                    JsonArray jarr = jobj.getAsJsonArray("bookCommunityList");
                                    Gson gson = new Gson();
                                    List<BookCommunity> communities = gson.fromJson(jarr, new TypeToken<List<BookCommunity>>() {
                                    }.getType());
                                    subscriber.onNext(communities);
                                    subscriber.onCompleted();
                                } else {
                                    subscriber.onNext(null);
                                }
                            } else {
                                subscriber.onError(new Exception("访问出错"));
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResultBean<JsonObject>> call, Throwable t) {
                            subscriber.onError(t);
                        }
                    });
        });
    }

    @Override
    public void initDAO() throws SQLException {
//        if (bcDao == null) {
//            bcDao = new BookCommunityDAO();
//        }
        if (skDao == null) {
            skDao = new SearchKeywordDAO();
        }
    }

    @Override
    public Observable<SearchKeyword> saveKeyword(SearchKeyword keyword) {
        return Observable.create((subscriber) -> {
            try {
                initDAO();
                subscriber.onNext(skDao.save(keyword));
            } catch (SQLException e) {
                e.printStackTrace();
                subscriber.onError(e);
            }
            subscriber.onCompleted();
        });
    }

    @Override
    public Observable<List<SearchKeyword>> getKeywords(int type) {
        return Observable.create((subscriber) -> {
            try {
                initDAO();
                subscriber.onNext(skDao.getAllByType(type));
            } catch (SQLException e) {
                e.printStackTrace();
                subscriber.onError(e);
            }
            subscriber.onCompleted();
        });
    }

    @Override
    public Observable<Boolean> clearHistory(List<SearchKeyword> keywords) {
        return Observable.create((subscriber) -> {
            try {
                initDAO();
                subscriber.onNext(skDao.deleteAll(keywords));
            } catch (SQLException e) {
                e.printStackTrace();
                subscriber.onError(e);
            }
            subscriber.onCompleted();
        });
    }
}
