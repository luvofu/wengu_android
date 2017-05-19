package com.culturebud.ui.bhome;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.culturebud.BaseActivity;
import com.culturebud.CommonConst;
import com.culturebud.R;
import com.culturebud.TextEditorFragment;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.Note;
import com.culturebud.contract.NoteContract;
import com.culturebud.presenter.NotePresenter;
import com.culturebud.util.TxtUtil;
import com.culturebud.widget.SettingItemView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;

import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_EDIT_NOTE_CHAPTER;
import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_EDIT_NOTE_CONTNET;
import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_EDIT_NOTE_PAGE;
import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_PHOTO_CROP;

/**
 * Created by XieWei on 2016/11/25.
 */

@PresenterInject(NotePresenter.class)
public class EditNoteActivity extends BaseActivity<NoteContract.Presenter> implements NoteContract.View, TextEditorFragment.OnFragmentInteractionListener {
    private Note note;
    private SimpleDraweeView sdvNoteImg;
    private SettingItemView sivContent;
    private SettingItemView sivChapter;
    private SettingItemView sivPage;
    //    private SettingItemView sivOther;
    private LinearLayout llEditImg;

    private String currentEditItemContent = null;

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
                aspectX = 0;
                aspectY = 0;
                outX = 0;
                outY = 0;
                showPhotoDialog();
                break;
            case R.id.siv_note_content: {
                getFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.editnotecontainview, TextEditorFragment.newInstance(REQUEST_CODE_EDIT_NOTE_CONTNET,
                                sivContent.getName(), note.getContent(),
                                "笔记内容（不超过1000个字符）", 1000, CommonConst.TextEditorInputType.MULTI_LINE_INPUT_TYPE,
                                false, null), TextEditorFragment.getFragmentTag()).commit();

                hideTitlebar();

                break;
            }
            case R.id.siv_note_chapter: {
                getFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.editnotecontainview, TextEditorFragment.newInstance(REQUEST_CODE_EDIT_NOTE_CHAPTER,
                                "章节", note.getChapter(),
                                "", 15, CommonConst.TextEditorInputType.DEFAULT_INPUT_TYPE,
                                false, null), TextEditorFragment.getFragmentTag()).commit();

                hideTitlebar();

                break;
            }
            case R.id.siv_note_pages: {
                getFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.editnotecontainview, TextEditorFragment.newInstance(REQUEST_CODE_EDIT_NOTE_PAGE,
                                "页码", String.valueOf(note.getPages()),
                                "", 5, CommonConst.TextEditorInputType.NUMBER_INPUT_TYPE,
                                false, null), TextEditorFragment.getFragmentTag()).commit();

                hideTitlebar();
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
        }
    }

    @Override
    public void onNoteOpera(boolean res, int operaType) {
        if (res && !TextUtils.isEmpty(currentEditItemContent)) {
            //因为此处传的opeatype不再是编辑类型，而是具体的请求码，所以此处当请求码来处理.
            int requestcode = operaType;
            switch (requestcode) {
                case REQUEST_CODE_EDIT_NOTE_CONTNET:
                    sivContent.setDesc(currentEditItemContent);
                    break;
                case REQUEST_CODE_EDIT_NOTE_CHAPTER:
                    sivChapter.setRightInfo(currentEditItemContent);
                    break;
                case REQUEST_CODE_EDIT_NOTE_PAGE:

                    if (!TxtUtil.isValidateNumber(currentEditItemContent)) {
                        //不合法.
                        onErrorTip("请输入合法的页码");
                        return;
                    }

                    sivPage.setRightInfo(currentEditItemContent);
                    break;
            }

            setResult(RESULT_OK);

            onExist();
        }
    }

    @Override
    protected void onBack() {
        super.onBack();

        finish();
    }

    @Override
    public void onBackPressed() {
        if (TextEditorFragment.isShowing(this)) {
            //移除.
            onExist();
        } else {
            finish();
        }
    }

    @Override
    public void onConfirmSubmission(String content, int requestCode) {
        currentEditItemContent = content;
        switch (requestCode) {
            case REQUEST_CODE_EDIT_NOTE_CONTNET:
                presenter.editNote(note.getNoteId(), content, null, -1, null, requestCode);
                break;
            case REQUEST_CODE_EDIT_NOTE_CHAPTER:
                presenter.editNote(note.getNoteId(), null, content, -1, null, requestCode);
                break;
            case REQUEST_CODE_EDIT_NOTE_PAGE:
                presenter.editNote(note.getNoteId(), null, null, Integer.parseInt(content), null, requestCode);
                break;
        }
    }

    @Override
    public void onExist() {
        //退出.
        FragmentManager fragmentManager = getFragmentManager();
        android.app.Fragment fragment = fragmentManager.findFragmentByTag(TextEditorFragment.getFragmentTag());
        if (fragment != null) {
            //移除.
            fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).remove
                    (fragment).commit();

            //显示activity的title
            showTitlebar();
        }
    }


}
