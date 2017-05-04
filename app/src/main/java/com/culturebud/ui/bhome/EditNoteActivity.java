package com.culturebud.ui.bhome;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.culturebud.BaseActivity;
import com.culturebud.R;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.Note;
import com.culturebud.contract.NoteContract;
import com.culturebud.presenter.NotePresenter;
import com.culturebud.ui.me.GeneralEditorActivity;
import com.culturebud.widget.SettingItemView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_EDIT_BOOK_SHEET_DESC;
import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_EDIT_BOOK_SHEET_NAME;
import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_EDIT_BOOK_SHEET_TAG;
import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_EDIT_NOTE_CHAPTER;
import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_EDIT_NOTE_CONTNET;
import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_EDIT_NOTE_PAGE;
import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_PHOTO_CROP;

/**
 * Created by XieWei on 2016/11/25.
 */

@PresenterInject(NotePresenter.class)
public class EditNoteActivity extends BaseActivity<NoteContract.Presenter> implements NoteContract.View {
    private Note note;
    private SimpleDraweeView sdvNoteImg;
    private SettingItemView sivContent;
    private SettingItemView sivChapter;
    private SettingItemView sivPage;
//    private SettingItemView sivOther;
    private LinearLayout llEditImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_note);
        presenter.setView(this);
        llEditImg = obtainViewById(R.id.ll_change_img);
        sdvNoteImg = obtainViewById(R.id.sdv_note_img);
        sivChapter = obtainViewById(R.id.siv_note_chapter);
        sivContent = obtainViewById(R.id.siv_note_content);
        sivPage = obtainViewById(R.id.siv_note_pages);
//        sivOther = obtainViewById(R.id.siv_note_other);
        showTitlebar();
        setTitle(R.string.edit_note);
        initData();
    }

    private void initData() {
        note = new Gson().fromJson(getIntent().getStringExtra("note"), Note.class);
        if (!TextUtils.isEmpty(note.getImage())) {
            sdvNoteImg.setImageURI(note.getImage());
        }
        if (!TextUtils.isEmpty(note.getContent())) {
//            sivContent.setRightInfo(note.getContent());
            sivContent.setDesc(note.getContent());
        } else {
            sivContent.setDesc("");
            sivContent.setRightInfo("");
        }
        if (!TextUtils.isEmpty(note.getChapter())) {
            sivChapter.setRightInfo(note.getChapter());
        } else {
            sivChapter.setRightInfo("");
        }
        int page = note.getPages() >= 0 ? note.getPages() : 0;
        sivPage.setRightInfo(String.valueOf(page));
//        if (!TextUtils.isEmpty(note.getOtherLocation())) {
//            sivOther.setRightInfo(note.getOtherLocation());
//        } else {
//            sivOther.setRightInfo("");
//        }

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {
            case R.id.ll_change_img:
                aspectX = 1;
                aspectY = 1;
                outX = 0;
                outY = 0;
                showPhotoDialog();
                break;
            case R.id.siv_note_content: {
                Intent intent = new Intent(this, GeneralEditorActivity.class);
                intent.putExtra("title", sivContent.getName());
                intent.putExtra("content", note.getContent());
                intent.putExtra("hint", "笔记内容（不超过1000个字符）");
                intent.putExtra("content_length", 1000);
                intent.putExtra("type", 2);
                startActivityForResult(intent, REQUEST_CODE_EDIT_NOTE_CONTNET);
                break;
            }
            case R.id.siv_note_chapter: {
                Intent intent = new Intent(this, GeneralEditorActivity.class);

                intent.putExtra("title", "章节");
                intent.putExtra("content", note.getChapter());
                intent.putExtra("hint", "");
                intent.putExtra("content_length", 15);

                startActivityForResult(intent, REQUEST_CODE_EDIT_NOTE_CHAPTER);

                break;
            }
            case R.id.siv_note_pages: {
                Intent intent = new Intent(this, GeneralEditorActivity.class);

                intent.putExtra("title", "页码");
                intent.putExtra("content", String.valueOf(note.getPages()));
                intent.putExtra("hint", "");
                intent.putExtra("content_length", 15);
                startActivityForResult(intent, REQUEST_CODE_EDIT_NOTE_PAGE);
                break;
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_PHOTO_CROP:
                if (resultCode == RESULT_OK) {
                    sdvNoteImg.setImageURI(photoUri);
                    presenter.editCover(photoUri, note.getNoteId());
                }
                break;
            case REQUEST_CODE_EDIT_NOTE_CONTNET:
                if (resultCode == RESULT_OK) {
                    String content = data.getStringExtra("content");
                    sivContent.setDesc(content);
                    presenter.editNote(note.getNoteId(), content, null,-1, null);
                }
                break;
            case REQUEST_CODE_EDIT_NOTE_CHAPTER:
                if (resultCode == RESULT_OK) {
                    String chapter = data.getStringExtra("content");
                    sivChapter.setRightInfo(chapter);
                    presenter.editNote(note.getNoteId(), null, chapter,-1, null);
                }
                break;
            case REQUEST_CODE_EDIT_NOTE_PAGE:
                if (resultCode == RESULT_OK) {
                    String page = data.getStringExtra("content");
                    sivPage.setRightInfo(page);
                    presenter.editNote(note.getNoteId(), null, null, Integer.parseInt(page), null);
                }
                break;
        }
    }

    @Override
    public void onNoteOpera(boolean res, int operaType) {
//        if (res && NoteContract.View.OPERA_TYPE_EDIT == operaType) {
//
//        }
    }

    @Override
    protected void onBack() {
        super.onBack();
        Intent intent = getIntent();
        setResult(RESULT_OK, intent);

        finish();
    }
}
