package com.culturebud.contract;

import android.graphics.Bitmap;

import com.culturebud.bean.BookDetail;
import com.culturebud.bean.BookSheet;
import com.culturebud.model.BookBaseModel;
import com.culturebud.model.BookSheetDetailModel;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.request.Postprocessor;

import java.util.List;

import rx.Observable;

/**
 * Created by XieWei on 2016/11/7.
 */

public interface BookDetailContract {

    abstract class Model extends BookSheetDetailModel {
        public abstract Observable<BookDetail> getBookDetail(String token, long bookId);

        public abstract Observable<BookDetail> getLocalBookDetail(long bookId);

        public abstract Observable<Boolean> cacheBookDetail(BookDetail bookDetail);

        public abstract Observable<Bitmap> blurBitmap(Bitmap bitmap);

        public abstract Observable<PipelineDraweeController> monitorImgDownload(String url, DraweeController
                oldController, Postprocessor postprocessor);

    }

    interface View extends BaseView {
        void onBookDetail(BookDetail detail);

        void onDraweeController(PipelineDraweeController controller);

        void onBlurCover(Bitmap bitmap);

        void onCollect(boolean success);

        void onCollectDel(boolean success);

        void onMySheets(List<BookSheet> bookSheets);

        void onSheetAddBook(long sheetId, long bookId, boolean success);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void getBookDetail(long bookId);

        public abstract void cacheBookDetail(BookDetail bookDetail);

        public abstract void requestCoverImg(String url, DraweeController controller);

        public abstract void collectAdd(long collectObjId);

        public abstract void collectDel(long collectObjId);

        public abstract void getMySheets();

        public abstract void bookSheetAddBook(long sheetId, long bookId);
    }
}
