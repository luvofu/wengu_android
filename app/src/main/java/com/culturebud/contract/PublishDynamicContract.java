package com.culturebud.contract;

import android.net.Uri;

import rx.Observable;

/**
 * Created by XieWei on 2017/1/9.
 */

public interface PublishDynamicContract {

    abstract class Model extends BaseModel {
        public abstract Observable<Boolean> publish(String token, String content, Uri imgUri,
                                                    int permission, int linkType, long linkId);
    }

    interface View extends BaseView {
        void onPublishResult(boolean result);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void publish(String content, Uri imgUri, int permission, int linkType, long linkId);
    }
}
