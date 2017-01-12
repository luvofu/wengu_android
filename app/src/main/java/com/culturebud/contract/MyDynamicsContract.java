package com.culturebud.contract;

import com.culturebud.bean.BookCircleDynamic;
import com.culturebud.bean.BookCircleDynamicRelationMe;

import java.util.List;

import rx.Observable;

/**
 * Created by XieWei on 2017/1/12.
 */

public interface MyDynamicsContract {
    abstract class Model extends BaseModel {
        public abstract Observable<List<BookCircleDynamic>> myPublished(String token, int page);

        public abstract Observable<List<BookCircleDynamicRelationMe>> myRelations(String token, int page);
    }

    interface View extends BaseView {
        void onDynamics(List<BookCircleDynamic> dynamics);
        void onRelations(List<BookCircleDynamicRelationMe> dynamics);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void myPublished(int page);

        public abstract void myRelations(int page);
    }
}
