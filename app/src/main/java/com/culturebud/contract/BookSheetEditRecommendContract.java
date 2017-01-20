package com.culturebud.contract;

import rx.Observable;

/**
 * Created by XieWei on 2017/1/20.
 */

public interface BookSheetEditRecommendContract {
    abstract class Model extends BaseModel {
        public abstract Observable<Boolean> editRecommend(String token, long sheetBookId, String recommend);
    }

    interface View extends BaseView {
        void onEditRecommend(long sheetBookId, boolean result);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void editRecommend(long sheetBookId, String recommend);
    }
}
