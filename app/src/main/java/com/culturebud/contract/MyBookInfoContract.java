package com.culturebud.contract;

import com.culturebud.bean.UserBookInfo;
import com.culturebud.model.BookBaseModel;

import java.util.Map;

import rx.Observable;

/**
 * Created by XieWei on 2017/3/2.
 */

public interface MyBookInfoContract {
    abstract class Model extends BookBaseModel {

        public abstract Observable<UserBookInfo> myBookInfo(String token, long userBookId);

        public abstract Observable<Boolean> alterBookReadStatus(String token, long userBookId, int status);

        public abstract Observable<Boolean> editUserBookInfo(String token, long userBookId, Map<String, Object>
                editContent);
    }

    interface View extends BaseView {
        void onBookInfo(UserBookInfo userBookInfo);

        void onAlert(long userBookId, boolean res, int readStatus);

        void onEditUserBookInfo(long userBookId, Map<String, Object> editContent, boolean res);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void myBookInfo(long userBookId);

        public abstract void alterBookReadStatus(long userBookId, int status);

        public abstract void editUserBookInfo(long userBookId, Map<String, Object> editContent);
    }
}
