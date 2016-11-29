package com.culturebud.model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.culturebud.ApiErrorCode;
import com.culturebud.bean.ApiResultBean;
import com.culturebud.bean.BookDetail;
import com.culturebud.contract.BookDetailContract;
import com.culturebud.db.dao.BookDetailDAO;
import com.culturebud.net.ApiBookInterface;
import com.culturebud.util.ApiException;
import com.culturebud.util.ImgUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.imagepipeline.request.Postprocessor;
import com.google.gson.JsonObject;

import java.sql.SQLException;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by XieWei on 2016/11/7.
 */

public class BookDetailModel extends BookDetailContract.Model {

    private BookDetailDAO bdDao;

    private void initDAO() throws SQLException {
        if (bdDao == null) {
            bdDao = new BookDetailDAO();
        }
    }

    @Override
    public Observable<BookDetail> getBookDetail(String token, long bookId) {
        return Observable.create((subscriber) -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }
            params.put("bookId", bookId);
            initRetrofit().create(ApiBookInterface.class)
                    .getBookDetail(params).subscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<ApiResultBean<BookDetail>>() {
                        @Override
                        public void onCompleted() {
                            subscriber.onCompleted();
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            subscriber.onError(e);
                        }

                        @Override
                        public void onNext(ApiResultBean<BookDetail> bean) {
                            int code = bean.getCode();
                            if (code == 200) {
                                subscriber.onNext(bean.getData());
                            } else {
                                subscriber.onError(new ApiException(code, bean.getMsg()));
                            }
                        }
                    });
        });
    }

    @Override
    public Observable<BookDetail> getLocalBookDetail(long bookId) {
        return Observable.create(subscriber -> {
            try {
                initDAO();
                BookDetail detail = bdDao.findByBookId(bookId);
                subscriber.onNext(detail);
            } catch (SQLException e) {
                e.printStackTrace();
//                subscriber.onNext(null);
                subscriber.onError(e);
            }
            subscriber.onCompleted();
        });
    }

    @Override
    public Observable<Boolean> cacheBookDetail(BookDetail bookDetail) {
        return Observable.create(subscriber -> {
            try {
                initDAO();
                boolean res = bdDao.save(bookDetail);
                subscriber.onNext(res);
            } catch (SQLException e) {
                e.printStackTrace();
                subscriber.onError(e);
            }
            subscriber.onCompleted();
        });
    }

    @Override
    public Observable<Bitmap> blurBitmap(Bitmap bitmap) {
        return Observable.create(subscriber -> {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int x = width / 4, y = height / 4;
            Bitmap img = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                img = ImgUtil.blurBitmap(Bitmap.createBitmap(bitmap, x, y, width / 2, height / 4));
                img = ImgUtil.blurBitmap(Bitmap.createScaledBitmap(bitmap, width / 10, height / 10, false));
            } else {
                img = ImgUtil.blurImageAmeliorate(Bitmap.createScaledBitmap(bitmap, width / 10, height / 10, false));
//                img = ImgUtil.blurImageAmeliorate(Bitmap.createBitmap(bitmap, x, y, width / 2, height / 4));
            }
            subscriber.onNext(img);
        });
    }

    @Override
    public Observable<PipelineDraweeController> monitorImgDownload(String url, DraweeController oldController, Postprocessor redMeshPostprocessor) {
        return Observable.create(subscriber -> {

            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
                    .setPostprocessor(redMeshPostprocessor)
                    .build();

            PipelineDraweeController ctrl = (PipelineDraweeController)
                    Fresco.newDraweeControllerBuilder()
                            .setImageRequest(request)
                            .setOldController(oldController)
                            .build();
            subscriber.onNext(ctrl);
        });
    }
}
