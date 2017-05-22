package com.culturebud;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.BottomSheetDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.culturebud.contract.BasePresenter;
import com.culturebud.ui.MainActivity;
import com.culturebud.ui.me.LoginActivity;
import com.culturebud.util.ClassUtil;
import com.culturebud.util.ImgUtil;
import com.culturebud.util.WidgetUtil;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.tendcloud.tenddata.TCAgent;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;
import java.util.WeakHashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_LOGIN;
import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_PHOTO_CROP;
import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_SELECT_IMAGE;
import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_TAKE_PHOTO;

/**
 * Created by XieWei on 2016/10/19.
 */

public abstract class BaseActivity<P extends BasePresenter> extends TitleBarActivity {
    private static final String TAG = BaseActivity.class.getSimpleName();
    public static final int RESULT_CANCELED_CUSTOM = -2;
    protected P presenter;
    private ProgressDialog progressDialog;
    private BottomSheetDialog editImgDialog;
    private TextView tvAlbum, tvPhoto, tvCancel;
    protected Uri photoUri;
    protected int aspectX = 1, aspectY = 1, outX = 480, outY = 720;
    private static WeakHashMap<String, Activity> acts = new WeakHashMap<>();

    public static void finishAll() {
        for (Activity a : acts.values()) {
            if (a != null && !a.isFinishing()) {
                a.finish();
            }
        }
        acts.clear();
    }

    public interface OnSoftKeyboardStateChangedListener {
        void onSoftKeyboardStateChanged(boolean isKeyBoardShow, int keyboardHeight);
    }

    //注册软键盘状态变化监听
    public void addSoftKeyboardChangedListener(OnSoftKeyboardStateChangedListener listener) {
        if (listener != null) {
            mKeyboardStateListeners.add(listener);
        }
    }

    //取消软键盘状态变化监听
    public void removeSoftKeyboardChangedListener(OnSoftKeyboardStateChangedListener listener) {
        if (listener != null) {
            mKeyboardStateListeners.remove(listener);
        }
    }

    private ArrayList<OnSoftKeyboardStateChangedListener> mKeyboardStateListeners;      //软键盘状态监听列表
    private ViewTreeObserver.OnGlobalLayoutListener mLayoutChangeListener;
    private boolean mIsSoftKeyboardShowing;
    private int screenHeight;

