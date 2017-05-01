package com.culturebud.ui.bhome;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.LinearLayout;

import com.culturebud.BaseActivity;
import com.culturebud.R;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.Note;
import com.culturebud.contract.NoteContract;
import com.culturebud.presenter.NotePresenter;
import com.culturebud.widget.SettingItemView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;

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
    private SettingItemView sivOther;
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
        sivOther = obtainViewById(R.id.siv_note_other);
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
            sivContent.setRightInfo(note.getContent());
        } else {
            sivContent.setRightInfo("");
        }
        if (!TextUtils.isEmpty(note.getChapter())) {
            sivChapter.setRightInfo(note.getChapter());
        } else {
            sivChapter.setRightInfo("");
        }
        int page = note.getPages() >= 0 ? note.getPages() : 0;
        sivPage.setRightInfo(String.valueOf(page));
        if (!TextUtils.isEmpty(note.getOtherLocation())) {
            sivOther.setRightInfo(note.getOtherLocation());
        } else {
            sivOther.setRightInfo("");
        }

    }

    @Override
    public void onNoteOpera(boolean res, int operaType) {
        if (res && NoteContract.View.OPERA_TYPE_EDIT == operaType) {

        }
    }

    @Override
    protected void onBack() {
        super.onBack();
        finish();
    }
}
