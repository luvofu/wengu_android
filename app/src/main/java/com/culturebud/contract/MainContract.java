package com.culturebud.contract;

import com.culturebud.BaseFragment;

/**
 * Created by XieWei on 2016/10/21.
 */

public interface MainContract {

    interface View extends BaseView {
        void onAttachPages(BaseFragment[] pages);

        void onSwitchPage(BaseFragment[] pages, Class<? extends BaseFragment> pageClass);
    }

    abstract class Presenter extends BasePresenter<View, Model> {

        public abstract void initFragment();

        public abstract void attachFragment();

        public abstract void switchPage(int index);

        public abstract void hidePop();
    }

    abstract class Model extends BaseModel {

    }
}
