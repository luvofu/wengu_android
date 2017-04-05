package com.culturebud.contract;

import com.culturebud.bean.Book;

import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by XieWei on 2017/4/5.
 */

public interface BookScanResultContract {
    abstract class Model extends BaseModel {
        public abstract Observable<Boolean> addScanBooks(String token, Map<String, List<Book>> scanBooks);
    }

    interface View extends BaseView {
        void onAdd(boolean success);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void addScanBooks(Map<String, List<Book>> books);
    }
}
