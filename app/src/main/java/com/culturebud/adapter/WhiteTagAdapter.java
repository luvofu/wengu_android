package com.culturebud.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.culturebud.R;
import com.culturebud.bean.BookCategoryGroup;
import com.culturebud.widget.FlowLayout;
import com.culturebud.widget.TagAdapter;

import java.util.List;

/**
 * Created by XieWei on 2017/3/25.
 */

public class WhiteTagAdapter extends TagAdapter<BookCategoryGroup.Category> {

    public WhiteTagAdapter(List<BookCategoryGroup.Category> datas) {
        super(datas);
    }

    @Override
    public View getView(FlowLayout parent, int position, BookCategoryGroup.Category category) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_category_item, null);
        TextView tvTag = (TextView) view;
        tvTag.setText(category.getCategory() + "(" + category.getStatistics() + ")");
        return view;
    }
}
