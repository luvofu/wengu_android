package com.culturebud.presenter;

import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.culturebud.BaseApp;
import com.culturebud.CommonConst;
import com.culturebud.CommonConst.ThumbUpType;
import com.culturebud.bean.ApiResultBean;
import com.culturebud.bean.BookCircleDynamic;
import com.culturebud.bean.DynamicReply;
import com.culturebud.bean.User;
import com.culturebud.contract.BookCircleContract;
import com.culturebud.model.BookCircleModel;
import com.culturebud.util.ApiException;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XieWei on 2016/10/28.
 */

public class BookCirclePresenter extends BookCircleContract.Presenter {
    private static final String TAG = BookCirclePresenter.class.getSimpleName();

    public BookCirclePresenter() {
        setModel(new BookCircleModel());
    }

    @Override
    public void loadDynamics(int page) {
        if (!validateToken()) {
            return;
        }
        User user = BaseApp.getInstance().getUser();
        model.getDynamics(page, user.getToken(), user.getUserId())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiResultBean<JsonObject>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("bCircle", "onError()");
                    }

                    @Override
                    public void onNext(ApiResultBean<JsonObject> res) {
                        Log.d("bCircle", "onNext()");
                        long total = -1;//动态总数
                        int relationType = -1;//关系类型：陌生人Stranger(0),好友Friend(1),个人Personal(2);

                        int resCode = res.getCode();
                        JsonObject jobj = res.getData();
                        Gson gson = new Gson();
                        Log.e("xwlljj", jobj.toString());
                        if (jobj.has("total")) {
                            total = gson.fromJson(jobj.get("total"), long.class);
                        }
                        if (jobj.has("relationType")) {
                            relationType = gson.fromJson(jobj.get("relationType"), int.class);
                        }
                        Log.e("xwlljj", "total = " + total + ", relationType = " + relationType);

                        if (jobj.has("bookCircleDynamicList")) {
                            JsonArray jarr = jobj.getAsJsonArray("bookCircleDynamicList");
                            List<BookCircleDynamic> bcds = gson.fromJson(jarr, new TypeToken<List<BookCircleDynamic>>
                                    () {
                            }.getType());

                            view.onDynamics(bcds);
                        }
                    }
                });
    }

    @Override
    public void downloadBgImg() {
        if (!validateToken()) {
            return;
        }
        User user = BaseApp.getInstance().getUser();
        if (TextUtils.isEmpty(user.getBackground())) {
            return;
        }
        String url = user.getBackground();
        model.downloadFile(url).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Bitmap>() {
                    @Override
                    public void onCompleted() {
                        Log.e(TAG, "onCompleted()");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.e(TAG, "onError()*******>>>" + e.getMessage());
                    }

                    @Override
                    public void onNext(Bitmap bitmap) {
                        if (bitmap != null && !bitmap.isRecycled()) {
                            view.onBgImg(bitmap);
                        }
                    }
                });
    }

    @Override
    public void uploadBgImg(Uri imgUri, boolean isJPG) {
        if (!validateToken()) {
            return;
        }
        view.showProDialog();
        User user = BaseApp.getInstance().getUser();
        model.uploadImage(user.getToken(), CommonConst.UploadImgType.TYPE_USER_BG, user.getUserId(), imgUri, isJPG)
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
                        if (e instanceof ApiException) {
                            view.onErrorTip(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(String s) {
                        Log.d("xiewei", "user before update " + user);
                        view.onUploadBgImg(s);
                        uploadLocalUser(s);
                    }
                });
    }

    private void uploadLocalUser(String url) {
        User user = BaseApp.getInstance().getUser();
        if (user != null) {
            user.setBackground(url);
            model.updateLocelUser(user).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<Boolean>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                }

                @Override
                public void onNext(Boolean aBoolean) {
                    Log.d("xiewei", "user update " + aBoolean);
                    Log.d("xiewei", "user update " + user);
                }
            });
        }
    }

    @Override
    public void thumbUp(long dynamicId) {
        if (!validateToken()) {
            return;
        }
        model.thumbUp(BaseApp.getInstance().getUser().getToken(), ThumbUpType.TYPE_DYNAMIC, dynamicId)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e instanceof ApiException) {
                            view.onErrorTip(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(Boolean result) {
                        view.onThumbUp(dynamicId, result);
                    }
                });
    }

    @Override
    public void replyDynamic(long dynamicId, String content, int replyType, long replyObjId) {
        if (!validateToken()) {
            return;
        }
        if (TextUtils.isEmpty(content)) {
            view.onErrorTip("回复内容不能为空");
            return;
        }
        model.replyDynamic(BaseApp.getInstance().getUser().getToken(), dynamicId, content, replyType, replyObjId)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DynamicReply>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e instanceof ApiException) {
                            view.onErrorTip(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(DynamicReply bean) {
                        view.onDynamicReply(bean);
                    }
                });
    }

    @Override
    public void deleteDynamicOrReply(long dynamicId, int deleteType, long deleteObjId) {
        if (!validateToken()) {
            return;
        }
        model.delete(BaseApp.getInstance().getUser().getToken(), deleteType, deleteObjId)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e instanceof ApiException) {
                            view.onErrorTip(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        view.onDeleteResult(dynamicId, deleteType, deleteObjId, aBoolean);
                    }
                });
    }
}
