package com.culturebud.contract;

import android.net.Uri;

import rx.Observable;

/**
 * Created by XieWei on 2017/4/8.
 */

public interface BookSheetEditContract {
    abstract class Model extends BaseModel {
        public abstract Observable<Boolean> editBookSheet(String token, long bookSheetId, String bsName, String desc,
                                                          String tag);
    }

    interface View extends BaseView {
        void onEdit(boolean success,String content, int requestcode);
        void onCoverEdit(String coverurl);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void editBookSheet(long bookSheetId, String bsName, String desc, String tag, int requestcode);

        public abstract void editCover(Uri uri, long bsId);
    }
}
