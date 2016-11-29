package com.culturebud.contract;

/**
 * Created by XieWei on 2016/11/3.
 */

public interface AccountSettingContract {
    interface View extends BaseView {

    }

    abstract class Model extends BaseModel {

    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void alterPassword();
    }
}
