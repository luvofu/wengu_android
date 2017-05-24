package com.culturebud.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.culturebud.BaseApp;
import com.culturebud.R;
import com.culturebud.bean.Category;
import com.culturebud.bean.CategoryTag;
import com.culturebud.widget.FlowLayout;
import com.culturebud.widget.TagAdapter;

import java.util.List;

import static com.culturebud.widget.TagFlowLayout.dip2px;

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
    public ViewGroup.MarginLayoutParams getLayoutParam(Context context) {
        ViewGroup.MarginLayoutParams lp;
        if (isOne) {
            lp = new ViewGroup.MarginLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        } else {
            lp = new ViewGroup.MarginLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            lp.setMargins(dip2px(context, 5), dip2px(context, 5), dip2px(context, 5), dip2px(context, 5));
        }
        return lp;
    }

    @Override
    public View getView(FlowLayout parent, int position, CategoryTag categoryTag) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_category_item, null);
        TextView tvTag = (TextView) view.findViewById(R.id.tv_tag);
        tvTag.setText(categoryTag.getCategory() + "(" + categoryTag.getStatis() + ")");
        if (isOne) {
            tvTag.setTextSize(14);
            tvTag.setPadding(36, 36, 36, 36);
        }
        if (categoryTag.isSelected()) {
            tvTag.setTextColor(BaseApp.getInstance().getResources().getColor(R.color.title_font_white));
            tvTag.setBackgroundResource(R.drawable.circle_selected_bg);
        }
        return tvTag;
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


