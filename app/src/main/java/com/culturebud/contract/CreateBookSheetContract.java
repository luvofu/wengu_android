package com.culturebud.contract;

import android.net.Uri;

import rx.Observable;

/**
 * Created by XieWei on 2016/12/14.
 */

public interface CreateBookSheetContract {
    abstract class Model extends BaseModel {
        public abstract Observable<Boolean> createBookSheet(String token, String bookSheetName, String bookSheetDesc, Uri imgUri);
    }

    interface View extends BaseView {

    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void createBookSheet(String bookSheetName, String bookSheetDesc, Uri imgUri);
    }
}
