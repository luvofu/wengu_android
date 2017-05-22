package com.culturebud.presenter;

import com.culturebud.BaseApp;
import com.culturebud.CommonConst;
import com.culturebud.bean.BookCategoryGroup;
import com.culturebud.bean.Category;
import com.culturebud.bean.CollectedBook;
import com.culturebud.bean.User;
import com.culturebud.contract.CollectedBooksContract;
import com.culturebud.model.CollectedBooksModel;
import com.culturebud.util.ApiException;

import java.util.ArrayList;
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
    public void getCollectedBooks(long userId, int page, int categoryType, String category) {
        if (!validateToken()) {
            return;
        }
        User user = BaseApp.getInstance().getUser();
        model.getCollectedBooks(user.getToken(), userId == -1 ? user.getUserId() : userId, page, categoryType, category)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<CollectedBook>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<CollectedBook> collectedBooks) {
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
        model.getCategoryStatistics(user.getToken(), userId)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BookCategoryGroup>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(BookCategoryGroup res) {
                        view.onCategoryStatistics(res);
                    }
                });
    }

    @Override
    public void deleteUserBooks(Set<CollectedBook> userBooks) {
        if (!validateToken()) {
            return;
        }
        model.deleteUserBooks(BaseApp.getInstance().getUser().getToken(), userBooks)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e instanceof ApiException) {
                            view.onErrorTip(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        view.onDeleteUserBooks(userBooks, aBoolean);
                    }
                });
    }

    @Override
    public void alterReadStatus(Set<CollectedBook> userBooks, int readStatus) {
        if (!validateToken()) {
            return;
        }
        model.alterReadStatus(BaseApp.getInstance().getUser().getToken(), userBooks, readStatus)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e instanceof ApiException) {
                            view.onErrorTip(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        view.onAlterReadStatus(userBooks, aBoolean);
                    }
                });
    }

    @Override
    public void getCustomCategories(BookCategoryGroup categoryGroup) {
//        if (!validateToken()) {
//            return;
//        }
//        model.getCustomCategories(BaseApp.getInstance().getUser().getToken())
//                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<List<Category>>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                        if (e instanceof ApiException) {
//                            view.onErrorTip(e.getMessage());
//                        }
//                    }
//
//                    @Override
//                    public void onNext(List<Category> categories) {
//                        view.onCustomCategories(categories);
//                    }
//                });
        List<Category> categories = new ArrayList<>();
        for (BookCategoryGroup.CategoryGroup cg : categoryGroup.getCategoryGroups()) {
            if (cg.getCategoryType() == CommonConst.UserBookCategoryType.TYPE_CUSTOM) {
                for (BookCategoryGroup.Category category : cg.getCategoryStatistics()) {
                    Category c = new Category();
                    c.setCategory(category.getCategory());
                    c.setStatis(category.getStatistics());
                    categories.add(c);
                }
            }
        }
        view.onCustomCategories(categories);
    }

    @Override
    public void moveBook2CustomCategory(Set<CollectedBook> books, String category) {
        if (!validateToken()) {
            return;
        }
        model.moveBook2CustomCategory(BaseApp.getInstance().getUser().getToken(), books, category)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e instanceof ApiException) {
                            view.onErrorTip(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        view.onMove2Category(aBoolean);
                    }
                });
    }
}
