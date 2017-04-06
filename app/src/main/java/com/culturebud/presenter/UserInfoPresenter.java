package com.culturebud.presenter;

import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.culturebud.BaseApp;
import com.culturebud.CommonConst;
import com.culturebud.bean.User;
import com.culturebud.contract.UserInfoContract;
import com.culturebud.model.UserInfoModel;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XieWei on 2016/11/16.
 */

public class UserInfoPresenter extends UserInfoContract.Presenter {
    private static final String TAG = UserInfoPresenter.class.getSimpleName();

    public UserInfoPresenter() {
        setModel(new UserInfoModel());
    }

    @Override
    public void editAvatar(long imageObjId, Uri imgUri, boolean isJPEG) {
        if (!validateToken()) {
            return;
        }
        User user = BaseApp.getInstance().getUser();
        view.showProDialog();
        model.uploadImage(user.getToken(), CommonConst.UploadImgType.TYPE_USER_AVATAR, imageObjId, imgUri, isJPEG)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        view.hideProDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hideProDialog();
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(String newUrl) {
                        Log.d(TAG, newUrl);
                        BaseApp.getInstance().getUser().setAvatar(newUrl);
                        view.onEditImg(newUrl);
                        updateLocalUser();
                    }
                });
    }

    @Override
    public void editNick(String nick) {
        if (!validateToken()) {
            return;
        }
        User user = BaseApp.getInstance().getUser();
        model.alterNick(user.getToken(), nick).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<User>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(User user) {
                        view.onNick(user.getNickname());
                        BaseApp.getInstance().getUser().setNickname(user.getNickname());
                        updateLocalUser();
                    }
                });
    }

    @Override
    public void editEmail(String email) {
        if (!validateToken()) {
            return;
        }
        model.alterEmail(BaseApp.getInstance().getUser().getToken(), email)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<User>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(User user) {
                        view.onEmail(user.getMailbox());
                        BaseApp.getInstance().getUser().setMailbox(user.getMailbox());
                        updateLocalUser();
                    }
                });
    }

    @Override
    public void editAutograph(String autograph) {
        if (!validateToken()) {
            return;
        }
        model.alterAutograph(BaseApp.getInstance().getUser().getToken(), autograph)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<User>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(User user) {
                        view.onAutograph(user.getAutograph());
                        BaseApp.getInstance().getUser().setAutograph(user.getAutograph());
                        updateLocalUser();
                    }
                });
    }

    private void updateLocalUser() {
        model.updateLocelUser(BaseApp.getInstance().getUser()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        Log.i(TAG, "update user success = " + aBoolean);
                    }
                });
    }
}
