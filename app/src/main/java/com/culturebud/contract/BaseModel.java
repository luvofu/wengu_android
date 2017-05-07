package com.culturebud.contract;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.culturebud.ApiErrorCode;
import com.culturebud.BaseApp;
import com.culturebud.CommonConst;
import com.culturebud.bean.ApiResultBean;
import com.culturebud.bean.User;
import com.culturebud.db.dao.UserDAO;
import com.culturebud.net.ApiCommonInterface;
import com.culturebud.net.ApiFileInterface;
import com.culturebud.net.ApiMeInterface;
import com.culturebud.util.ApiException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by XieWei on 2016/10/21.
 */

public abstract class BaseModel {
    private UserDAO userDAO;
    private static final String TAG = BaseModel.class.getSimpleName();
    protected static final String TOKEN_KEY = CommonConst.TOKEN_KEY;
    protected static final String PLATFORM_KEY = CommonConst.PLATFORM_KEY;
    protected static final String PLATFORM = CommonConst.PLATFORM;

    protected static final String DEVICE_TOKEN_KEY = CommonConst.DEVICE_TOKEN_KEY;
    protected static final String DEVICE_TOKEN = CommonConst.DEVICE_TOKEN;
    private static final int DEFAULT_TIMEOUT = 20;

    private Retrofit retrofit;

    protected Retrofit initRetrofit() {
        if (retrofit == null) {
            synchronized (BaseModel.class) {
                if (retrofit == null) {
                    OkHttpClient client = new OkHttpClient.Builder()
                            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                            .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                            .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                            .build();

                    retrofit = new Retrofit.Builder()
                            .baseUrl(CommonConst.getHost())
                            .client(client)
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }
        return retrofit;
    }

    protected Map<String, Object> getCommonParams() {
        Map<String, Object> params = new HashMap<>();
        params.put(PLATFORM_KEY, PLATFORM);
        params.put(DEVICE_TOKEN_KEY, DEVICE_TOKEN);
        return params;
    }

    protected void clearUser() throws SQLException {
        if (userDAO == null) {
            userDAO = new UserDAO();
        }
        User user = BaseApp.getInstance().getUser();
        if (user != null) {
            userDAO.delUser(user);
        }
        BaseApp.getInstance().setUser(null);
    }

    /**
     * String token;令牌
     * int imageType;图片类型：
     * 用户头像UserAvatar(0), 用户背景UserBackground(1),
     * 书单封面BookSheetCover(2),笔记内容NotebookContent(3),
     * 动态内容DynamicContent(4);
     * long imageObjId;图片所属对象
     * File imageFile;图片文件
     *
     * @return
     */
    public Observable<String> uploadImage(String token, int imageType, long imageObjId, Uri imgUri, boolean isJPEG) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }
            params.put("imageType", imageType);
            params.put("imageObjId", imageObjId);
            ContentResolver resolver = BaseApp.getInstance().getContentResolver();
            String sufix = ".png";
            if (isJPEG) {
                sufix = ".jpg";
            }
            String imgPath = BaseApp.getInstance().getCacheDir() + "/" + UUID.randomUUID().toString() + sufix;
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(resolver.openInputStream(imgUri));
                Log.d(TAG, imgPath);
                FileOutputStream fos = new FileOutputStream(imgPath);
                int bsize = bitmap.getByteCount() / 1024;
                int scale = 1;
                if (bsize > 160) {
                    scale = bsize / 160;
                }
                if (scale > 100) {
                    scale = 100;
                }
                if (isJPEG) {
                    bitmap.compress(CompressFormat.JPEG, 100 / scale, fos);
                } else {
                    bitmap.compress(CompressFormat.PNG, 100 / scale, fos);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            File file = new File(imgPath);
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("imageFile", file.getName(), requestBody);
            Map<String, RequestBody> realParams = new HashMap<>();
            for (String key : params.keySet()) {
                realParams.put(key, RequestBody.create(null, params.get(key).toString()));
            }
            initRetrofit().create(ApiFileInterface.class).uploadImage(realParams, body)
                    .subscribe(new Subscriber<ApiResultBean<JsonObject>>() {
                        @Override
                        public void onCompleted() {
                            subscriber.onCompleted();
                        }

                        @Override
                        public void onError(Throwable e) {
                            subscriber.onError(e);
                            file.delete();
                        }

                        @Override
                        public void onNext(ApiResultBean<JsonObject> bean) {
                            Log.d(TAG, bean.toString());
                            int code = bean.getCode();
                            if (code == ApiErrorCode.CODE_SUCCESS) {
                                JsonObject jobj = bean.getData();
                                if (jobj.has("imageUrl")) {
                                    subscriber.onNext(jobj.get("imageUrl").getAsString());
                                }
                            } else {
                                subscriber.onError(new ApiException(code, bean.getMsg()));
                            }
                            file.delete();
                        }
                    });
        });
    }

    public Observable<Bitmap> downloadFile(String url) {
        ApiFileInterface dowload = initRetrofit().create(ApiFileInterface.class);
        return dowload.downloadFile(url).subscribeOn(Schedulers.newThread())
                .map(responseBody -> {
                    Log.e(TAG, "call()******>>>>");
                    String path = Uri.parse(url).getPath();
                    if (!path.startsWith("/")) {
                        path = "/" + path;
                    }
                    String dir = BaseApp.getInstance().getFilesDir().getPath();
                    FileOutputStream fos = null;
                    Log.e(TAG, dir + path);
                    try {
                        File file = new File(dir + path.substring(0, path.lastIndexOf("/")));
                        file.mkdirs();
                        fos = new FileOutputStream(dir + path);
                        fos.write(responseBody.bytes());
                        fos.flush();
                        Bitmap bitmap = BitmapFactory.decodeFile(dir + path);
                        if (bitmap != null && !bitmap.isRecycled()) {
                            return bitmap;
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        return null;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    } finally {
                        if (fos != null) {
                            try {
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    return null;
                });
    }

    /**
     * @param token       令牌
     * @param deleteType  删除类型:评论Comment(0),评论回复CommentReply(1),动态Dynamic(2),动态回复DynamicReply(3)
     * @param deleteObjId 删除对象id
     * @return
     */
    public Observable<Boolean> delete(String token, int deleteType, long deleteObjId) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }
            params.put("deleteType", deleteType);
            params.put("deleteObjId", deleteObjId);
            initRetrofit().create(ApiCommonInterface.class).commonDelete(params)
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

    /**
     * String token;令牌（查验手机号、绑定新手机号）
     * String deviceToken;设备标识
     * String regMobile;请求验证码手机号
     * int useCondition;使用场景：
     * 注册Signup(0),忘记密码ForgetPsw(1),
     * 查验手机号CheckMobile(2),绑定手机号BindMobile(3);
     *
     * @return
     */
    public Observable<Boolean> getSucrityCode(String token, String phoneNumber, int useCondition, int thirdType) {
        return Observable.create(subscriber -> {
            Map<String, Object> params = getCommonParams();
            if (!TextUtils.isEmpty(token)) {
                params.put(TOKEN_KEY, token);
            }
            params.put("regMobile", phoneNumber);
            params.put("useCondition", useCondition);
            if (thirdType >= 0) {
                params.put("thirdType", thirdType);
            }
            initRetrofit().create(ApiMeInterface.class).getSucrityCode(params)
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

    public Observable<Integer> countDown(int time) {
        if (time < 0) {
            time = 0;
        }
        final int countTime = time;
        return Observable.interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .map((increaseTime) -> countTime - increaseTime.intValue())
                .take(countTime + 1);

    }
}
