package com.culturebud.contract;

import android.graphics.Bitmap;

import com.culturebud.bean.BookDetail;
import com.culturebud.model.BookBaseModel;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.request.Postprocessor;

import rx.Observable;

/**
 * Created by XieWei on 2016/11/7.
 */

public interface BookDetailContract {

    abstract class Model extends BookBaseModel {
        public abstract Observable<BookDetail> getBookDetail(String token, long bookId);

        public abstract Observable<BookDetail> getLocalBookDetail(long bookId);

        public abstract Observable<Boolean> cacheBookDetail(BookDetail bookDetail);

        public abstract Observable<Bitmap> blurBitmap(Bitmap bitmap);

        public abstract Observable<PipelineDraweeController> monitorImgDownload(String url, DraweeController oldController, Postprocessor postprocessor);

    }

    interface View extends BaseView {
        void onBookDetail(BookDetail detail);

        void onDraweeController(PipelineDraweeController controller);

        void onBlurCover(Bitmap bitmap);

        void onCollect(boolean success);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void getBookDetail(long bookId);

        public abstract void cacheBookDetail(BookDetail bookDetail);

        public abstract void requestCoverImg(String url, DraweeController controller);

        public abstract void collectAdd(long collectObjId);

        public abstract void collectDel(long collectObjId);
    }
}
