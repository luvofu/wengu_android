package com.culturebud.contract;

import com.culturebud.bean.CollectedBook;

import java.util.List;

import rx.Observable;

/**
 * Created by XieWei on 2016/11/9.
 */

public interface CollectedBooksContract {

    abstract class Model extends BaseModel {
        public abstract Observable<List<CollectedBook>> getCollectedBooks(String token, long userId, int page);

        public abstract Observable<List<CollectedBook>> getCollectedBooks(String token, long userId, int page, int
                categoryType, String category);
    }

    interface View extends BaseView {
        void onBooks(List<CollectedBook> books);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void getMyBooks(int page);

        public abstract void getMyBooks(int page, int categoryType, String category);
    }
}
