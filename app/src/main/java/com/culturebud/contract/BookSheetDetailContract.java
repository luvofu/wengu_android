package com.culturebud.contract;

import com.culturebud.bean.BookSheetDetail;
import com.culturebud.model.BookBaseModel;
import com.google.gson.JsonObject;

import rx.Observable;

/**
 * Created by XieWei on 2016/11/7.
 */

public interface BookSheetDetailContract {
    abstract class Model extends BookBaseModel {
        public abstract Observable<JsonObject> getBookSheetDetail(String token, long sheetId);
    }

    interface View extends BaseView {
        void onBookSheetDetail(BookSheetDetail detail);

        void onRelationType(int type);

        void onCollect(boolean isCollected);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void getBookSheetDetail(long sheetId);

        public abstract void collectAdd(long sheetId);

        public abstract void collectDel(long sheetId);
    }
}
