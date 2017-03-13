package com.culturebud.contract;

import com.culturebud.bean.UserBookInfo;
import com.culturebud.model.BookBaseModel;

import rx.Observable;

/**
 * Created by XieWei on 2017/3/2.
 */

public interface MyBookInfoContract {
    abstract class Model extends BookBaseModel {
        public abstract Observable<UserBookInfo> myBookInfo(String token, long userBookId);
    }

    interface View extends BaseView {
        void onBookInfo(UserBookInfo userBookInfo);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void myBookInfo(long userBookId);
    }
}
