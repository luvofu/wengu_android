package com.culturebud.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.culturebud.R;
import com.culturebud.bean.Category;
import com.culturebud.util.WidgetUtil;

import java.util.ArrayList;
import java.util.Collections;
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

    public List<Category> getData() {
        List<Category> categories = new ArrayList<>();
        categories.addAll(data);
        return categories;
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

    public void removeItem(int position) {
        Category c = data.remove(position);
        if (c != null) {
            notifyItemRemoved(position);
        }
    }

    public void onItemDragMoving(RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        int from = getViewHolderPosition(source);
        int to = getViewHolderPosition(target);
        if (from < to) {
            for (int i = from; i < to; i++) {
                Collections.swap(data, i, i + 1);
            }
        } else {
            for (int i = from; i > to; i--) {
                Collections.swap(data, i, i - 1);
            }
        }

        notifyItemMoved(source.getAdapterPosition(), target.getAdapterPosition());
    }

    public int getViewHolderPosition(RecyclerView.ViewHolder viewHolder) {
        return viewHolder.getAdapterPosition();
    }

    @Override
    public AddCustomCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (ITEM_TYPE_ADD == viewType) {
            return new AddCustomCategoryViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_custom_category_add, parent, false));
        } else if (ITEM_TYPE_CATEGORY == viewType) {
            return new CustomCategoriesViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_custom_category, parent, false));
        } else {
            return new CustomCategoriesSimpleViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_custom_category_add, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(AddCustomCategoryViewHolder holder, int position) {
        holder.position = position;
        if (holder.getClass() == CustomCategoriesViewHolder.class) {
            Category item = data.get(position);
            CustomCategoriesViewHolder ccHolder = (CustomCategoriesViewHolder) holder;
            ccHolder.setCategory(item.getCategory());
            ccHolder.setCount(item.getStatis());
            ccHolder.category = item;
        } else if (holder.getClass() == AddCustomCategoryViewHolder.class) {
            holder.category = null;
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

    public void deleteItem(Category category) {
        if (data != null) {
            int index = data.indexOf(category);
            if (data.remove(category)) {
                notifyItemRemoved(index);
            }
        }
    }

    class AddCustomCategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected Category category;
        protected int position;

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
        private Button btnDel;

        public CustomCategoriesViewHolder(View itemView) {
            super(itemView);
            etCategory = WidgetUtil.obtainViewById(itemView, R.id.et_category);
            tvCount = WidgetUtil.obtainViewById(itemView, R.id.tv_count);
            btnDel = WidgetUtil.obtainViewById(itemView, R.id.btn_delete);
            etCategory.setOnTouchListener((v, event) -> {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    etCategory.requestFocus();
                    etCategory.setCursorVisible(true);
                    etCategory.setSelection(etCategory.getText().length());
                }
                return false;
            });
            etCategory.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    etCategory.setCursorVisible(false);
                    etCategory.clearFocus();

                    //隐藏键盘.
                    InputMethodManager imm = (InputMethodManager)
                            ((Activity) editCategoryListener).getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etCategory.getWindowToken(), 0);

                    int cposition = getAdapterPosition();
                    Category category = data.get(cposition);
                    String newCategoryName = etCategory.getText().toString();

                    if (newCategoryName.isEmpty()) {
                        etCategory.setError("分类名不能为空");
                        return false;
                    }

                    if (!category.getCategory().equals(newCategoryName) && editCategoryListener != null) {
                        //上传.
                        category.setCategory(newCategoryName);
                        editCategoryListener.onItemEditCategory(etCategory, category);
                    }

                    return true;
                }
                return false;
            });

            etCategory.setOnFocusChangeListener((view, b) -> {
                //失去焦点，提交更新.
                if (!b) {
                    int cPosition = getAdapterPosition();
                    if (cPosition >= 0 && data.size() > cPosition) {
                        Category category1 = data.get(cPosition);

                        String newCategoryName = etCategory.getText().toString();

                        if (!newCategoryName.isEmpty()) {
                            if (!category1.getCategory().equals(newCategoryName) && editCategoryListener != null) {
                                //上传.
                                category1.setCategory(newCategoryName);
                                editCategoryListener.onItemEditCategory(etCategory, category1);
                            }
                        }
                    }
                }
            });

            btnDel.setOnClickListener(v -> {
                if (deleteListener != null) {
                    int cPosition = getAdapterPosition();
                    Category category = data.get(cPosition);

                    deleteListener.onItemDelete(cPosition, category);
                }
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

    class CustomCategoriesSimpleViewHolder extends AddCustomCategoryViewHolder {
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

    private OnItemDeleteListener deleteListener;

    public OnItemDeleteListener getDeleteListener() {
        return deleteListener;
    }

    public void setDeleteListener(OnItemDeleteListener deleteListener) {
        this.deleteListener = deleteListener;
    }

    public interface OnItemDeleteListener {
        void onItemDelete(int position, Category category);
    }

    private OnItemEditCategoryListener editCategoryListener;

    public OnItemEditCategoryListener getEditCategoryListener() {
        return editCategoryListener;
    }

    public void setEditCategoryListener(OnItemEditCategoryListener editCategoryListener) {
        this.editCategoryListener = editCategoryListener;
    }

    public interface OnItemEditCategoryListener {
        void onItemEditCategory(EditText editText, Category category);
    }

}
