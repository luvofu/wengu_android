package com.culturebud.contract;

import com.culturebud.bean.BookMark;

import java.util.List;

import rx.Observable;

/**
 * Created by XieWei on 2016/10/28.
 */

public interface BookHomeContract {
    abstract class Model extends BaseModel {
        public abstract Observable<List<BookMark>> getBookMarks(String token);

        public abstract Observable<Boolean> addBookMark(String token, long userBookId, int pages, int totalPage);

        public abstract Observable<Boolean> alterBookMark(String token, long bookmarkId, int pages, int totalPage);

        public abstract Observable<Boolean> delBookMark(String token, long bookmarkId);
    }

    interface View extends BaseView {
        void onBookMarks(List<BookMark> bookMarks);

        void onAddBookMark(boolean success);

        void onAlterBookMark(boolean success);

        void onDelBookMark(boolean success, BookMark bookMark);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void getMyBookMarks();

        public abstract void addBookMark(long userBookId, int pages, int totalPage);

        public abstract void alterBookMark(long bookmarkId, int pages, int totalPage);

        public abstract void delBookMark(BookMark bookMark);

    }
}
