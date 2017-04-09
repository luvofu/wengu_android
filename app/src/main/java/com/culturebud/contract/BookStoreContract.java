package com.culturebud.contract;

import com.culturebud.bean.Book;
import com.culturebud.bean.BookSheet;

import java.util.List;

import rx.Observable;

/**
 * Created by XieWei on 2016/11/3.
 */

public interface BookStoreContract {

    abstract class Model extends BaseModel {
        public abstract Observable<List<Book>> getBooks(String token, int page, int sortType, String filterType);

        public abstract Observable<List<BookSheet>> getBookSheets(String token, int page, int sortType, String filterType);

        public abstract Observable<Boolean> cacheBooks(List<Book> books);

        public abstract Observable<List<String>> getBookSheetFilters();

        public abstract Observable<List<String>> getBookFilters();
    }

    interface View extends BaseView {
        void onShowBooks(List<Book> books);

        void onShowBookSheets(List<BookSheet> sheets);

        void initFilters(List<String> filters);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void getBooks(int page, int sortType, String filterType);

        public abstract void getBookSheets(int page, int sortType, String filterType);

        public abstract void cacheBooks(List<Book> books);

        public abstract  void getFiltersByIsBookSheets(boolean isBookSheets);
    }
}
