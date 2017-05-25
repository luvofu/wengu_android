package com.culturebud.adapter;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.culturebud.R;
import com.culturebud.vo.Tag;
import com.culturebud.widget.TagAdapter;
import com.culturebud.widget.TagView;

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
    public View getView(TagView parent, int position, Tag tag) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_editable_tag, parent);
        EditText etTag = (EditText) view.findViewById(R.id.et_tag);
        view.setTag(tag);
        if (tag.getContent() == null || tag.getContent().isEmpty()) {
            etTag.setBackgroundResource(R.drawable.black_circle_bg_dark);
            etTag.setCursorVisible(true);
            etTag.setHint("输入标签");
            etTag.setSingleLine();
            etTag.setInputType(EditorInfo.TYPE_CLASS_TEXT);
            etTag.setOnClickListener(null);
            etTag.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI | EditorInfo.IME_ACTION_DONE);
            etTag.setOnEditorActionListener(this);
        } else {
            etTag.setBackgroundResource(R.drawable.blue_circle_bg_dark);
            etTag.setText(tag.getContent());
            etTag.setHint("");
            etTag.setInputType(EditorInfo.TYPE_NULL);
            etTag.setCursorVisible(false);
            etTag.setOnClickListener(this);
            etTag.setOnEditorActionListener(null);
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
                if (getCount() < 3) {
                    Tag t = new Tag();
                    t.setContent(v.getText().toString());
                    addTag(t, getCount() - 1);
                    if (onTagClickListener != null) {
                        onTagClickListener.onTagAdded(v, t);
                    }
                } else {
                    Tag t = (Tag) v.getTag();
                    t.setContent(v.getText().toString());
                    if (onTagClickListener != null) {
                        onTagClickListener.onTagAdded(v, t);
                    }
                }
                notifyDataChanged();
                return true;
        }
        return false;
    }

    public interface OnTagClickListener {
        void onTagClick(View v, Tag tag);

        void onTagAdded(View v, Tag tag);
    }

}
