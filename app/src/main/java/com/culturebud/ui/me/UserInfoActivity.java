package com.culturebud.ui.me;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.BottomSheetDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.culturebud.BaseActivity;
import com.culturebud.BaseApp;
import com.culturebud.R;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.User;
import com.culturebud.contract.UserInfoContract;
import com.culturebud.presenter.UserInfoPresenter;
import com.culturebud.util.WidgetUtil;
import com.culturebud.widget.SettingItemView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.UUID;

/**
 * Created by XieWei on 2016/11/2.
 */

@PresenterInject(UserInfoPresenter.class)
public class UserInfoActivity extends BaseActivity<UserInfoContract.Presenter>
        implements UserInfoContract.View {
    private static final String TAG = UserInfoActivity.class.getSimpleName();
    private static final int REQUEST_CODE_ALTER_NICK = 101;
    private static final int REQUEST_CODE_ALTER_EMAIL = 102;
    private static final int REQUEST_CODE_ALTER_PROFILE = 103;
    private SimpleDraweeView sdvFace;
    private LinearLayout llFace;
    private SettingItemView sivNick, sivCulturebudName, sivSex, sivEmail, sivRegion, sivProfile;
    private BottomSheetDialog editImgDialog;
    private TextView tvAlbum, tvPhoto, tvCancel;
    private Uri imgUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info);
        presenter.setView(this);
        showTitlebar();
        llFace = obtainViewById(R.id.ll_face);
        sdvFace = obtainViewById(R.id.sdv_face);
        sivNick = obtainViewById(R.id.siv_nick);
        sivCulturebudName = obtainViewById(R.id.siv_culturebud_name);
        sivSex = obtainViewById(R.id.siv_sex);
        sivEmail = obtainViewById(R.id.siv_email);
        sivRegion = obtainViewById(R.id.siv_region);
        sivProfile = obtainViewById(R.id.siv_profile);
        setTitle(getString(R.string.user_info));
        setListeners();

        initData();
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

    private void setListeners() {
        llFace.setOnClickListener(this);
        sivNick.setOnClickListener(this);
        sivSex.setOnClickListener(this);
        sivEmail.setOnClickListener(this);
        sivRegion.setOnClickListener(this);
        sivProfile.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initData() {
        User user = BaseApp.getInstance().getUser();
        if (null != user) {
            String avatar = user.getAvatar();
            if (!TextUtils.isEmpty(avatar)) {
                sdvFace.setImageURI(avatar);
            }
            String nick = user.getNickname();
            if (!TextUtils.isEmpty(nick)) {
                sivNick.setRightInfo(nick);
            } else {
                sivNick.setRightInfo(R.string.no_fill);
            }
            String culturebudName = user.getUserName();
            if (!TextUtils.isEmpty(culturebudName)) {
                sivCulturebudName.setRightInfo(culturebudName);
            } else {
                sivCulturebudName.setRightInfo(R.string.no_fill);
            }
            int sex = user.getSex();
            sivSex.setRightInfo(sex == 0 ? "男" : "女");
            String email = user.getMailbox();
            if (!TextUtils.isEmpty(email)) {
                sivEmail.setRightInfo(email);
            } else {
                sivEmail.setRightInfo(R.string.no_fill);
            }
            String region = user.getCity();
            if (!TextUtils.isEmpty(region)) {
                sivRegion.setRightInfo(region);
            } else {
                sivRegion.setRightInfo(R.string.no_fill);
            }
            String profile = user.getAutograph();
            if (!TextUtils.isEmpty(profile)) {
                sivProfile.setDesc(profile);
            } else {
                sivProfile.setDesc(R.string.no_fill);
            }
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_face:
                initEditImgDialog();
                editImgDialog.show();
                break;
            case R.id.tv_opera_content://相册
            {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
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
                    imgUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
                    startActivityForResult(cameraIntent, REQUEST_CODE_TAKE_PHOTO);
                }
                editImgDialog.dismiss();
                break;
            }
            case R.id.tv_cancel://取消
                editImgDialog.dismiss();
                break;
            case R.id.siv_nick://修改昵称
            {
                Intent intent = new Intent(this, AlterUserInfoActivity.class);
                intent.putExtra("title", "昵称");
                intent.putExtra("content", BaseApp.getInstance().getUser().getNickname());
                intent.putExtra("type", 0);
                startActivityForResult(intent, REQUEST_CODE_ALTER_NICK);
                break;
            }
            case R.id.siv_email://修改邮箱
            {
                Intent intent = new Intent(this, AlterUserInfoActivity.class);
                intent.putExtra("title", "邮箱");
                intent.putExtra("content", BaseApp.getInstance().getUser().getMailbox());
                intent.putExtra("type", 1);
                startActivityForResult(intent, REQUEST_CODE_ALTER_EMAIL);
                break;
            }
            case R.id.siv_profile://修改个性签名
            {
                Intent intent = new Intent(this, AlterUserInfoActivity.class);
                intent.putExtra("title", "修改签名");
                intent.putExtra("content", BaseApp.getInstance().getUser().getAutograph());
                intent.putExtra("type", 2);
                startActivityForResult(intent, REQUEST_CODE_ALTER_PROFILE);
                break;
            }
        }
    }

    @Override
    protected void onBack() {
        super.onBack();
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_SELECT_IMAGE:
                if (resultCode == RESULT_OK) {
                    imgUri = data.getData();
                    Log.d(TAG, "image url is : " + imgUri.toString());
                    Log.d(TAG, "image url is : " + (imgUri.getHost() + imgUri.getPath()));
                    sdvFace.setImageURI(imgUri);
                    User user = BaseApp.getInstance().getUser();
                    presenter.editAvatar(user.getUserId(), imgUri, false);
                }
                break;
            case REQUEST_CODE_TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    User user = BaseApp.getInstance().getUser();
                    presenter.editAvatar(user.getUserId(), imgUri, true);
                }
                break;
            case REQUEST_CODE_ALTER_NICK: {
                if (resultCode == RESULT_OK && data.hasExtra("content")) {
                    String content = data.getStringExtra("content");
                    sivNick.setRightInfo(content);
                    presenter.editNick(content);
                }
                break;
            }
            case REQUEST_CODE_ALTER_EMAIL: {
                if (resultCode == RESULT_OK && data.hasExtra("content")) {
                    String content = data.getStringExtra("content");
                    sivEmail.setRightInfo(content);
                    presenter.editEmail(content);
                }
                break;
            }
            case REQUEST_CODE_ALTER_PROFILE: {
                if (resultCode == RESULT_OK && data.hasExtra("content")) {
                    String content = data.getStringExtra("content");
                    sivProfile.setDesc(content);
                    presenter.editAutograph(content);
                }
                break;
            }
        }
    }

    @Override
    public void onEditImg(String newUrl) {
        sdvFace.setImageURI(newUrl);
    }

    @Override
    public void onNick(String nick) {

    }

    @Override
    public void onEmail(String email) {

    }

    @Override
    public void onAutograph(String autograph) {

    }
}
