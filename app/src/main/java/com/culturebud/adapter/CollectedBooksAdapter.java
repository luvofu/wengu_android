package com.culturebud.adapter;

import android.support.annotation.IntDef;
import android.support.annotation.IntegerRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.culturebud.R;
import com.culturebud.bean.CollectedBook;
import com.culturebud.util.WidgetUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XieWei on 2016/11/9.
 */

public class CollectedBooksAdapter extends RecyclerView.Adapter<CollectedBooksAdapter.CollectedBooksViewHolder> {
    private List<CollectedBook> data;
    public static final int MODEL_EDIT = 0;
    public static final int MODEL_CHECK = 1;
    private int model = MODEL_EDIT;

    @IntDef({MODEL_EDIT, MODEL_CHECK})
    @interface ModelRes {
    }

    public CollectedBooksAdapter() {
        data = new ArrayList<>();
    }

    public void setModel(@ModelRes int model) {
        this.model = model;
        notifyDataSetChanged();
    }

    public void clearData() {
        if (!data.isEmpty()) {
            data.clear();
            notifyDataSetChanged();
        }
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

    class CollectedBooksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private SimpleDraweeView sdvCover;
        private ImageView ivEdit;
        private CheckBox cbCheck;
        private int position;

        public CollectedBooksViewHolder(View itemView) {
            super(itemView);
            sdvCover = WidgetUtil.obtainViewById(itemView, R.id.sdv_book_sheet_cover);
            ivEdit = WidgetUtil.obtainViewById(itemView, R.id.iv_edit);
            cbCheck = WidgetUtil.obtainViewById(itemView, R.id.cb_check);
//            sdvCover.setOnClickListener(this);
            ivEdit.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        public void setCover(String url) {
            if (TextUtils.isEmpty(url)) {
                return;
            }
            sdvCover.setImageURI(url);
        }

        public void showEditModel() {
            ivEdit.setVisibility(View.VISIBLE);
            cbCheck.setVisibility(View.GONE);
        }

        public void showCheckModel() {
            cbCheck.setVisibility(View.VISIBLE);
            ivEdit.setVisibility(View.GONE);
        }

        @Override
        public void onClick(View v) {
            if (itemView == v && onItemClickListener != null) {
                onItemClickListener.onItemClick(v, position, data.get(position), OPERA_TYPE_DETAIL);
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
