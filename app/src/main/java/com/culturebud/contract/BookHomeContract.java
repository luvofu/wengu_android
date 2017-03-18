package com.culturebud.contract;

import com.culturebud.bean.BookMark;

import java.util.List;

import rx.Observable;

/**
 * Created by XieWei on 2016/10/28.
 */

public interface BookHomeContract {
    abstract class Model extends BaseModel {
        public abstract Observable<List<BookMark>> getBookMarks(String token);
    }

    interface View extends BaseView {
        void onBookMarks(List<BookMark> bookMarks);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void getMyBookMarks();
    }
}
