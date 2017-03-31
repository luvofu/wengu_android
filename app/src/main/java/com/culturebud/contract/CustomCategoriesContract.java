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
    }
    interface View extends BaseView {
        void onCustomCategories(List<Category> categories);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void customCategories();
    }
}
