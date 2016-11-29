package com.culturebud.presenter;

import com.culturebud.BaseApp;
import com.culturebud.bean.ApiResultBean;
import com.culturebud.bean.Comment;
import com.culturebud.bean.User;
import com.culturebud.contract.CommunityContract;
import com.culturebud.model.CommunityModel;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XieWei on 2016/10/27.
 */

public class CommunityPresenter extends CommunityContract.Presenter {

    public CommunityPresenter() {
        setModel(new CommunityModel());
    }

    @Override
    public void loadComments(int page) {
        String token = null;
        User user = BaseApp.getInstance().getUser();
        if (user != null) {
            token = user.getToken();
        }
        model.getComments(page, token).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiResultBean<JsonObject>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ApiResultBean<JsonObject> resBean) {
                        int res = resBean.getCode();
                        if (res == 200) {
                            Type type = new TypeToken<List<Comment>>() {
                            }.getType();
                            JsonObject jobj = resBean.getData();
                            JsonArray jarr = jobj.getAsJsonArray("communityCommentList");
                            List<Comment> comments = new Gson().fromJson(jarr, type);

                            view.onShowComments(comments);
                        }
                    }
                });
    }

    @Override
    public void thumbUp(int goodType, long goodObjId) {
        User user = BaseApp.getInstance().getUser();
        if (user == null) {
            return;
        }
        String token = user.getToken();
        model.thumbUp(token, goodType, goodObjId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Boolean res) {
                        view.onThumbUp(goodObjId, res);
                    }
                });
    }
}
