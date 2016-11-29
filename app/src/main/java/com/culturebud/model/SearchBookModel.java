package com.culturebud.model;

import com.culturebud.bean.ApiResultBean;
import com.culturebud.bean.Book;
import com.culturebud.bean.SearchKeyword;
import com.culturebud.contract.SearchBookContract;
import com.culturebud.db.dao.BookDAO;
import com.culturebud.db.dao.SearchKeywordDAO;
import com.culturebud.net.ApiBookInterface;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by XieWei on 2016/11/5.
 */

public class SearchBookModel extends SearchBookContract.Model {
    private SearchKeywordDAO skDao;
    private BookDAO bookDAO;

    @Override
    public void initDAO() throws SQLException {
        if (skDao == null) {
            skDao = new SearchKeywordDAO();
        }
        if (bookDAO == null) {
            bookDAO = new BookDAO();
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
    public Observable<List<Book>> searchBooks(String keyword, int page) {
        return Observable.create((subscriber) -> {
            ApiBookInterface abi = initRetrofit().create(ApiBookInterface.class);
            Map<String, Object> paramas = getCommonParams();
            paramas.put("page", page);
            paramas.put("keyword", keyword);
            abi.searchBooks(paramas).subscribe(new Subscriber<ApiResultBean<JsonObject>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(ApiResultBean<JsonObject> bean) {
                    int code = bean.getCode();
                    if (code == 200) {
                        JsonObject jobj = bean.getData();
                        if (jobj.has("bookList")) {
                            JsonArray jarr = jobj.getAsJsonArray("bookList");
                            Gson gson = new Gson();
                            List<Book> books = gson.fromJson(jarr, new TypeToken<List<Book>>() {
                            }.getType());
                            subscriber.onNext(books);
                        }
                    } else {
                        subscriber.onError(new Exception(bean.getMsg()));
                    }
                    subscriber.onCompleted();
                }
            });
        });
    }

    @Override
    public Observable<Boolean> clearHistory(List<SearchKeyword> keywords) {
        return Observable.create((subscriber) -> {
            try {
                initDAO();
                boolean res = skDao.deleteAll(keywords);
                subscriber.onNext(res);
                subscriber.onCompleted();
            } catch (SQLException e) {
                e.printStackTrace();
                subscriber.onError(e);
            }
        });
    }

    @Override
    public Observable<Boolean> cacheBooks(List<Book> books) {
        return Observable.create((subscriber) -> {
            try {
                initDAO();
                for (Book book : books) {
                    boolean res = bookDAO.addBook(book);
                    subscriber.onNext(res);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                subscriber.onError(e);
            }
            subscriber.onCompleted();
        });
    }
}
