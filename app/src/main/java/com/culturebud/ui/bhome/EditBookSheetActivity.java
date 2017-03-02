package com.culturebud.ui.bhome;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.culturebud.BaseActivity;
import com.culturebud.R;
import com.culturebud.adapter.StringTagsAdapter;
import com.culturebud.bean.BookSheetDetail;
import com.culturebud.widget.SettingItemView;
import com.culturebud.widget.TagFlowLayout;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;

/**
 * Created by XieWei on 2016/12/14.
 */

public class EditBookSheetActivity extends BaseActivity {
    private SimpleDraweeView sdvBookSheetCover;
    private SettingItemView sivBookSheetName;
    private SettingItemView sivBookSheetDesc;
    private RelativeLayout rlTags;
    private TagFlowLayout tflTags;
    private BookSheetDetail bookSheetDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_book_sheet);
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
            case R.id.siv_bs_name:

                break;
            case R.id.siv_bs_desc:

                break;
            case R.id.rl_tags:

                break;
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
                    sdvBookSheetCover.setImageURI(photoUri);
                }
                break;
            case REQUEST_CODE_PHOTO_CROP:
                if (resultCode == RESULT_OK) {
                    sdvBookSheetCover.setImageURI(photoUri);
                }
                break;
        }
    }
}
