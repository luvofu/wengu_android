package com.culturebud.presenter;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.culturebud.BaseApp;
import com.culturebud.bean.BookDetail;
import com.culturebud.bean.BookSheet;
import com.culturebud.bean.User;
import com.culturebud.contract.BookDetailContract;
import com.culturebud.model.BookDetailModel;
import com.culturebud.util.ApiException;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.Postprocessor;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XieWei on 2016/11/7.
 */

public class BookDetailPresenter extends BookDetailContract.Presenter {

    public BookDetailPresenter() {
        setModel(new BookDetailModel());
    }

    @Override
    public void getBookDetail(long bookId) {
        if (bookId <= 0) {

            return;
        }
        getRemoteBookDetail(bookId);
    }

    private void getLocalBookDetail(long bookId) {
        model.getLocalBookDetail(bookId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BookDetail>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        getRemoteBookDetail(bookId);
                    }

                    @Override
                    public void onNext(BookDetail bookDetail) {
                        if (bookDetail != null) {
                            view.onBookDetail(bookDetail);
                        }
                    }
                });
    }

    private void getRemoteBookDetail(long bookId) {
        User user = BaseApp.getInstance().getUser();
        String token = null;
        if (user != null) {
            token = user.getToken();
        }

        model.getBookDetail(token, bookId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BookDetail>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(BookDetail detail) {
                        if (detail != null) {
                            view.onBookDetail(detail);
                            cacheBookDetail(detail);
                        } else {
                            getLocalBookDetail(bookId);
                        }
                    }
                });
    }

    @Override
    public void cacheBookDetail(BookDetail bookDetail) {
        model.cacheBookDetail(bookDetail).subscribeOn(Schedulers.io())
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

                    }
                });
    }

    @Override
    public void requestCoverImg(String url, DraweeController controller) {
        Postprocessor redMeshPostprocessor = new BasePostprocessor() {
            @Override
            public String getName() {
                return "redMeshPostprocessor";
            }

            @Override
            public void process(Bitmap bitmap) {
                processBitmap(bitmap);
            }
        };
        model.monitorImgDownload(url, controller, redMeshPostprocessor).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PipelineDraweeController>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(PipelineDraweeController controller) {
                        if (controller != null) {
                            view.onDraweeController(controller);
                        }
                    }
                });
    }

    @Override
    public void collectAdd(long collectObjId) {
        if (!validateToken()) {
            return;
        }

        model.collectAdd(BaseApp.getInstance().getUser().getToken(), 0, collectObjId).subscribeOn(Schedulers.io())
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
                        view.onCollect(res);
                    }
                });
    }

    @Override
    public void collectDel(long collectObjId) {
        if (!validateToken()) {
            return;
        }

        model.collectDel(BaseApp.getInstance().getUser().getToken(), 0, collectObjId).subscribeOn(Schedulers.io())
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
                        view.onCollectDel(res);
                    }
                });
    }

    @Override
    public void getMySheets() {
        if (!validateToken()) {
            return;
        }
        User user = BaseApp.getInstance().getUser();
        model.getMySheets(user.getToken(), user.getUserId())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<BookSheet>>() {
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
                    public void onNext(List<BookSheet> bookSheets) {
                        view.onMySheets(bookSheets);
                    }
                });
    }

    @Override
    public void bookSheetAddBook(long sheetId, long bookId) {
        if (!validateToken()) {
            return;
        }
        model.bookSheetAddBook(BaseApp.getInstance().getUser().getToken(), sheetId, bookId)
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
                        view.onSheetAddBook(sheetId, bookId, aBoolean);
                    }
                });
    }

    private void processBitmap(Bitmap bitmap) {
        model.blurBitmap(bitmap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Bitmap>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Bitmap bitmap) {
                        if (bitmap != null && !bitmap.isRecycled()) {
                            view.onBlurCover(bitmap);
                        }
                    }
                });
    }
}
