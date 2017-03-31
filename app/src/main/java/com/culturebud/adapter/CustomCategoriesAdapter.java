package com.culturebud.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.culturebud.R;
import com.culturebud.bean.Category;
import com.culturebud.util.WidgetUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XieWei on 2017/3/31.
 */

public class CustomCategoriesAdapter extends RecyclerView.Adapter<CustomCategoriesAdapter.AddCustomCategoryViewHolder> {
    private List<Category> data;
    private static final int ITEM_TYPE_ADD = 1;
    private static final int ITEM_TYPE_CATEGORY = 2;

    public CustomCategoriesAdapter() {
        data = new ArrayList<>();
    }

    public void clearData() {
        if (!data.isEmpty()) {
            data.clear();
            notifyDataSetChanged();
        }
    }

    public void addItems(List<Category> categories) {
        if (categories != null && !categories.isEmpty()) {
            int position = data.size() - 1;
            data.addAll(categories);
            notifyItemRangeChanged(position, categories.size());
        }
    }

    @Override
    public AddCustomCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (ITEM_TYPE_ADD == viewType) {
            return new AddCustomCategoryViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_custom_category_add, parent, false));
        } else {
            return new CustomCategoriesViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_custom_category, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(AddCustomCategoryViewHolder holder, int position) {
        if (holder.getClass() == AddCustomCategoryViewHolder.class) {

        } else {
            Category item = data.get(position);
            CustomCategoriesViewHolder ccHolder = (CustomCategoriesViewHolder) holder;
            ccHolder.setCategory(item.getCategory());
            ccHolder.setCount(item.getStatis());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == data.size()) {
            return ITEM_TYPE_ADD;
        } else {
            return ITEM_TYPE_CATEGORY;
        }
    }

    @Override
    public int getItemCount() {
        return data.size() + 1;
    }

    class AddCustomCategoryViewHolder extends RecyclerView.ViewHolder {
        public AddCustomCategoryViewHolder(View itemView) {
            super(itemView);
        }
    }


    class CustomCategoriesViewHolder extends AddCustomCategoryViewHolder {
        private TextView tvCategory, tvCount;

        public CustomCategoriesViewHolder(View itemView) {
            super(itemView);
            tvCategory = WidgetUtil.obtainViewById(itemView, R.id.tv_category);
            tvCount = WidgetUtil.obtainViewById(itemView, R.id.tv_count);
        }

        public void setCategory(String category) {
            if (!TextUtils.isEmpty(category)) {
                tvCategory.setText(category);
            }
        }

        public void setCount(int count) {
            tvCount.setText(String.valueOf(count));
        }
    }
}
