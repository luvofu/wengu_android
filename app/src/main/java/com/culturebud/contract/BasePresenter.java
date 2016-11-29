package com.culturebud.contract;

import android.text.TextUtils;

import com.culturebud.BaseApp;
import com.culturebud.bean.User;

/**
 * Created by XieWei on 2016/10/21.
 */

public abstract class BasePresenter<V extends BaseView, M extends BaseModel> {
    protected V view;
    protected M model;

    public void setView(V v) {
        this.view = v;
    }

    public void setModel(M m) {
        this.model = m;
    }

    public boolean validateToken() {
        User user = BaseApp.getInstance().getUser();
        if (user == null || TextUtils.isEmpty(user.getToken())) {
            view.onToLogin();
            return false;
        }
        return true;
    }

    public void countDown() {
    }
}
