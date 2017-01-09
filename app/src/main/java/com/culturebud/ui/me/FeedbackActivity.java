package com.culturebud.ui.me;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.culturebud.BaseActivity;
import com.culturebud.R;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.contract.FeedbackContract;
import com.culturebud.presenter.FeedbackPresenter;

/**
 * Created by XieWei on 2016/11/1.
 */

@PresenterInject(FeedbackPresenter.class)
public class FeedbackActivity extends BaseActivity<FeedbackContract.Presenter>
        implements FeedbackContract.View {
    private EditText etQuestions, etContact;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);
        presenter.setView(this);
        showTitlebar();
        setTitle(R.string.feedback_idea);
        setBackGroundColor(Color.WHITE);
        etQuestions = obtainViewById(R.id.et_feedback_content);
        etContact = obtainViewById(R.id.et_contact);
        btnSubmit = obtainViewById(R.id.btn_submit);
        setListeners();
    }

    private void setListeners() {
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_submit:
                presenter.feedback(etQuestions.getText().toString(), etContact.getText().toString());
                break;
        }
    }

    @Override
    protected void onBack() {
        super.onBack();
        finish();
    }

    @Override
    public void onFeedback(String tips) {
        if (!TextUtils.isEmpty(tips)) {
            Toast.makeText(this, tips, Toast.LENGTH_SHORT).show();
        } else {
            finish();
        }
    }
}
