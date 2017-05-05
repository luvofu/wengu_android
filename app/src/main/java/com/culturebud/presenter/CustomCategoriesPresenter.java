package com.culturebud.presenter;

import com.culturebud.BaseApp;
import com.culturebud.bean.Category;
import com.culturebud.contract.CustomCategoriesContract;
import com.culturebud.model.CustomCategoriesModel;
import com.culturebud.util.ApiException;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XieWei on 2017/3/31.
 */

public class CustomCategoriesPresenter extends CustomCategoriesContract.Presenter {
    public CustomCategoriesPresenter() {
        setModel(new CustomCategoriesModel());
    }

    @Override
    public void customCategories() {
        if (!validateToken()) {
            return;
        }
        model.customCategories(BaseApp.getInstance().getUser().getToken())
        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<List<Category>>() {
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
            public void onNext(List<Category> categories) {
                view.onCustomCategories(categories);
            }
        });
    }

    @Override
    public void addCustomCategory(String category) {
        if (!validateToken()) {
            return;
        }
        model.addCategory(BaseApp.getInstance().getUser().getToken(), category)
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
                view.onCategoryChanged(aBoolean);
            }
        });
    }

    @Override
    public void deleteCustomCategory(long categoryId) {
        if (!validateToken()) {
            return;
        }
        model.deleteCategory(BaseApp.getInstance().getUser().getToken(), categoryId)
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
                view.onCategoryChanged(aBoolean);
            }
        });
    }

    @Override
    public void editCustomCategory(long categoryId, String category) {
        if (!validateToken()) {
            return;
        }
        model.editCategory(BaseApp.getInstance().getUser().getToken(), categoryId, category)
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
                view.onCategoryChanged(true);
            }
        });
    }

    @Override
    public void sortCustomCategory(String categoryIds) {
        if (!validateToken()) {
            return;
        }

        view.showProDialog();
        model.sortCategory(BaseApp.getInstance().getUser().getToken(), categoryIds)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        view.hideProDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hideProDialog();
                        e.printStackTrace();
                        if (e instanceof ApiException) {
                            view.onErrorTip(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        view.onCategorySorted(true);
                    }
                });
    }
}
