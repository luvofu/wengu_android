package com.culturebud.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.culturebud.R;
import com.culturebud.bean.Book;
import com.culturebud.bean.CollectedBook;
import com.culturebud.util.WidgetUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XieWei on 2017/1/9.
 */

public class BooksSimpleAdapter extends RecyclerView.Adapter<BooksSimpleAdapter.BookSimpleViewHolder> {
    private List<CollectedBook> data;

    public BooksSimpleAdapter() {
        data = new ArrayList<>();
    }

    public void clearData() {
        if (!data.isEmpty()) {
            data.clear();
            notifyDataSetChanged();
        }
    }

    public void addItems(List<CollectedBook> books) {
        if (books != null && !books.isEmpty()) {
            int position = data.size() - 1;
            data.addAll(books);
            notifyItemRangeChanged(position, books.size());
        }
    }

    public void addItem(CollectedBook item) {
        if (item != null) {
            data.add(item);
            notifyItemChanged(data.size() - 1);
        }
    }

    @Override
    public BookSimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BookSimpleViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.books_simple_item, parent, false));
    }

    @Override
    public void onBindViewHolder(BookSimpleViewHolder holder, int position) {
        CollectedBook item = data.get(position);
        holder.book = item;
        holder.setBookCover(item.getCover());
        holder.setBookName(item.getTitle());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class BookSimpleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private SimpleDraweeView sdvBookCover;
        private TextView tvBookName;
        private CollectedBook book;

        public BookSimpleViewHolder(View itemView) {
            super(itemView);
            sdvBookCover = WidgetUtil.obtainViewById(itemView, R.id.sdv_book_cover);
            tvBookName = WidgetUtil.obtainViewById(itemView, R.id.tv_book_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v == itemView) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(itemView, book);
                }
            }
        }

        public void setBookCover(String url) {
            if (!TextUtils.isEmpty(url)) {
                sdvBookCover.setImageURI(url);
            }
        }

        public void setBookName(String bookName) {
            if (!TextUtils.isEmpty(bookName)) {
                tvBookName.setText(bookName);
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
        void onItemClick(View v, CollectedBook book);
    }
}
