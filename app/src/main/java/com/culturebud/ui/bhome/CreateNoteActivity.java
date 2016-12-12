package com.culturebud.ui.bhome;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.BottomSheetDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.culturebud.BaseActivity;
import com.culturebud.R;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.contract.NoteContract;
import com.culturebud.presenter.NotePresenter;
import com.culturebud.util.WidgetUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Locale;
import java.util.UUID;

/**
 * Created by XieWei on 2016/11/23.
 */
@PresenterInject(NotePresenter.class)
public class CreateNoteActivity extends BaseActivity<NoteContract.Presenter> implements NoteContract.View {
    private long notebookId = -1;
    private EditText etNoteContent, etChapter, etPage, etOther;
    private SimpleDraweeView sdvAddImg;
    private ImageView ivDelImg;

    private BottomSheetDialog editImgDialog;
    private TextView tvAlbum, tvPhoto, tvCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_note);
        presenter.setView(this);
        showTitlebar();
        setTitle("写笔记");
        showOperas();
        setOperasText("确定");
        setOperasTextColor(getResources().getColor(R.color.title_font_white));
        etNoteContent = obtainViewById(R.id.et_note_content);
        etChapter = obtainViewById(R.id.et_chapter);
        etPage = obtainViewById(R.id.et_page);
        etOther = obtainViewById(R.id.et_other);
        sdvAddImg = obtainViewById(R.id.sdv_add_img);
        ivDelImg = obtainViewById(R.id.iv_del_img);

        Intent intent = getIntent();
        notebookId = intent.getLongExtra("notebookId", -1);
        String strTitle = intent.getStringExtra("notebookTitle");
        if (!TextUtils.isEmpty(strTitle)) {
            etNoteContent.setHint(String.format(Locale.getDefault(),
                    getString(R.string.hint_create_note_content), strTitle));
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

    private Uri photoUri;

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_operas:
                presenter.createNote(notebookId, etNoteContent.getText().toString(), etChapter.getText().toString(),
                        Integer.valueOf(TextUtils.isEmpty(etPage.getText().toString()) ? "-1" : etPage.getText().toString()),
                        etOther.getText().toString(), photoUri);
                break;
            case R.id.sdv_add_img:
                initEditImgDialog();
                editImgDialog.show();
                break;
            case R.id.iv_del_img:
                photoUri = null;
                sdvAddImg.setImageURI(photoUri);
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

    @Override
    protected void onBack() {
        super.onBack();
        finish();
    }

    @Override
    public void onNoteOpera(boolean res, int operaType) {
        if (res && operaType == NoteContract.View.OPERA_TYPE_CREATE) {
            Intent intent = getIntent();
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_SELECT_IMAGE:
                if (resultCode == RESULT_OK) {
                    photoUri = data.getData();
                    sdvAddImg.setImageURI(photoUri);
                }
                break;
            case REQUEST_CODE_TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    sdvAddImg.setImageURI(photoUri);
                }
                break;
        }
    }
}
