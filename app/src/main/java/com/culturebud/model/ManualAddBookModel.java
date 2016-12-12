package com.culturebud.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;

import com.culturebud.BaseApp;
import com.culturebud.bean.ApiResultBean;
import com.culturebud.contract.ManualAddBookContract;
import com.culturebud.net.ApiBookHomeInterface;
import com.culturebud.util.ApiException;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by XieWei on 2016/12/10.
 */

public class ManualAddBookModel extends ManualAddBookContract.Model {
    @Override
    public Observable<Boolean> addBook(String token, Uri imgUri, String bookName, String nameOrgin,
                                       String subTitle, String isbn, String authors, String translators,
                                       String price, String publisher, String pubDate, String binding,
                                       String pages, String summary, String authorInfo) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }
            if (!TextUtils.isEmpty(bookName)) {
                params.put("title", bookName);
            }
            if (!TextUtils.isEmpty(nameOrgin)) {
                params.put("originTitle", nameOrgin);
            }
            if (!TextUtils.isEmpty(subTitle)) {
                params.put("subTitle", subTitle);
            }
            if (!TextUtils.isEmpty(isbn)) {
                params.put("isbn", isbn);
            }
            if (!TextUtils.isEmpty(authors)) {
                params.put("author", authors);
            }
            if (!TextUtils.isEmpty(translators)) {
                params.put("translator", translators);
            }
            if (!TextUtils.isEmpty(price)) {
                params.put("price", price);
            }
            if (!TextUtils.isEmpty(publisher)) {
                params.put("publisher", publisher);
            }
            if (!TextUtils.isEmpty(pubDate)) {
                params.put("pubDate", pubDate);
            }
            if (!TextUtils.isEmpty(binding)) {
                params.put("binding", binding);
            }
            if (!TextUtils.isEmpty(pages)) {
                params.put("pages", pages);
            }
            if (!TextUtils.isEmpty(summary)) {
                params.put("summary", summary);
            }
            if (!TextUtils.isEmpty(authorInfo)) {
                params.put("authorInfo", authorInfo);
            }

            MultipartBody.Part body = null;
            String imgCachePath = BaseApp.getInstance().getCacheDir() + "/" + UUID.randomUUID().toString() + ".jpg";
            if (imgUri != null) {
                try {
                    InputStream is = BaseApp.getInstance().getContentResolver().openInputStream(imgUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    FileOutputStream fos = new FileOutputStream(imgCachePath);
                    int bsize = bitmap.getByteCount() / 1024;
                    int scale = 1;
                    if (bsize > 230) {
                        scale = bsize / 230;
                    }
                    if (scale > 100) {
                        scale = 100;
                    }
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100 / scale, fos);
                    File file = new File(imgCachePath);
                    RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    body = MultipartBody.Part.createFormData("imageFile", file.getName(), requestBody);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), new byte[0]);
                body = MultipartBody.Part.createFormData("null", "null", requestBody);
            }
            Map<String, RequestBody> realParams = new HashMap<>();
            for (String key : params.keySet()) {
                realParams.put(key, RequestBody.create(null, params.get(key).toString()));
            }
            initRetrofit().create(ApiBookHomeInterface.class).manualAddBook(realParams, body)
            .subscribe(new Subscriber<ApiResultBean<JsonObject>>() {
                @Override
                public void onCompleted() {
                    subscriber.onCompleted();
                }

                @Override
                public void onError(Throwable e) {
                    subscriber.onError(e);
                }

                @Override
                public void onNext(ApiResultBean<JsonObject> bean) {
                    int code = bean.getCode();
                    if (code == 200) {
                        subscriber.onNext(true);
                    } else {
                        subscriber.onError(new ApiException(code, bean.getMsg()));
                    }
                }
            });
        });
    }
}
