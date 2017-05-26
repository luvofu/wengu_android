package com.culturebud.contract;

import com.culturebud.bean.Friend;

import java.util.List;

import rx.Observable;

/**
 * Created by XieWei on 2016/12/26.
 */

public interface UserSearchContract {

    abstract class Model extends BaseModel {
        public abstract Observable<List<Friend>> search(String token, String keyword, int page);
    }

    interface View extends BaseView {
        void onUsers(List<Friend> users);

    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void search(String keyword, int page);
    }
}
