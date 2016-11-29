package com.culturebud.contract;

import com.culturebud.model.CommunityBaseModel;

/**
 * Created by XieWei on 2016/11/11.
 */

public interface PublishShortCommentContract {
    abstract class Model extends CommunityBaseModel {

    }

    interface View extends BaseView {
        void onPublisResult(boolean res);

        void onContentEmpty();
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void publish(long communityId, String content);
    }
}
