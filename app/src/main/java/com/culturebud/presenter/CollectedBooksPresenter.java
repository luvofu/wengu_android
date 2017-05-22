package com.culturebud.presenter;

import com.culturebud.BaseApp;
import com.culturebud.bean.BookCategoryGroup;
import com.culturebud.bean.Category;
import com.culturebud.bean.CollectedBook;
import com.culturebud.bean.User;
import com.culturebud.contract.CollectedBooksContract;
import com.culturebud.model.CollectedBooksModel;
import com.culturebud.util.ApiException;

import java.util.List;
import java.util.Set;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XieWei on 2016/11/9.
 */

public class CollectedBooksPresenter extends CollectedBooksContract.Presenter {

    public CollectedBooksPresenter() {
        setModel(new CollectedBooksModel());
    }

    @Override
    public void getCollectedBooks(long userId, int page) {
        if (!validateToken()) {
            return;
        }

        view.showLoadingView(page != 0);
        User user = BaseApp.getInstance().getUser();
        model.getCollectedBooks(user.getToken(), userId == -1 ? user.getUserId() : userId, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<CollectedBook>>() {
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
                    public void onNext(List<CollectedBook> collectedBooks) {
                        view.hiddenNoDataView();

                        if (page == 0 && collectedBooks.isEmpty()) {
                            view.showNoDataView("该分类尚无书籍");
                        }

                        view.onBooks(collectedBooks);

                    }
                });
    }

    @Override
    public void getCollectedBooks(long userId, int page, int categoryType, String category) {
        if (!validateToken()) {
            return;
        }
        User user = BaseApp.getInstance().getUser();

        view.showLoadingView(page != 0);
        model.getCollectedBooks(user.getToken(), userId == -1 ? user.getUserId() : userId, page, categoryType, category)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<CollectedBook>>() {
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
                    public void onNext(List<CollectedBook> collectedBooks) {
                        view.hiddenNoDataView();

                        if (page == 0 && collectedBooks.isEmpty()) {
                            view.showNoDataView("该分类尚无书籍");
                        }

                        view.onBooks(collectedBooks);
                    }
                });
    }

    @Override
    public void getCategoryStatistics(long userId) {
        if (!validateToken()) {
            return;
        }
        User user = BaseApp.getInstance().getUser();
        view.showLoadingView(true);
        model.getCategoryStatistics(user.getToken(), userId)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BookCategoryGroup>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hiddenNoDataView();
                        e.printStackTrace();

                        String errorMessage = ApiException.getErrorMessage(e);

                        view.onErrorTip(errorMessage);
                    }

                    @Override
                    public void onNext(BookCategoryGroup res) {
                        view.hiddenNoDataView();
                        view.onCategoryStatistics(res);
                    }
                });
    }

    @Override
    public void deleteUserBooks(Set<CollectedBook> userBooks) {
        if (!validateToken()) {
            return;
        }

        view.showLoadingView(true);
        model.deleteUserBooks(BaseApp.getInstance().getUser().getToken(), userBooks)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

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
                        view.hiddenNoDataView();
                        view.onDeleteUserBooks(userBooks, aBoolean);
                    }
                });
    }

    @Override
    public void alterReadStatus(Set<CollectedBook> userBooks, int readStatus) {
        if (!validateToken()) {
            return;
        }

        view.showLoadingView(true);
        model.alterReadStatus(BaseApp.getInstance().getUser().getToken(), userBooks, readStatus)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

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
                        view.hiddenNoDataView();
                        view.onAlterReadStatus(userBooks, aBoolean);
                    }
                });
    }

    @Override
    public void customCategories() {
        if (!validateToken()) {
            return;
        }

        view.showLoadingView();
        model.customCategories(BaseApp.getInstance().getUser().getToken())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Category>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hiddenNoDataView();
                        e.printStackTrace();
                        String errorMessage = ApiException.getErrorMessage(e);
                        view.onErrorTip(errorMessage);
                    }

                    @Override
                    public void onNext(List<Category> categories) {
                        view.hiddenNoDataView();
                        view.onCustomCategories(categories);
                    }
                });
    }

    @Override
    public void moveBook2CustomCategory(Set<CollectedBook> books, String category) {
        if (!validateToken()) {
            return;
        }

        view.showLoadingView(true);
        model.moveBook2CustomCategory(BaseApp.getInstance().getUser().getToken(), books, category)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hiddenNoDataView();
                        e.printStackTrace();
                        String errorMessage = ApiException.getErrorMessage(e);
                        view.onErrorTip(e.getMessage());
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        view.hiddenNoDataView();
                        view.onMove2Category(aBoolean);
                    }
                });
    }
}
