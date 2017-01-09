package com.culturebud.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;

import com.culturebud.ApiErrorCode;
import com.culturebud.BaseApp;
import com.culturebud.bean.ApiResultBean;
import com.culturebud.contract.PublishDynamicContract;
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
 * Created by XieWei on 2017/1/9.
 */

public class PublishDynamicModel extends PublishDynamicContract.Model {

    @Override
    public Observable<Boolean> publish(String token, String content, Uri imgUri,
                                       int permission, int linkType, long linkId) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }
            if (!TextUtils.isEmpty(content)) {
                params.put("content", content);
            }
            params.put("permission", permission);
            params.put("linkType", linkType);
            if (linkType > 0) {
                params.put("linkId", linkId);
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
            initRetrofit().create(ApiBookHomeInterface.class).addDynamic(realParams, body)
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
                    if (code == ApiErrorCode.CODE_SUCCESS) {
                        subscriber.onNext(true);
                    } else {
                        subscriber.onError(new ApiException(code, bean.getMsg()));
                    }
                }
            });
        });
    }
}
