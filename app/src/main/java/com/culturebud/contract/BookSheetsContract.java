package com.culturebud.contract;

import com.culturebud.bean.BookSheet;
import com.culturebud.model.BookSheetDetailModel;

import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by XieWei on 2016/12/13.
 */

public interface BookSheetsContract {
    abstract class Model extends BookSheetDetailModel {

        public abstract Observable<Map<Integer, List<BookSheet>>> getMyFavoriteBookSheets(String token, int page, long userId);

    }

    interface View extends BaseView {
        void onMyCreatedSheets(List<BookSheet> sheets);

        void onMyFavoriteSheets(List<BookSheet> sheets);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void getMyCreatedSheets();

        public abstract void getMyFavoriteSheets();
    }
}
