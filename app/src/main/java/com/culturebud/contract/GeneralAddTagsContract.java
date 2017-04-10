package com.culturebud.contract;

import com.culturebud.bean.HistoryTag;
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

        public abstract Observable<List<HistoryTag>> getLocalHistory(byte type, long userId);

        public abstract Observable<Boolean> saveHistoryToLocal(HistoryTag tag);
    }

    interface View extends BaseView {
        void onTags(List<String> tags);

        void onHistoryTags(List<String> tags);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void getBookTags();

        public abstract void getBookSheetTags();

        public abstract void getHistoryTag(byte type);

        public abstract void recordHistory(byte type, String tag);
    }
}
