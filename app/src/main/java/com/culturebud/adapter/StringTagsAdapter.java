package com.culturebud.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.culturebud.R;
import com.culturebud.widget.TagAdapter;
import com.culturebud.widget.TagView;

import java.util.List;

/**
 * Created by XieWei on 2016/11/8.
 */

public class StringTagsAdapter extends TagAdapter<String> {

    public StringTagsAdapter(List<String> data) {
        super(data);
    }

    public StringTagsAdapter(String... data) {
        super(data);
    }

    @Override
    public void setTagView(TagView parent, int position, String s) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_sheet_tag_item, parent);
        TextView tvTag = (TextView) view.findViewById(R.id.tv_tag);
        tvTag.setText(s);
    }
}
