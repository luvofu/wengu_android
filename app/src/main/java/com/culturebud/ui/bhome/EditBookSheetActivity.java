package com.culturebud.ui.bhome;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.culturebud.BaseActivity;
import com.culturebud.R;
import com.culturebud.adapter.StringTagsAdapter;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.BookSheetDetail;
import com.culturebud.contract.BookSheetEditContract;
import com.culturebud.presenter.BookSheetEditPresenter;
import com.culturebud.ui.me.GeneralEditorActivity;
import com.culturebud.widget.SettingItemView;
import com.culturebud.widget.TagFlowLayout;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_EDIT_BOOK_SHEET_DESC;
import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_EDIT_BOOK_SHEET_NAME;
import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_EDIT_BOOK_SHEET_TAG;
import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_PHOTO_CROP;

/**
 * Created by XieWei on 2016/12/14.
 */

@PresenterInject(BookSheetEditPresenter.class)
public class EditBookSheetActivity extends BaseActivity<BookSheetEditContract.Presenter> implements BookSheetEditContract.View {
    private SimpleDraweeView sdvBookSheetCover;
    private SettingItemView sivBookSheetName;
    private SettingItemView sivBookSheetDesc;
    private RelativeLayout rlTags;
    private TagFlowLayout tflTags;
    private BookSheetDetail bookSheetDetail;
    private boolean hasChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_book_sheet);
        presenter.setView(this);
        showTitlebar();
        setTitle(R.string.edit_book_sheet_info);
        showBack();

        sdvBookSheetCover = obtainViewById(R.id.sdv_bs_cover);
        sivBookSheetName = obtainViewById(R.id.siv_bs_name);
        sivBookSheetDesc = obtainViewById(R.id.siv_bs_desc);
        rlTags = obtainViewById(R.id.rl_tags);
        tflTags = obtainViewById(R.id.tfl_tags);

        initData();
    }

    private void initData() {
        String sheetJson = getIntent().getStringExtra("bookSheet");
        if (TextUtils.isEmpty(sheetJson)) {
            finish();
            return;
        }
        bookSheetDetail = new Gson().fromJson(sheetJson, BookSheetDetail.class);
        sdvBookSheetCover.setImageURI(bookSheetDetail.getCover());
        sivBookSheetName.setRightInfo(bookSheetDetail.getName());
        sivBookSheetDesc.setRightInfo(bookSheetDetail.getDescription());
        String tagStr = bookSheetDetail.getTag();
        if (!TextUtils.isEmpty(tagStr)) {
            String[] tags = tagStr.split("\\|");
            StringTagsAdapter bstAdapter = new StringTagsAdapter(tags);
            tflTags.setAdapter(bstAdapter);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_add_cover:
                showPhotoDialog();
                break;
            case R.id.siv_bs_name: {
                Intent intent = new Intent(this, GeneralEditorActivity.class);
                intent.putExtra("title", "书单名");
                intent.putExtra("content", bookSheetDetail.getName());
                intent.putExtra("hint", "书单名（不超过15个字符）");
                intent.putExtra("content_length", 15);
                startActivityForResult(intent, REQUEST_CODE_EDIT_BOOK_SHEET_NAME);
                break;
            }
            case R.id.siv_bs_desc: {
                Intent intent = new Intent(this, GeneralEditorActivity.class);
                intent.putExtra("title", "书单介绍");
                intent.putExtra("content", bookSheetDetail.getDescription());
                intent.putExtra("hint", "书单介绍");
                intent.putExtra("content_length", 500);
                intent.putExtra("type", 2);
                startActivityForResult(intent, REQUEST_CODE_EDIT_BOOK_SHEET_DESC);
                break;
            }
            case R.id.rl_tags: {
                Intent intent = new Intent(this, GeneralAddTagsActivity.class);
                if (!TextUtils.isEmpty(bookSheetDetail.getTag())) {
                    intent.putExtra("tag", bookSheetDetail.getTag());
                }
                intent.putExtra("type", 1);
                startActivityForResult(intent, REQUEST_CODE_EDIT_BOOK_SHEET_TAG);
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
//            case REQUEST_CODE_SELECT_IMAGE:
//                if (resultCode == RESULT_OK) {
//                    sdvBookSheetCover.setImageURI(photoUri);
//                }
//                break;
            case REQUEST_CODE_PHOTO_CROP:
                if (resultCode == RESULT_OK) {
                    sdvBookSheetCover.setImageURI(photoUri);
                    presenter.editCover(photoUri, bookSheetDetail.getSheetId());
                }
                break;
            case REQUEST_CODE_EDIT_BOOK_SHEET_NAME:
                if (resultCode == RESULT_OK) {
                    String content = data.getStringExtra("content");
                    sivBookSheetName.setRightInfo(content);
                    presenter.editBookSheet(bookSheetDetail.getSheetId(), content, null, null);
                }
                break;
            case REQUEST_CODE_EDIT_BOOK_SHEET_DESC:
                if (resultCode == RESULT_OK) {
                    String content = data.getStringExtra("content");
                    sivBookSheetDesc.setDesc(content);
                    presenter.editBookSheet(bookSheetDetail.getSheetId(), null, content, null);
                }
                break;
            case REQUEST_CODE_EDIT_BOOK_SHEET_TAG:
                if (resultCode == RESULT_OK) {
                    String tag = data.getStringExtra("tag");
                    String[] tarr = tag.split("\\|");
                    List<String> tmp = new ArrayList<>();
                    if (tarr != null) {
                        for (String s : tarr) {
                            tmp.add(s);
                        }
                    }
                    tflTags.getAdapter().clearData();
                    tflTags.getAdapter().addTags(tmp);
                    presenter.editBookSheet(bookSheetDetail.getSheetId(), null, null, tag);
                }
                break;
        }
    }

    @Override
    public void onEdit(boolean success) {
        if (!hasChanged) {
            if (success) {
                hasChanged = true;
                Intent data = new Intent();
                data.putExtra("has_changed", hasChanged);
                setResult(RESULT_OK, data);
            }
        }
    }
}
