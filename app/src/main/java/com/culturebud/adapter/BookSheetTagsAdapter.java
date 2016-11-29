package com.culturebud.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.culturebud.R;
import com.culturebud.widget.FlowLayout;
import com.culturebud.widget.TagAdapter;

import java.util.List;

/**
 * Created by XieWei on 2016/11/8.
 */

public class BookSheetTagsAdapter extends TagAdapter<String> {

    public BookSheetTagsAdapter(List<String> data) {
        super(data);
    }

    public BookSheetTagsAdapter(String... data) {
        super(data);
    }

    @Override
    public View getView(FlowLayout parent, int position, String s) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_sheet_tag_item, null);
        TextView tvTag = (TextView) view;
        tvTag.setText(s);
        return view;
    }
}
