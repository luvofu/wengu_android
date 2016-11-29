package com.culturebud.presenter;

import com.culturebud.BaseApp;
import com.culturebud.bean.Book;
import com.culturebud.bean.BookSheet;
import com.culturebud.bean.User;
import com.culturebud.contract.MyFavoritesContract;
import com.culturebud.model.MyFavoritesModel;

import java.util.List;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XieWei on 2016/11/17.
 */

public class MyFavoritesPresenter extends MyFavoritesContract.Presenter {

    public MyFavoritesPresenter() {
        setModel(new MyFavoritesModel());
    }

    @Override
    public void getMyFavoriteBooks(int page) {
        if (!validateToken()) {
            return;
        }
        model.getMyFavoriteBooks(BaseApp.getInstance().getUser().getToken(), page)
        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<List<Book>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(List<Book> books) {
                view.onBooks(books);
            }
        });
    }

    @Override
    public void getMyFavoriteBookSheets(int page) {
        if (!validateToken()) {
            return;
        }
        User user = BaseApp.getInstance().getUser();
        model.getMyFavoriteBookSheets(user.getToken(), page, user.getUserId())
        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<Map<Integer, List<BookSheet>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Map<Integer, List<BookSheet>> map) {
                if (map.size() > 0) {
                    for (Integer key : map.keySet()) {
                        view.onBookSheets(map.get(key));
                    }
                }
            }
        });
    }
}