    public boolean softKeyboardHasShowing() {
        return mIsSoftKeyboardShowing;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        acts.put(getClass().getName(), this);
        if (savedInstanceState != null) {
            String FRAGMENTS_TAG = "Android:support:fragments";
            savedInstanceState.remove(FRAGMENTS_TAG);
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Loading . . .");
        initPresenter();
        hideTitlebar();
        listenKeyboard();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {
            case R.id.tv_opera_content://相册
            {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
//                intent.putExtra("crop", "true");
//                intent.putExtra("aspectX", 4);
//                intent.putExtra("aspectY", 4);
//                intent.putExtra("outputX", 300);
//                intent.putExtra("outputY", 300);
//                intent.putExtra("scale", true);
//                intent.putExtra("return-data", false);

                Log.d(TAG, CommonConst.getRootPath());
                File dir = new File(CommonConst.CAPTURE_PATH.replace("file://", ""));
                Log.d(TAG, "ext storage dir permission " + Environment.getExternalStorageState());
                if (!dir.exists()) {
                    boolean res = dir.mkdirs();
                    Log.d(TAG, "dir is create " + res);
                }
                photoUri = Uri.parse(CommonConst.CAPTURE_PATH + "/" + UUID.randomUUID() + ".jpg");
                Log.d(TAG, "photo uri is " + photoUri);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//                intent.putExtra("noFaceDetection", true); // no face detection
                startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
                editImgDialog.dismiss();
                break;
            }
            case R.id.tv_del://拍照
            {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                    ContentValues contentValues = new ContentValues(2);
                    //如果想拍完存在系统相机的默认目录,改为
                    contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, UUID.randomUUID().toString() + ".jpg");
                    contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                    photoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(cameraIntent, REQUEST_CODE_TAKE_PHOTO);
                }
                editImgDialog.dismiss();
                break;
            }
            case R.id.tv_cancel://取消
                editImgDialog.dismiss();
                break;
        }
    }

    protected void showPhotoDialog() {
        new RxPermissions(this).request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(grant -> {
                    if (grant) {
                        initEditImgDialog();
                        editImgDialog.show();
                    } else {
                        onErrorTip("您拒绝了授权，将无法使用拍照和相册图片！");
                    }
                });
    }

    private void initEditImgDialog() {
        if (editImgDialog == null) {
            editImgDialog = new BottomSheetDialog(this);
            editImgDialog.setContentView(R.layout.bottom_sheet_dialog);
            editImgDialog.setCancelable(true);
            editImgDialog.getWindow().findViewById(android.support.design.R.id.design_bottom_sheet)
                    .setBackgroundResource(android.R.color.transparent);
            tvAlbum = (TextView) editImgDialog.getWindow().findViewById(R.id.tv_opera_content);
            tvPhoto = (TextView) editImgDialog.getWindow().findViewById(R.id.tv_del);
            tvCancel = (TextView) editImgDialog.getWindow().findViewById(R.id.tv_cancel);
            tvAlbum.setText("相册");
            tvPhoto.setText("相机");
            tvAlbum.setGravity(Gravity.CENTER);
            WidgetUtil.setRawTextSize(tvAlbum, getResources().getDimensionPixelSize(R.dimen.dialog_opera_font_size));
            tvAlbum.setTextColor(Color.BLUE);
            tvPhoto.setTextColor(Color.BLUE);
            tvCancel.setTextColor(Color.BLUE);
            tvAlbum.setOnClickListener(this);
            tvPhoto.setOnClickListener(this);
            tvCancel.setOnClickListener(this);
        }
    }

    public void showProDialog() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog.show();
    }

    public void showProDialog(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            progressDialog.setMessage(msg);
        }
        showProDialog();
    }

    public void hideProDialog() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void listenKeyboard() {
        WindowManager wm = getWindowManager();
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        screenHeight = dm.heightPixels;
        mIsSoftKeyboardShowing = false;
        mKeyboardStateListeners = new ArrayList<>();
        mLayoutChangeListener = () -> {
            //判断窗口可见区域大小
            Rect r = new Rect();
            getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
            //如果屏幕高度和Window可见区域高度差值大于整个屏幕高度的1/3，则表示软键盘显示中，否则软键盘为隐藏状态。
//                int heightDifference = screenHeight - (r.bottom - r.top);
            int heightDifference = screenHeight - r.bottom;//沉浸式 activity 不需要减去 top
            boolean isKeyboardShowing = heightDifference > screenHeight / 3;

            //如果之前软键盘状态为显示，现在为关闭，或者之前为关闭，现在为显示，则表示软键盘的状态发生了改变
            if ((mIsSoftKeyboardShowing && !isKeyboardShowing) || (!mIsSoftKeyboardShowing && isKeyboardShowing)) {
                mIsSoftKeyboardShowing = isKeyboardShowing;
                for (int i = 0; i < mKeyboardStateListeners.size(); i++) {
                    OnSoftKeyboardStateChangedListener listener = mKeyboardStateListeners.get(i);
                    listener.onSoftKeyboardStateChanged(mIsSoftKeyboardShowing, heightDifference);
                }
            }
        };
        //注册布局变化监听
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(mLayoutChangeListener);
    }

    private void initPresenter() {
        presenter = ClassUtil.getPresenter(this);
    }

    public void onToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, REQUEST_CODE_LOGIN);
    }

    private Toast toast;

    public void onErrorTip(String tip) {
        if (!TextUtils.isEmpty(tip)) {
            if (toast != null) {
                toast.cancel();
            }
            toast = Toast.makeText(this, tip, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void  showLoadingView() {
        getNoDataView().showLoading();
    }

    public void showNoDataView(String nodataDesc) {
        getNoDataView().showNoDataView(nodataDesc);
    }

    public void hiddenNoDataView() {
        getNoDataView().hiddenNoDataView();
    }

    public void  showErrorView(String errorDesc) {
        getNoDataView().setOnRetryClickListener(view -> {
            onRetryData();
        });

        getNoDataView().showErrorView(errorDesc);
    }

    public void showNoNetView() {
        getNoDataView().setOnRetryClickListener(view -> {
            onRetryData();
        });

        getNoDataView().showNoNetwork();
    }

    public void onRetryData() {
        //子类需要刷新的都需要重载.
    }

    public void showErrorView(String errorDesc, View.OnClickListener listener) {
        getNoDataView().setOnClickListener(listener);
        getNoDataView().showErrorView(errorDesc);
    }

    public void  showNoNetworkView(View.OnClickListener listener) {
        getNoDataView().setOnClickListener(listener);
        getNoDataView().showNoNetwork();
    }

    @Override
    protected void onStart() {
        super.onStart();
        TCAgent.onPageStart(this, getPackageName());
    }

    @Override
    protected void onStop() {
        super.onStop();
        TCAgent.onPageEnd(this, getPackageName());
    }

    @Override
    protected void onDestroy() {
        //移除布局变化监听
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getWindow().getDecorView().getViewTreeObserver().removeOnGlobalLayoutListener(mLayoutChangeListener);
        } else {
            getWindow().getDecorView().getViewTreeObserver().removeGlobalOnLayoutListener(mLayoutChangeListener);
        }
        acts.remove(getClass().getName());
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_LOGIN:
                if (resultCode == RESULT_CANCELED_CUSTOM && !(this instanceof MainActivity)) {
                    finish();
                }
                break;
            case REQUEST_CODE_SELECT_IMAGE: {
                if (RESULT_OK == resultCode) {
                    ImgUtil.cropImageUri(this, data.getData(), photoUri, aspectX, aspectY, outX, outY,
                            REQUEST_CODE_PHOTO_CROP);
                }
            }
            break;
            case REQUEST_CODE_TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    ImgUtil.cropImageUri(this, photoUri, aspectX, aspectY, outX, outY, REQUEST_CODE_PHOTO_CROP);
                }
                break;
        }
    }
}
