package com.culturebud.ui.bhome;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.culturebud.BaseActivity;
import com.culturebud.R;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.CollectedBook;
import com.culturebud.contract.CreateNotebookContract;
import com.culturebud.presenter.CreateNotebookPresenter;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;

/**
 * Created by XieWei on 2016/11/21.
 */

@PresenterInject(CreateNotebookPresenter.class)
public class CreateNotebookActivity extends BaseActivity<CreateNotebookContract.Presenter> implements CreateNotebookContract.View {
    private static final String TAG = CreateNotebookActivity.class.getSimpleName();
    private static final int REQUEST_CODE_SELECT_BOOK = 202;
    private EditText etContent;
    private TextView tvSelectBook;
    private SimpleDraweeView sdvBookCover;
    private CollectedBook collectedBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_notebook);
        presenter.setView(this);
        showTitlebar();
        showOperas();
        setOperasText(R.string.confirm);
        setOperasTextColor(getResources().getColor(R.color.title_font_white));
        etContent = obtainViewById(R.id.et_notebook_name);
        tvSelectBook = obtainViewById(R.id.tv_select_book);
        sdvBookCover = obtainViewById(R.id.sdv_book_sheet_cover);
        tvSelectBook.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_select_book:
                Intent intent = new Intent(this, CollectedBooksActivity.class);
                intent.putExtra(CollectedBooksActivity.TYPE_KEY, CollectedBooksActivity.TYPE_SELECT);
                startActivityForResult(intent, REQUEST_CODE_SELECT_BOOK);
                break;
            case R.id.tv_operas:
                presenter.createNotebook(etContent.getText().toString(), collectedBook);
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
            case REQUEST_CODE_SELECT_BOOK:
                if (resultCode == RESULT_OK) {
                    String bookJson = data.getStringExtra("book");
                    if (!TextUtils.isEmpty(bookJson)) {
                        collectedBook = new Gson().fromJson(bookJson, CollectedBook.class);
                        if (!TextUtils.isEmpty(collectedBook.getTitle())) {
                            tvSelectBook.setGravity(Gravity.CENTER_VERTICAL);
                            tvSelectBook.setText(collectedBook.getTitle());
                            sdvBookCover.setVisibility(View.VISIBLE);
                            sdvBookCover.setImageURI(collectedBook.getCover());
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onCreateNotebook(boolean res, long bookId) {
        if (res && bookId == collectedBook.getBookId()) {
            setResult(RESULT_OK);
            finish();
        }
    }
}
