package com.culturebud.contract;

import com.culturebud.bean.ApiResultBean;
import com.culturebud.bean.Book;
import com.culturebud.bean.BookSheet;
import com.culturebud.bean.Comment;
import com.culturebud.model.CommunityBaseModel;
import com.culturebud.net.ApiHomeInterface;
import com.google.gson.JsonObject;

import java.util.List;

import rx.Observable;

/**
 * Created by XieWei on 2016/10/24.
 */

public interface FrontPageContract {
    interface View extends BaseView {
        void showBooks(List<Book> books);

        void showBookSheets(List<BookSheet> bookSheets);

        void showComment(List<Comment> comments);

        void onThumbUp(long commentId, boolean isGood);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void loadDatas();

        public abstract void thumbUp(int goodType, long goodObjId);
    }

    abstract class Model extends CommunityBaseModel {
        protected ApiHomeInterface getHomeInterface() {
            return initRetrofit().create(ApiHomeInterface.class);
        }
        public abstract Observable<ApiResultBean<JsonObject>> getFrontPageData();

        public abstract Observable<Boolean> cacheBooks(List<Book> books);
    }
}
