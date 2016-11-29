package com.culturebud.contract;

/**
 * Created by XieWei on 2016/10/28.
 */

public interface BookHomeContract {
    abstract class Model extends BaseModel {

    }

    interface View extends BaseView {

    }

    abstract class Presenter extends BasePresenter<View, Model> {
    }
}
