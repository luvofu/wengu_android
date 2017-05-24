package com.culturebud.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.culturebud.BaseApp;
import com.culturebud.R;
import com.culturebud.bean.CategoryTag;
import com.culturebud.widget.FlowLayout;
import com.culturebud.widget.TagAdapter;

import java.util.List;

/**
 * Created by XieWei on 2017/3/25.
 */

public class WhiteTagAdapter extends TagAdapter<CategoryTag> {
    int categoryType;

    public WhiteTagAdapter(List<CategoryTag> datas, int categoryType) {
        super(datas);
        this.categoryType = categoryType;
    }

    @Override
    public View getView(FlowLayout parent, int position, CategoryTag categoryTag) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_category_item, null);
        TextView tvTag = (TextView) view.findViewById(R.id.tv_tag);
        tvTag.setText(categoryTag.getCategory() + "(" + categoryTag.getStatis() + ")");
        if (categoryTag.isSelected()) {
            tvTag.setTextColor(BaseApp.getInstance().getResources().getColor(R.color.title_font_white));
            tvTag.setBackgroundResource(R.drawable.circle_selected_bg);
        }
        return view;
    }

    public CategoryTag doSelected(View view, int position, FlowLayout parent) {
        getTags().get(position).setSelected(true);
        notifyDataChanged();
        return getTags().get(position);
    }

    public void disableAllSelected() {
        for (CategoryTag categoryTag : getTags()) {
            categoryTag.setSelected(false);
        }
        notifyDataChanged();
    }

    public int getCategoryType() {
        return categoryType;
    }

}
