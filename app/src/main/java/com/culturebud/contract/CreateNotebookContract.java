package com.culturebud.contract;

import com.culturebud.bean.CollectedBook;

import rx.Observable;

/**
 * Created by XieWei on 2016/11/22.
 */

public interface CreateNotebookContract {
    abstract class Model extends BaseModel {
        public abstract Observable<Boolean> createNotebook(String token, String notebookName, long bookId);
    }

    interface View extends BaseView {
        void onCreateNotebook(boolean res, long bookId);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void createNotebook(String notebookName, CollectedBook book);
    }
}
