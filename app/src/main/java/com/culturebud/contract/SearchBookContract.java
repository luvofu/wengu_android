package com.culturebud.contract;

import com.culturebud.bean.Book;
import com.culturebud.bean.SearchKeyword;
import com.culturebud.model.BookBaseModel;

import java.sql.SQLException;
import java.util.List;

import rx.Observable;

/**
 * Created by XieWei on 2016/11/5.
 */

public interface SearchBookContract {
    abstract class Model extends BookBaseModel {
        public abstract void initDAO() throws SQLException;

        public abstract Observable<SearchKeyword> saveKeyword(SearchKeyword keyword);

        public abstract Observable<List<SearchKeyword>> getKeywords(int type);

        public abstract Observable<List<Book>> searchBooks(String keyworkd, int page);

        public abstract Observable<Boolean> clearHistory(List<SearchKeyword> keywords);

        public abstract Observable<Boolean> cacheBooks(List<Book> books);

    }

    interface View extends BaseView {
        void onBooks(List<Book> books);

        void onKeywords(List<SearchKeyword> keywords);

        void onKeyword(SearchKeyword keyword);

        void onClearHistory();

        void onSheetAddBook(long bookSheetId, long bookId, boolean result);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void searchBook(String keyword, int page);

        public abstract void getHistorySearchKeyword();

        public abstract void cacheKeyword(String keyword);

        public abstract void clearHistory(List<SearchKeyword> keywords);

        public abstract void bookSheetAddBook(long bookSheetId, long bookId);
    }
}
