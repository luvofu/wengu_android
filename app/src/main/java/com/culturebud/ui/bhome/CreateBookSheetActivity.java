package com.culturebud.ui.bhome;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.BottomSheetDialog;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.culturebud.BaseActivity;
import com.culturebud.R;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.contract.CreateBookSheetContract;
import com.culturebud.presenter.CreateBookSheetPresenter;
import com.culturebud.util.WidgetUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.UUID;

/**
 * Created by XieWei on 2016/12/14.
 */

@PresenterInject(CreateBookSheetPresenter.class)
public class CreateBookSheetActivity extends BaseActivity<CreateBookSheetContract.Presenter>
        implements CreateBookSheetContract.View {
    private EditText etSheetName, etSheetDesc;
    private SimpleDraweeView sdvBookSheetCover;
    private BottomSheetDialog editImgDialog;
    private TextView tvAlbum, tvPhoto, tvCancel;
    private Uri photoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_book_sheet);
        presenter.setView(this);
        showTitlebar();
        setTitle("新建书单");
        showBack();
        showOperas();
        setOperasText("确定");

        sdvBookSheetCover = obtainViewById(R.id.sdv_bs_cover);
        etSheetName = obtainViewById(R.id.et_sheet_name);
        etSheetDesc = obtainViewById(R.id.et_sheet_desc);
    }

    @Override
    protected void onOptions(View view) {
        super.onOptions(view);
        presenter.createBookSheet(etSheetName.getText().toString(), etSheetDesc.getText().toString(), photoUri);
    }

    @Override
    protected void onBack() {
        super.onBack();
        finish();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_add_cover:
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_SELECT_IMAGE:
                if (resultCode == RESULT_OK) {
                    photoUri = data.getData();
                    sdvBookSheetCover.setImageURI(photoUri);
                }
                break;
            case REQUEST_CODE_TAKE_PHOTO:
                if (requestCode == RESULT_OK) {
                    sdvBookSheetCover.setImageURI(photoUri);
                }
                break;
        }
    }
}
