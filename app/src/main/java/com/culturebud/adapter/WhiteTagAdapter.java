package com.culturebud.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.culturebud.BaseApp;
import com.culturebud.R;
import com.culturebud.bean.Category;
import com.culturebud.bean.CategoryTag;
import com.culturebud.widget.FlowLayout;
import com.culturebud.widget.TagAdapter;

import java.util.List;

/**
 * Created by XieWei on 2017/3/25.
 */

public class WhiteTagAdapter extends TagAdapter<CategoryTag> {
    boolean isOne = false;
    int categoryType;

    public WhiteTagAdapter(int categoryType, boolean isOne) {
        this.categoryType = categoryType;
        this.isOne = isOne;
    }

    @Override
    public View getView(FlowLayout parent, int position, CategoryTag categoryTag) {
        View view;
        if (isOne) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_category_one_item, null);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_category_item, null);
        }
        TextView tvTag = (TextView) view.findViewById(R.id.tv_tag);
        tvTag.setText(categoryTag.getCategory() + "(" + categoryTag.getStatis() + ")");
        if (categoryTag.isSelected()) {
            tvTag.setTextColor(BaseApp.getInstance().getResources().getColor(R.color.title_font_white));
            tvTag.setBackgroundResource(R.drawable.circle_selected_bg);
        }
        return view;
    }

    public void doSelect(View view, int position, FlowLayout parent) {
        getTags().get(position).setSelected(true);
        notifyDataChanged();
    }

    public void unselectAll() {
        boolean hasChanged = false;
        for (CategoryTag categoryTag : getTags()) {
            if (categoryTag.isSelected()) {
                categoryTag.setSelected(false);
                hasChanged = true;
            }
        }
        if (hasChanged) {
            notifyDataChanged();
        }
    }

    public int getCategoryType() {
        return categoryType;
    }

    public void setData(List<Category> categories, int currType, String currCategory) {
        clearData();
        for (Category category : categories) {
            addTag(new CategoryTag(category, currType == categoryType && category.getCategory().equals(currCategory)));
        }
    }


}


