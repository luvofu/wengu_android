package com.culturebud.contract;

import com.culturebud.bean.Book;

import rx.Observable;

/**
 * Created by XieWei on 2016/11/26.
 */

public interface ScanBookContract {
    abstract class Model extends BaseModel {
        public abstract Observable<Book> scanBook(String token, String isbn);
    }

    interface View extends BaseView {
        void onScanBook(Book book);

        void onNotExitsBook(String tip, String isbn);

        void onScanFail();
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void scanBook(String isbn);
    }
}
