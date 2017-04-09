package com.culturebud.contract;

import com.culturebud.bean.Book;
import com.culturebud.bean.SearchKeyword;

import java.sql.SQLException;
import java.util.List;

import rx.Observable;

public interface ImportBookContract {
    abstract class Model extends BaseModel {
        public abstract void initDAO() throws SQLException;

        public abstract Observable<SearchKeyword> saveKeyword(SearchKeyword keyword);

        public abstract Observable<List<SearchKeyword>> getKeywords(int type);

        public abstract Observable<List<Book>> searchBooks(String keyworkd, int page);

        public abstract Observable<Boolean> clearHistory(List<SearchKeyword> keywords);

        public abstract Observable<Boolean> cacheBooks(List<Book> books);

        public abstract Observable<Boolean> addBook(Book book);

    }

    interface View extends BaseView {
        void onBooks(List<Book> books);

        void onKeywords(List<SearchKeyword> keywords);

        void onKeyword(SearchKeyword keyword);

        void onClearHistory();

        void afterAddBook(long bookId);

    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void searchBook(String keyword, int page);

        public abstract void getHistorySearchKeyword();

        public abstract void cacheKeyworkd(String keyword);

        public abstract void clearHistory(List<SearchKeyword> keywords);

        public abstract void addBook(Book book);
    }
}
