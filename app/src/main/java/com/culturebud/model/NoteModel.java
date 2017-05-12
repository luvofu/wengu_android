package com.culturebud.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.text.TextUtils;

import com.culturebud.ApiErrorCode;
import com.culturebud.BaseApp;
import com.culturebud.bean.ApiResultBean;
import com.culturebud.contract.NoteContract;
import com.culturebud.net.ApiBookHomeInterface;
import com.culturebud.util.ApiException;
import com.culturebud.util.ImgUtil;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
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
 * Created by XieWei on 2016/11/23.
 */

public class NoteModel extends NoteContract.Model {
    @Override
    public Observable<Boolean> createNote(String token, long notebookId, String content, String chapter, int page, String otherLocation, Uri imgUri) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }
            params.put("notebookId", notebookId);
            if (!TextUtils.isEmpty(content)) {
                params.put("content", content);
            }
            if (!TextUtils.isEmpty(chapter)) {
                params.put("chapter", chapter);
            }
            if (page >= 0) {
                params.put("pages", page);
            }
            if (!TextUtils.isEmpty(otherLocation)) {
                params.put("otherLocation", otherLocation);
            }
            MultipartBody.Part body = null;
            String imgCachePath = BaseApp.getInstance().getCacheDir() + "/" + UUID.randomUUID().toString() + ".jpg";
            if (imgUri != null) {
                try {
                    ImgUtil.compressImage(imgUri, imgCachePath, true);

                    File file = new File(imgCachePath);
                    RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    body = MultipartBody.Part.createFormData("imageFile", file.getName(), requestBody);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), new byte[0]);
                body = MultipartBody.Part.createFormData("null", "null", requestBody);
            }
            Map<String, RequestBody> realParams = new HashMap<>();
            for (String key : params.keySet()) {
                realParams.put(key, RequestBody.create(null, params.get(key).toString()));
            }
            initRetrofit().create(ApiBookHomeInterface.class).createNote(realParams, body)
                    .subscribe(new Subscriber<ApiResultBean<JsonObject>>() {
                        @Override
                        public void onCompleted() {
                            subscriber.onCompleted();
                            File file = new File(imgCachePath);
                            if (file.exists()) {
                                file.delete();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            subscriber.onError(e);
                            File file = new File(imgCachePath);
                            if (file.exists()) {
                                file.delete();
                            }
                        }

                        @Override
                        public void onNext(ApiResultBean<JsonObject> bean) {
                            File file = new File(imgCachePath);
                            if (file.exists()) {
                                file.delete();
                            }
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

    @Override
    public Observable<Boolean> deleteNote(String token, long noteId) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }
            params.put("noteId", noteId);
            initRetrofit().create(ApiBookHomeInterface.class).deleteNote(params)
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

    @Override
    public Observable<Boolean> editNote(String token, long noteId, String content, String chapter, int pages, String otherLocation) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }
            params.put("noteId", noteId);

            if (!TextUtils.isEmpty(content)) {
                params.put("content", content);
            }

            if (!TextUtils.isEmpty(chapter)) {
                params.put("chapter", chapter);
            }

            if (pages > -1) {
                params.put("pages", pages);
            }

            if (!TextUtils.isEmpty(otherLocation)) {
                params.put("otherLocation", otherLocation);
            }

            initRetrofit().create(ApiBookHomeInterface.class).editNote(params)
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
