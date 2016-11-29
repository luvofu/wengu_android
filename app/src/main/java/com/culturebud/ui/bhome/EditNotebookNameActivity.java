package com.culturebud.ui.bhome;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.culturebud.BaseActivity;
import com.culturebud.R;

/**
 * Created by XieWei on 2016/11/23.
 */

public class EditNotebookNameActivity extends BaseActivity {
    private EditText etNotebookName;
    private String notebookName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_notebook_name);
        showTitlebar();
        setTitle("编辑笔记本名");
        setOperasText("确定");
        etNotebookName = obtainViewById(R.id.et_notebook_name);
        notebookName = getIntent().getStringExtra("notebookName");
        if (!TextUtils.isEmpty(notebookName)) {
            etNotebookName.setText(notebookName);
            etNotebookName.setSelection(notebookName.length());
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_operas:
                if (etNotebookName.getText().toString().equals(notebookName)) {
                    finish();
                } else {
                    getIntent().putExtra("notebookName", etNotebookName.getText().toString());
                    setResult(RESULT_OK, getIntent());
                    finish();
                }
                break;
        }
    }

    @Override
    protected void onBack() {
        super.onBack();
        finish();
    }
}
