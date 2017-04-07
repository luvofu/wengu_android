package com.culturebud.contract;

import com.culturebud.bean.BookCategoryGroup;
import com.culturebud.bean.Category;
import com.culturebud.bean.CollectedBook;
import com.culturebud.model.CustomCategoriesModel;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Set;

import rx.Observable;

/**
 * Created by XieWei on 2016/11/9.
 */

public interface CollectedBooksContract {

    abstract class Model extends CustomCategoriesModel {
        public abstract Observable<List<CollectedBook>> getCollectedBooks(String token, long userId, int page);

        public abstract Observable<List<CollectedBook>> getCollectedBooks(String token, long userId, int page, int
                categoryType, String category);

        public abstract Observable<BookCategoryGroup> getCategoryStatistics(String token, long userId);

        public abstract Observable<Boolean> deleteUserBooks(String token, Set<CollectedBook> userBooks);

        public abstract Observable<Boolean> alterReadStatus(String token, Set<CollectedBook> userBooks, int readStatus);

        public abstract Observable<Boolean> moveBook2CustomCategory(String token, Set<CollectedBook> books, String
                category);
    }

    interface View extends BaseView {
        void onBooks(List<CollectedBook> books);

        void onCategoryStatistics(BookCategoryGroup categoryGroup);

        void onDeleteUserBooks(Set<CollectedBook> books, boolean success);

        void onCustomCategories(List<Category> categories);

        void onMove2Category(boolean success);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void getMyBooks(long userId, int page);

        public abstract void getMyBooks(long userId, int page, int categoryType, String category);

        public abstract void getCategoryStatistics();

        public abstract void deleteUserBooks(Set<CollectedBook> userBooks);

        public abstract void alterReadStatus(Set<CollectedBook> userBooks, int readStatus);

        public abstract void customCategories();

        public abstract void moveBook2CustomCategory(Set<CollectedBook> books, String category);
    }
}
