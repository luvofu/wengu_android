package com.culturebud.contract;

import com.culturebud.model.BookBaseModel;

import java.util.List;

import rx.Observable;

/**
 * Created by XieWei on 2017/3/13.
 */

public interface GeneralAddTagsContract {
    abstract class Model extends BookBaseModel {
        public abstract Observable<List<String>> getBookTags(String token);

        public abstract Observable<List<String>> getBookSheetTags(String token);
    }

    interface View extends BaseView {
        void onTags(List<String> tags);

        void onHistoryTags(List<String> tags);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void getBookTags();

        public abstract void getBookSheetTags();
    }
}
