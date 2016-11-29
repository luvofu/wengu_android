package com.culturebud.contract;

import com.culturebud.bean.Notebook;

import java.util.List;

import rx.Observable;

/**
 * Created by XieWei on 2016/11/20.
 */

public interface NotebookContract {
    abstract class Model extends BaseModel {
        public abstract Observable<List<Notebook>> myNotebooks(String token, long userId, int page);
    }

    interface View extends BaseView {
        void onNotebooks(List<Notebook> notebooks);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void myNotebooks(int page);
    }
}
