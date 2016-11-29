package com.culturebud.contract;

import android.graphics.Bitmap;
import android.net.Uri;

import com.culturebud.bean.User;

import rx.Observable;

/**
 * Created by XieWei on 2016/11/3.
 */

public interface UserInfoContract {

    abstract class Model extends BaseModel {

        public abstract Observable<Boolean> updateLocelUser(User user);

        public abstract Observable<User> alterNick(String token, String nick);

        public abstract Observable<User> alterEmail(String token, String email);

        public abstract Observable<User> alterAutograph(String token, String autograph);

    }

    interface View extends BaseView {

        void onEditImg(String newUrl);

        void onNick(String nick);

        void onEmail(String email);

        void onAutograph(String autograph);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void editAvatar(long imageObjId, Uri imgUri, boolean isJPEG);

        public abstract void editNick(String nick);

        public abstract void editEmail(String email);

        public abstract void editAutograph(String autograph);
    }
}
