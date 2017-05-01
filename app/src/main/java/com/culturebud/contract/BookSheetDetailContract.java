package com.culturebud.contract;

import com.culturebud.bean.BookSheet;
import com.culturebud.bean.BookSheetDetail;
import com.culturebud.model.BookBaseModel;
import com.google.gson.JsonObject;

import java.util.List;

import rx.Observable;

/**
 * Created by XieWei on 2016/11/7.
 */

public interface BookSheetDetailContract {
    abstract class Model extends BookBaseModel {
        public abstract Observable<JsonObject> getBookSheetDetail(String token, long sheetId);

        public abstract Observable<List<BookSheet>> getMySheets(String token, long userId);
    }

    interface View extends BaseView {
        void onBookSheetDetail(BookSheetDetail detail);

        void onRelationType(int type);

        void onCollect(boolean isCollected);

        void onCollectDel(boolean isCollected);

        void onMySheets(List<BookSheet> bookSheets);

        void onSheetAddBook(long bookSheetId, long bookId, boolean result);

        void onSheetDelBook(long sheetBookId, boolean result);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void getBookSheetDetail(long sheetId);

        public abstract void collectAdd(long sheetId);

        public abstract void collectDel(long sheetId);

        public abstract void getMySheets();

        public abstract void bookSheetAddBook(long bookSheetId, long bookId);

        public abstract void bookSheetDelBook(long sheetBookId);
    }
}
