package com.culturebud.ui.bhome;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.culturebud.BaseActivity;
import com.culturebud.R;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.contract.BookSheetEditRecommendContract;
import com.culturebud.presenter.BookSheetEditRecommendPresenter;

/**
 * Created by XieWei on 2017/1/20.
 */

@PresenterInject(BookSheetEditRecommendPresenter.class)
public class BookSheetEditRecommendActivity extends BaseActivity<BookSheetEditRecommendContract.Presenter> implements BookSheetEditRecommendContract.View, TextWatcher {
    private EditText etRecommend;
    private TextView tvWordCount;
    private String originRecommend;
    private long sheetBookId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bs_edit_recommend);
        presenter.setView(this);
        etRecommend = obtainViewById(R.id.et_recommend);
        tvWordCount = obtainViewById(R.id.tv_word_count);
        etRecommend.addTextChangedListener(this);
        showTitlebar();
        showBack();
        showOperas();
        setTitle(R.string.recommend_reason);
        setBackText(R.string.cancel);
        setOperasText(R.string.confirm);
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        sheetBookId = intent.getLongExtra("sheet_book_id", -1);
        if (sheetBookId <= 0) {
            finish();
        }
        originRecommend = intent.getStringExtra("recommend");
        if (!TextUtils.isEmpty(originRecommend)) {
            etRecommend.setText(originRecommend);
            etRecommend.setSelection(etRecommend.getText().length());
        }
        tvWordCount.setText("" + (300 - etRecommend.getText().length()));
    }

    @Override
    protected void onBack() {
        super.onBack();
        finish();
    }

    @Override
    protected void onOptions(View view) {
        super.onOptions(view);
        presenter.editRecommend(sheetBookId, etRecommend.getText().toString());
    }

    @Override
    public void onEditRecommend(long sheetBookId, boolean result) {
        if (this.sheetBookId != sheetBookId) {
            return;
        }
        if (result) {
            Intent data = new Intent();
            data.putExtra("sheet_book_id", sheetBookId);
            data.putExtra("recommend", etRecommend.getText().toString());
            setResult(RESULT_OK, data);
            finish();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        tvWordCount.setText("" + (300 - etRecommend.getText().length()));
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
