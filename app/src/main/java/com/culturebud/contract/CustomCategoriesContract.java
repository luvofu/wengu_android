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

        public abstract Observable<Boolean> deleteCategory(String token, long categoryId);

        public abstract Observable<Boolean> editCategory(String token, long categoryId, String category);

        public abstract Observable<Boolean> sortCategory(String token, String categoryIds);
    }
    interface View extends BaseView {
        void onCustomCategories(List<Category> categories);

        void onCategoryChanged(boolean success);

        void onCategorySorted(boolean success);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void customCategories();

        public abstract void addCustomCategory(String category);

        public abstract void deleteCustomCategory(long categoryId);

        public abstract void editCustomCategory(long categoryId, String category);

        public abstract void sortCustomCategory(String categoryIds);

    }
}
