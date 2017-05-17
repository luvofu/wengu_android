package com.culturebud.adapter;

import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.culturebud.BaseApp;
import com.culturebud.R;
import com.culturebud.bean.CollectedBook;
import com.culturebud.util.WidgetUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by XieWei on 2016/11/9.
 */

public class CollectedBooksAdapter extends RecyclerView.Adapter<CollectedBooksAdapter.CollectedBooksViewHolder> {
    private List<CollectedBook> data;
    public static final int MODEL_EDIT = 0;
    public static final int MODEL_CHECK = 1;
    private int model = MODEL_EDIT;
    private Set<CollectedBook> checkedSet;
    private boolean isMe = false;

    public void setMe(long userId) {
        isMe = BaseApp.getInstance().isMe(userId);
    }

    @IntDef({MODEL_EDIT, MODEL_CHECK})
    @interface ModelRes {
    }

    public CollectedBooksAdapter() {
        data = new ArrayList<>();
        checkedSet = new HashSet<>();
    }

    public Set<CollectedBook> getCheckedBooks() {
        Set<CollectedBook> tmp = new HashSet<>();
        tmp.addAll(checkedSet);
        return tmp;
    }

    public void clearCheckedStatus() {
        checkedSet.clear();
    }

    public void setModel(@ModelRes int model) {
        setModel(model, false);
    }

    public void setModel(@ModelRes int model, boolean notify) {
        this.model = model;
        if (notify) {
            notifyDataSetChanged();
        }
    }

    public boolean inModel(int modelCheck) {
        return model == modelCheck;
    }

    public void clearData() {
        if (!data.isEmpty()) {
            data.clear();
            checkedSet.clear();
            notifyDataSetChanged();
        }
    }

    public void deleteItems(Collection<CollectedBook> items) {
        data.removeAll(items);
        checkedSet.removeAll(items);
        notifyDataSetChanged();
    }

    public void addItems(List<CollectedBook> books) {
        if (books == null || books.isEmpty()) {
            return;
        }
        int position = data.size() - 1;
        data.addAll(books);
        notifyItemRangeChanged(position, books.size());
    }

    public void addItem(CollectedBook book) {
        if (book == null) {
            return;
        }
        data.add(book);
        notifyItemChanged(data.size() - 1);
    }

    @Override
    public CollectedBooksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CollectedBooksViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.collected_books_item, parent, false));
    }

    @Override
    public void onBindViewHolder(CollectedBooksViewHolder holder, int position) {
        CollectedBook item = data.get(position);
        holder.position = position;
        holder.item = item;
        holder.setCover(item.getCover());
        if (model == MODEL_EDIT) {
            holder.showEditModel();
        } else {
            holder.showCheckModel();
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class CollectedBooksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton
            .OnCheckedChangeListener {
        private SimpleDraweeView sdvCover;
        private ImageView ivEdit;
        private CheckBox cbCheck;
        private int position;
        private CollectedBook item;

        public CollectedBooksViewHolder(View itemView) {
            super(itemView);
            sdvCover = WidgetUtil.obtainViewById(itemView, R.id.sdv_book_sheet_cover);
            ivEdit = WidgetUtil.obtainViewById(itemView, R.id.iv_edit);
            cbCheck = WidgetUtil.obtainViewById(itemView, R.id.cb_check);
//            sdvCover.setOnClickListener(this);
            ivEdit.setOnClickListener(this);
            itemView.setOnClickListener(this);
            cbCheck.setOnCheckedChangeListener(this);
        }

        public void setCover(String url) {
            if (TextUtils.isEmpty(url)) {
                return;
            }
            sdvCover.setImageURI(url);
        }

        public void showEditModel() {
            if (isMe) {
                ivEdit.setVisibility(View.VISIBLE);
            } else {
                ivEdit.setVisibility(View.GONE);
            }
            cbCheck.setVisibility(View.GONE);

        }

        public void showCheckModel() {
            cbCheck.setVisibility(View.VISIBLE);
            ivEdit.setVisibility(View.GONE);
            if (checkedSet.contains(item)) {
                cbCheck.setChecked(true);
            } else {
                cbCheck.setChecked(false);
            }
        }

        @Override
        public void onClick(View v) {
            if (itemView == v && onItemClickListener != null) {
                if (inModel(CollectedBooksAdapter.MODEL_EDIT)) {
                    onItemClickListener.onItemClick(v, position, data.get(position), OPERA_TYPE_DETAIL);
                } else {
                    cbCheck.performClick();
                }
                return;
            }
            switch (v.getId()) {
                case R.id.sdv_book_sheet_cover:
                    break;
                case R.id.iv_edit:
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, position, data.get(position), OPERA_TYPE_EDIT);
                    }
                    break;
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                checkedSet.add(item);
            } else {
                checkedSet.remove(item);
            }
        }
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public static final int OPERA_TYPE_DETAIL = 0;
    public static final int OPERA_TYPE_EDIT = 1;

    public interface OnItemClickListener {
        void onItemClick(View v, int position, CollectedBook book, int operaType);
    }
}
