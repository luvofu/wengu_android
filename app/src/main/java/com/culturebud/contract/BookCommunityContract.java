package com.culturebud.contract;

import com.culturebud.bean.BookCommunityDetail;
import com.culturebud.bean.Comment;
import com.culturebud.model.CommunityBaseModel;

import java.util.List;

import rx.Observable;

/**
 * Created by XieWei on 2016/11/10.
 */

public interface BookCommunityContract {

    abstract class Model extends CommunityBaseModel {
        public abstract Observable<BookCommunityDetail> getCommunityDetail(String token, long communityId);

        public abstract Observable<List<Comment>> getComments(String token, int page, int sortType, long communityId);
    }

    interface View extends BaseView {
        void onCommunityDetail(BookCommunityDetail detail);

        void onComments(List<Comment> comments);

        void onThumbUp(boolean res, long commentId, int position);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void getCommunityDetail(long communityId);

        public abstract void getCommunityComments(int page, int sortType, long communityId);

        public abstract void thumbUp(long commentId, int position);
    }
}
