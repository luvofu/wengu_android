package com.culturebud.contract;

import com.culturebud.bean.BookCategoryGroup;
import com.culturebud.bean.CollectedBook;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Set;

import rx.Observable;

/**
 * Created by XieWei on 2016/11/9.
 */

public interface CollectedBooksContract {

    abstract class Model extends BaseModel {
        public abstract Observable<List<CollectedBook>> getCollectedBooks(String token, long userId, int page);

        public abstract Observable<List<CollectedBook>> getCollectedBooks(String token, long userId, int page, int
                categoryType, String category);

        public abstract Observable<BookCategoryGroup> getCategoryStatistics(String token, long userId);

        public abstract Observable<Boolean> deleteUserBooks(String token, Set<CollectedBook> userBooks);
    }

    interface View extends BaseView {
        void onBooks(List<CollectedBook> books);

        void onCategoryStatistics(BookCategoryGroup categoryGroup);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void getMyBooks(int page);

        public abstract void getMyBooks(int page, int categoryType, String category);

        public abstract void getCategoryStatistics();

        public abstract void deleteUserBooks(Set<CollectedBook> userBooks);
    }
}
