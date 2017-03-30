package com.culturebud.contract;

import com.culturebud.bean.CheckedBook;

import java.util.List;

import rx.Observable;

/**
 * Created by XieWei on 2017/3/30.
 */

public interface MyCreatedBooksContract {
    abstract class Model extends BaseModel {
        public abstract Observable<List<CheckedBook>> myCreatedBooks(String token, long page);
    }

    interface View extends BaseView {
        void onMyBooks(List<CheckedBook> books);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void myCreatedBooks(long page);
    }
}
