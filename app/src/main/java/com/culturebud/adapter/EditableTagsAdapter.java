package com.culturebud.adapter;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputContentInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.culturebud.R;
import com.culturebud.vo.Tag;
import com.culturebud.widget.FlowLayout;
import com.culturebud.widget.TagAdapter;

import java.util.List;

/**
 * Created by XieWei on 2017/4/8.
 */

public class EditableTagsAdapter extends TagAdapter<Tag> implements View.OnClickListener, TextView
        .OnEditorActionListener {

    public boolean delByTag(String tag) {
        if (TextUtils.isEmpty(tag)) {
            return false;
        }
        List<Tag> tags = getTags();
        for (Tag t : tags) {
            if (tag.equals(t.getContent())) {
                delTag(t);
                return true;
            }
        }
        return false;
    }

    @Override
    public View getView(FlowLayout parent, int position, Tag tag) {
        EditText view = (EditText) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_add_editable_tag, null);
        view.setTag(tag);
        if (tag.getContent() == null || tag.getContent().isEmpty()) {
            view.setBackgroundResource(R.drawable.black_circle_bg_dark);
            view.setCursorVisible(true);
            view.setHint("输入标签");
            view.setSingleLine();
            view.setInputType(EditorInfo.TYPE_CLASS_TEXT);
            view.setOnClickListener(null);
            view.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI | EditorInfo.IME_ACTION_DONE);
            view.setOnEditorActionListener(this);
        } else {
            view.setBackgroundResource(R.drawable.blue_circle_bg_dark);
            view.setText(tag.getContent());
            view.setHint("");
            view.setInputType(EditorInfo.TYPE_NULL);
            view.setCursorVisible(false);
            view.setOnClickListener(this);
            view.setOnEditorActionListener(null);
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        if (onTagClickListener != null) {
            onTagClickListener.onTagClick(v, (Tag) v.getTag());
        }
    }

    private OnTagClickListener onTagClickListener;

    public OnTagClickListener getOnTagClickListener() {
        return onTagClickListener;
    }

    public void setOnTagClickListener(OnTagClickListener onTagClickListener) {
        this.onTagClickListener = onTagClickListener;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch (actionId) {
            case EditorInfo.IME_ACTION_DONE:
            case EditorInfo.IME_ACTION_NONE:
                Tag t = (Tag) v.getTag();
                t.setContent(v.getText().toString());
                notifyDataChanged();
                return true;
        }
        return false;
    }

    public interface OnTagClickListener {
        void onTagClick(View v, Tag tag);
    }
}
