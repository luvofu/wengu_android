package com.culturebud.ui.bhome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.culturebud.BaseActivity;
import com.culturebud.R;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.contract.CreateBookSheetContract;
import com.culturebud.presenter.CreateBookSheetPresenter;
import com.facebook.drawee.view.SimpleDraweeView;

import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_PHOTO_CROP;
import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_SELECT_IMAGE;

/**
 * Created by XieWei on 2016/12/14.
 */

@PresenterInject(CreateBookSheetPresenter.class)
public class CreateBookSheetActivity extends BaseActivity<CreateBookSheetContract.Presenter>
        implements CreateBookSheetContract.View {
    private EditText etSheetName, etSheetDesc;
    private SimpleDraweeView sdvBookSheetCover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_book_sheet);
        presenter.setView(this);
        showTitlebar();
        setTitle(R.string.create_book_sheet);
        showBack();
        showOperas();
        setOperasText(R.string.confirm);

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
                showPhotoDialog();
                break;
        }
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

    @Override
    public void onCreateSuccess(int sheetId) {
        Intent data = new Intent();
        data.putExtra("sheetId", sheetId);
        setResult(RESULT_OK, data);
        finish();
    }
}
