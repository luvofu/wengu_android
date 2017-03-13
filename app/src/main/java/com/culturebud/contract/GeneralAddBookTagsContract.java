package com.culturebud.contract;

import com.culturebud.model.BookBaseModel;

import java.util.List;

/**
 * Created by XieWei on 2017/3/13.
 */

public interface GeneralAddBookTagsContract {
    abstract class Model extends BookBaseModel {

    }

    interface View extends BaseView {
        void onTags(List<String> tags);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void getBookTags();
    }
}
