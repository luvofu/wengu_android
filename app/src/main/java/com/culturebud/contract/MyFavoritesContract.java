package com.culturebud.contract;

import com.culturebud.bean.Book;
import com.culturebud.bean.BookSheet;
import com.culturebud.model.BookBaseModel;

import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by XieWei on 2016/11/17.
 */

public interface MyFavoritesContract {

    abstract class Model extends BookBaseModel {
        public abstract Observable<List<Book>> getMyFavoriteBooks(String token, int page);

        public abstract Observable<Map<Integer, List<BookSheet>>> getMyFavoriteBookSheets(String token, int page,
                                                                                          long userId);
    }

    interface View extends BaseView {
        void onBooks(List<Book> books);

        void onBookSheets(List<BookSheet> bookSheets);

        void onDelMyFavorite(int type, long id, boolean success);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void getMyFavoriteBooks(int page);

        public abstract void getMyFavoriteBookSheets(int page);

        public abstract void deleteMyFavorite(int type, long id);
    }
}
