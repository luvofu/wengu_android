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

        public abstract Observable<Boolean> updateLocalUser(User user);

        public abstract Observable<User> alterNick(String token, String nick);

        public abstract Observable<User> alterEmail(String token, String email);

        public abstract Observable<User> alterAutograph(String token, String autograph);

        public abstract Observable<User> alterSex(String token, int sex);

        public abstract Observable<User> alterUsername(String token, String username);

    }

    interface View extends BaseView {

        void onEditImg(String newUrl);

        void onNick(String nick);

        void onEmail(String email);

        void onAutograph(String autograph);

        void onSex(int sex);

        void onUsername(String username);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void editAvatar(long imageObjId, Uri imgUri, boolean isJPEG);

        public abstract void editNick(String nick);

        public abstract void editEmail(String email);

        public abstract void editAutograph(String autograph);

        public abstract void editSex(int sex);

        public abstract void editUsername(String username);
    }
}
