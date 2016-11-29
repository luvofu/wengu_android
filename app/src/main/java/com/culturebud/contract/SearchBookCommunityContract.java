package com.culturebud.contract;

import com.culturebud.bean.BookCommunity;
import com.culturebud.bean.SearchKeyword;

import java.sql.SQLException;
import java.util.List;

import rx.Observable;

/**
 * Created by XieWei on 2016/11/6.
 */

public interface SearchBookCommunityContract {
    abstract class Model extends BaseModel {
        public abstract Observable<List<BookCommunity>> search(String keyword, int page);

        public abstract void initDAO() throws SQLException;

        public abstract Observable<SearchKeyword> saveKeyword(SearchKeyword keyword);

        public abstract Observable<List<SearchKeyword>> getKeywords(int type);

        public abstract Observable<Boolean> clearHistory(List<SearchKeyword> keywords);

    }

    interface View extends BaseView {
        void onKeywords(List<SearchKeyword> keywords);

        void onKeyword(SearchKeyword keyword);

        void onClearHistory();

        void onBookCommunities(List<BookCommunity> communities);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void search(String keyword, int page);

        public abstract void getHistorySearchKeyword();

        public abstract void cacheKeyworkd(String keyword);

        public abstract void clearHistory(List<SearchKeyword> keywords);

    }
}
