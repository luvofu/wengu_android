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

import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_PHOTO_CROP;
import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_SELECT_IMAGE;

/**
 * Created by XieWei on 2016/11/23.
 */
@PresenterInject(NotePresenter.class)
public class CreateNoteActivity extends BaseActivity<NoteContract.Presenter> implements NoteContract.View {
    private long notebookId = -1;
    private EditText etNoteContent, etChapter, etPage, etOther;
    private SimpleDraweeView sdvAddImg;
    private ImageView ivDelImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_note);
        presenter.setView(this);
        showTitlebar();
        setTitle(R.string.create_note);
        showOperas();
        setOperasText(R.string.confirm);
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
                showPhotoDialog();
                break;
            case R.id.iv_del_img:
                photoUri = null;
                sdvAddImg.setImageURI(photoUri);
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
                    sdvAddImg.setImageURI(photoUri);
                }
                break;
            case REQUEST_CODE_PHOTO_CROP:
                if (resultCode == RESULT_OK) {
                    sdvAddImg.setImageURI(photoUri);
                }
                break;
        }
    }
}
