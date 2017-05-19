package com.culturebud.ui.bhome;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.culturebud.BaseActivity;
import com.culturebud.CommonConst;
import com.culturebud.R;
import com.culturebud.TextEditorFragment;
import com.culturebud.adapter.StringTagsAdapter;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.BookSheetDetail;
import com.culturebud.contract.BookSheetEditContract;
import com.culturebud.presenter.BookSheetEditPresenter;
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
public class EditBookSheetActivity extends BaseActivity<BookSheetEditContract.Presenter> implements BookSheetEditContract.View, TextEditorFragment.OnFragmentInteractionListener {
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
        sivBookSheetDesc.setRightInfo("");
        sivBookSheetDesc.setDesc(bookSheetDetail.getDescription());
        String tagStr = bookSheetDetail.getTag();
        if (!TextUtils.isEmpty(tagStr)) {
            String[] tags = tagStr.split("\\|");
            StringTagsAdapter bstAdapter = new StringTagsAdapter(tags);
            tflTags.setAdapter(bstAdapter);
        } else {
            tflTags.setAdapter(new StringTagsAdapter());
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_add_cover:
                aspectX = 1;
                aspectY = 1;
                outX = 720;
                outY = 720;
                showPhotoDialog();
                break;
            case R.id.siv_bs_name: {
                getFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.editbooksheetcontainview, TextEditorFragment.newInstance(REQUEST_CODE_EDIT_BOOK_SHEET_NAME,
                                "书单名", bookSheetDetail.getName(),
                                "书单名", 15, CommonConst.TextEditorInputType.DEFAULT_INPUT_TYPE,
                                false, null), TextEditorFragment.getFragmentTag()).commit();

                hideTitlebar();

                break;
            }
            case R.id.siv_bs_desc: {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                TextEditorFragment fragment = TextEditorFragment.newInstance(REQUEST_CODE_EDIT_BOOK_SHEET_DESC,
                        "书单介绍", bookSheetDetail.getDescription(),
                        "书单介绍", 500, CommonConst.TextEditorInputType.MULTI_LINE_INPUT_TYPE,
                        false, null);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.editbooksheetcontainview, fragment, TextEditorFragment.getFragmentTag()).commit();

                hideTitlebar();

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
    public void onBackPressed() {
        if (TextEditorFragment.isShowing(this)) {
            //移除.
            onExist();
        } else {
            finish();
        }
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
                    presenter.editCover(photoUri, bookSheetDetail.getSheetId());
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
                    presenter.editBookSheet(bookSheetDetail.getSheetId(), null, null, tag, REQUEST_CODE_EDIT_BOOK_SHEET_TAG);
                }
                break;
        }
    }


    @Override
    public void onConfirmSubmission(String inputString, int requestCode) {
        switch (requestCode) {
            case REQUEST_CODE_EDIT_BOOK_SHEET_NAME: {
                presenter.editBookSheet(bookSheetDetail.getSheetId(), inputString, null, null, REQUEST_CODE_EDIT_BOOK_SHEET_NAME);
                break;
            }
            case REQUEST_CODE_EDIT_BOOK_SHEET_DESC: {
                presenter.editBookSheet(bookSheetDetail.getSheetId(), null, inputString, null, REQUEST_CODE_EDIT_BOOK_SHEET_DESC);
                break;
            }
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

    @Override
    public void onEdit(boolean success, String content, int requestcode) {
        if (success) {
            switch (requestcode) {
                case REQUEST_CODE_EDIT_BOOK_SHEET_NAME: {
                    sivBookSheetName.setRightInfo(content);
                    break;
                }
                case REQUEST_CODE_EDIT_BOOK_SHEET_DESC: {
                    sivBookSheetDesc.setDesc(content);
                    break;
                }
            }

            hasChanged = true;
            Intent data = new Intent();
            data.putExtra("has_changed", hasChanged);
            setResult(RESULT_OK, data);

            //移除.
            onExist();
        }

        if (!hasChanged) {
            hasChanged = true;
            Intent data = new Intent();
            data.putExtra("has_changed", hasChanged);
            setResult(RESULT_OK, data);
        }
    }

    @Override
    public void onCoverEdit(String coverurl) {
        if (!TextUtils.isEmpty(coverurl)) {
            sdvBookSheetCover.setImageURI(photoUri);
        }
    }
}
