package com.culturebud.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.culturebud.BaseApp;
import com.culturebud.R;
import com.culturebud.bean.Category;
import com.culturebud.bean.CategoryTag;
import com.culturebud.util.SystemParameterUtil;
import com.culturebud.widget.FlowLayout;
import com.culturebud.widget.TagAdapter;
import com.culturebud.widget.TagView;

import java.util.List;

/**
 * Created by XieWei on 2017/3/25.
 */

public class WhiteTagAdapter extends TagAdapter<CategoryTag> {
    boolean singleTag = false;
    int categoryType;

    public WhiteTagAdapter(int categoryType, boolean singleTag) {
        this.categoryType = categoryType;
        this.singleTag = singleTag;
    }

    @Override
    public View getView(TagView parent, int position, CategoryTag categoryTag) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_category_item, parent);
        TextView tvTag = (TextView) view.findViewById(R.id.tv_tag);

        tvTag.setText(categoryTag.getCategory() + "(" + categoryTag.getStatis() + ")");

        if (singleTag) {
            tvTag.setTextSize(14);
            int padding = (int) (12 * SystemParameterUtil.getDeviceDensity());
            tvTag.setPadding(padding, padding, padding, padding);
            tvTag.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
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


