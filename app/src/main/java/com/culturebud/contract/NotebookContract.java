package com.culturebud.contract;

import com.culturebud.bean.CollectedBook;
import com.culturebud.bean.Notebook;

import java.util.List;

import rx.Observable;

/**
 * Created by XieWei on 2016/11/20.
 */

public interface NotebookContract {
    abstract class Model extends BaseModel {
        public abstract Observable<List<Notebook>> userNotebooks(String token, long userId, int page);

        public abstract Observable<Boolean> deleteNotebook(String token, long notebookId);

        public abstract Observable<Boolean> createNotebook(String token, long bookId);

    }

    interface View extends BaseView {
        void onNotebooks(List<Notebook> notebooks);

        void onDeleteNotebook(Notebook notebook, boolean success);

        void onCreateNotebook(boolean res, long bookId);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void userNotebooks(int page, long userId);

        public abstract void deleteNotebook(Notebook notebook);

        public abstract void createNotebook(long bookId);

    }
}
