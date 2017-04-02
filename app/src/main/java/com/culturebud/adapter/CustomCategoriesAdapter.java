package com.culturebud.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
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
    private static final int ITEM_TYPE_CATEGORY_SIMPLE = 3;
    private static final int MODEL_NORMAL = 1;
    private static final int MODEL_SIMPLE = 2;
    private int model = MODEL_NORMAL;

    public CustomCategoriesAdapter() {
        data = new ArrayList<>();
    }

    public void setAsDlg() {
        model = MODEL_SIMPLE;
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
        } else if(ITEM_TYPE_CATEGORY == viewType) {
            return new CustomCategoriesViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_custom_category, parent, false));
        } else {
            return new CustomCategoriesSimpleViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_custom_category_add, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(AddCustomCategoryViewHolder holder, int position) {
        if (holder.getClass() == AddCustomCategoryViewHolder.class) {
            holder.category = null;
        } else if (holder.getClass() == CustomCategoriesViewHolder.class) {
            Category item = data.get(position);
            CustomCategoriesViewHolder ccHolder = (CustomCategoriesViewHolder) holder;
            ccHolder.setCategory(item.getCategory());
            ccHolder.setCount(item.getStatis());
            ccHolder.category = item;
        } else {
            Category item = data.get(position);
            CustomCategoriesSimpleViewHolder ccsHolder = (CustomCategoriesSimpleViewHolder) holder;
            ccsHolder.setCategory(item.getCategory());
            ccsHolder.category = item;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == data.size()) {
            return ITEM_TYPE_ADD;
        } else {
            if (model == MODEL_NORMAL) {
                return ITEM_TYPE_CATEGORY;
            } else {
                return ITEM_TYPE_CATEGORY_SIMPLE;
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size() + 1;
    }

    class AddCustomCategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected Category category;

        public AddCustomCategoryViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, category);
            }
        }
    }


    class CustomCategoriesViewHolder extends AddCustomCategoryViewHolder {
        private TextView tvCount;
        private EditText etCategory;

        public CustomCategoriesViewHolder(View itemView) {
            super(itemView);
            etCategory = WidgetUtil.obtainViewById(itemView, R.id.et_category);
            tvCount = WidgetUtil.obtainViewById(itemView, R.id.tv_count);
            etCategory.setOnClickListener(v -> {
                etCategory.setCursorVisible(true);
            });
            etCategory.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    etCategory.setCursorVisible(true);
                    return true;
                }
                return false;
            });
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, category);
            }
        }

        public void setCategory(String category) {
            if (!TextUtils.isEmpty(category)) {
                etCategory.setText(category);
                etCategory.setSelection(category.length());
                etCategory.setCursorVisible(false);
            }
        }

        public void setCount(int count) {
            tvCount.setText(String.valueOf(count));
        }
    }

    class CustomCategoriesSimpleViewHolder extends AddCustomCategoryViewHolder{
        private TextView tvCategory;

        public CustomCategoriesSimpleViewHolder(View itemView) {
            super(itemView);
            tvCategory = (TextView) itemView;
            tvCategory.setTextColor(itemView.getResources().getColor(R.color.title_font_default));
            tvCategory.setCompoundDrawables(null, null, null, null);
        }

        public void setCategory(String category) {
            if (!TextUtils.isEmpty(category)) {
                tvCategory.setText(category);
            }
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, category);
            }
        }
    }

    private OnItemClickListener onItemClickListener;

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, Category category);
    }
}
