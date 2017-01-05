package com.culturebud.contract;

import com.culturebud.bean.BookCircleDynamic;

import rx.Observable;

/**
 * Created by XieWei on 2017/1/4.
 */

public interface DynamicDetailContract {
    abstract class Model extends BaseModel {
        public abstract Observable<BookCircleDynamic> dynamicDetail(String token, long dynamicId);
    }

    interface View extends BaseView {
        void onDynamic(BookCircleDynamic dynamic);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void dynamicDetail(long dynamicId);
    }
}
