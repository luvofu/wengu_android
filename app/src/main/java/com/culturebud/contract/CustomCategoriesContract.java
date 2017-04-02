package com.culturebud.contract;

import com.culturebud.bean.Category;

import java.util.List;

import rx.Observable;

/**
 * Created by XieWei on 2017/3/31.
 */

public interface CustomCategoriesContract {
    abstract class Model extends BaseModel {
        public abstract Observable<List<Category>> customCategories(String token);

        public abstract Observable<Boolean> addCategory(String token, String category);
    }
    interface View extends BaseView {
        void onCustomCategories(List<Category> categories);

        void onAddCategory(boolean success);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void customCategories();

        public abstract void addCustomCategory(String category);
    }
}
