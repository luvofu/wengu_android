package com.culturebud.ui.me;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;

import com.culturebud.BaseActivity;
import com.culturebud.R;

/**
 * Created by XieWei on 2016/11/16.
 */

public class GeneralEditorActivity extends BaseActivity {
    private EditText etInput;
    private String content;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alter_user_info);
        etInput = obtainViewById(R.id.et_alter_user_info);
        showTitlebar();
        setOperasText(R.string.confirm);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        setTitle(title);
        content = intent.getStringExtra("content");
        type = intent.getIntExtra("type", -1);
        if (content != null) {
            etInput.setText(content);
            etInput.setSelection(content.length());
        }
        switch (type) {
            case 0://昵称
                break;
            case 1://邮箱
                etInput.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                break;
            case 2://签名
                etInput.setMinLines(6);
                etInput.setGravity(Gravity.TOP | Gravity.LEFT);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_operas:
                String tmp = etInput.getText().toString();
                if (!tmp.equals(content)) {
                    Intent intent = new Intent();
                    intent.putExtra("content", tmp);
                    setResult(RESULT_OK, intent);
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
