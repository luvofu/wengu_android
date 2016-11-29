package com.culturebud.contract;

import android.graphics.Bitmap;

import com.culturebud.bean.ApiResultBean;
import com.culturebud.bean.BookCircleDynamic;
import com.culturebud.net.ApiBookHomeInterface;
import com.google.gson.JsonObject;

import java.util.List;

import rx.Observable;

/**
 * Created by XieWei on 2016/10/28.
 */

public interface BookCircleContract {
    abstract class Model extends BaseModel {
        protected ApiBookHomeInterface getBookCircleInterface() {
            return initRetrofit().create(ApiBookHomeInterface.class);
        }
        public abstract Observable<ApiResultBean<JsonObject>> getDynamics(int page, String token, long userId);
    }

    interface View extends BaseView {
        void onDynamics(List<BookCircleDynamic> dynamics);

        void onBgImg(Bitmap bitmap);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void loadDynamics(int page);

        public abstract void downloadBgImg();
    }

}
