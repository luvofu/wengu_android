package com.culturebud.adapter;

import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.culturebud.R;
import com.culturebud.bean.CollectedBook;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XieWei on 2016/11/22.
 */

public class CollectedBooksVerticalAdapter extends Adapter<CollectedBooksVerticalAdapter.CBVerticalViewHolder> {
    private List<CollectedBook> data;

    public CollectedBooksVerticalAdapter() {
        data = new ArrayList<>();
    }

    public void clearData() {
        if (!data.isEmpty()) {
            data.clear();
            notifyDataSetChanged();
        }
    }

    public void addItems(List<CollectedBook> items) {
        if (items != null && !items.isEmpty()) {
            int position = data.size() - 1;
            data.addAll(items);
            notifyItemRangeChanged(position, items.size());
        }
    }

    @Override
    public CBVerticalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CBVerticalViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.collected_books_vertical_item, parent, false));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onBindViewHolder(CBVerticalViewHolder holder, int position) {
        CollectedBook item = data.get(position);
        holder.position = position;
        holder.setBookCover(item.getCover());
        holder.setBookName(item.getTitle());
    }

    class CBVerticalViewHolder extends ViewHolder implements View.OnClickListener {
        private SimpleDraweeView sdvBookCover;
        private TextView tvBookName;
        private int position;

        public CBVerticalViewHolder(View itemView) {
            super(itemView);
            sdvBookCover = (SimpleDraweeView) itemView.findViewById(R.id.sdv_book_sheet_cover);
            tvBookName = (TextView) itemView.findViewById(R.id.tv_book_name);
            itemView.setOnClickListener(this);
        }

        public void setBookCover(String url) {
            if (TextUtils.isEmpty(url)) {
                return;
            }
            sdvBookCover.setImageURI(url);
        }

        public void setBookName(String bookName) {
            if (TextUtils.isEmpty(bookName)) {
                return;
            }
            tvBookName.setText(bookName);
        }

        @Override
        public void onClick(View view) {
            if (view == itemView && onItemClickListener != null) {
                onItemClickListener.onItemClick(position, view, data.get(position));
            }
        }
    }
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View v, CollectedBook book);
    }
}
