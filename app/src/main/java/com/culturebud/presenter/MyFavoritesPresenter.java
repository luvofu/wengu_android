package com.culturebud.presenter;

import com.culturebud.BaseApp;
import com.culturebud.bean.Book;
import com.culturebud.bean.BookSheet;
import com.culturebud.bean.User;
import com.culturebud.contract.MyFavoritesContract;
import com.culturebud.model.MyFavoritesModel;
import com.culturebud.util.ApiException;

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

        view.showLoadingView(page != 0);
        model.getMyFavoriteBooks(BaseApp.getInstance().getUser().getToken(), page)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Book>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hiddenNoDataView();
                        e.printStackTrace();

                        String errorMessage = ApiException.getErrorMessage(e);
                        if (page == 0) {
                            view.showErrorView(errorMessage);
                        } else {
                            view.onErrorTip(errorMessage);
                        }
                    }

                    @Override
                    public void onNext(List<Book> books) {
                        view.hiddenNoDataView();

                        if (page == 0 && books.isEmpty()) {
                            view.showNoDataView("没有收藏的书籍");
                        }

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

        view.showLoadingView(page != 0);
        model.getMyFavoriteBookSheets(user.getToken(), page, user.getUserId())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Map<Integer, List<BookSheet>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hiddenNoDataView();
                        e.printStackTrace();
                        String errorMessage = ApiException.getErrorMessage(e);
                        if (page == 0) {
                            view.showErrorView(errorMessage);
                        } else {
                            view.onErrorTip(errorMessage);
                        }
                    }

                    @Override
                    public void onNext(Map<Integer, List<BookSheet>> map) {
                        view.hiddenNoDataView();

                        if (page == 0 && map.size() == 0) {
                            view.showNoDataView("没有收藏的书单");
                        }

                        if (map.size() > 0) {
                            for (Integer key : map.keySet()) {
                                view.onBookSheets(map.get(key));
                            }
                        }
                    }
                });
    }

    @Override
    public void deleteMyFavorite(int type, long id) {
        if (!validateToken()) {
            return;
        }
        view.showLoadingView(true);
        model.collectDel(BaseApp.getInstance().getUser().getToken(), type, id)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        view.hiddenNoDataView();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hiddenNoDataView();
                        e.printStackTrace();
                        String errorMessage = ApiException.getErrorMessage(e);
                        view.onErrorTip(errorMessage);
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        view.onDelMyFavorite(type, id, aBoolean);
                    }
                });
    }
}
