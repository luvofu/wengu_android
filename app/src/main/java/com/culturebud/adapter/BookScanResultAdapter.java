package com.culturebud.adapter;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.culturebud.R;
import com.culturebud.bean.Book;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XieWei on 2016/11/26.
 */

public class BookScanResultAdapter extends Adapter<BookScanResultAdapter.BookScanResultViewHolder> {
    private List<Book> data;
    private SparseBooleanArray checkedData;
    private boolean selAll = true;

    public BookScanResultAdapter() {
        data = new ArrayList<>();
        checkedData = new SparseBooleanArray();
    }

    public void addItems(List<Book> items) {
        if (items != null && !items.isEmpty()) {
            int position = data.size() - 1;
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).isContain()) {
                    continue;
                }
                checkedData.put(i, !items.get(i).isContain());
            }
            data.addAll(items);
            notifyItemRangeChanged(position, items.size());
        }
    }

    public boolean isAllChecked() {
        return selAll;
    }

    public void tongleAllChecked() {
        selAll = !selAll;
        for (int i = 0; i < checkedData.size(); i++) {
            checkedData.put(checkedData.keyAt(i), !checkedData.get(checkedData.keyAt(i)));
        }
        notifyDataSetChanged();
    }

    public int getCheckedCount() {
        int count = 0;
        for (int i = 0; i < checkedData.size(); i++) {
            if (checkedData.get(checkedData.keyAt(i))) {
                count++;
            }
        }
        return count;
    }

    @Override
    public BookScanResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BookScanResultViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_scan_result_item, parent, false));
    }

    @Override
    public void onBindViewHolder(BookScanResultViewHolder holder, int position) {
        Book item = data.get(position);
        holder.position = position;
        holder.setCover(item.getCover());
        holder.setBookName(item.getTitle());
        holder.setPubInfo(item.getAuthor(), item.getTranslator(), item.getPublisher(), item.getPubDate());
        holder.setState(item.isContain());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class BookScanResultViewHolder extends ViewHolder implements View.OnClickListener {
        private SimpleDraweeView sdvCover;
        private TextView tvBookName, tvPubInfo, tvState;
        private int position;

        public BookScanResultViewHolder(View itemView) {
            super(itemView);
            sdvCover = (SimpleDraweeView) itemView.findViewById(R.id.sdv_book_cover);
            tvBookName = (TextView) itemView.findViewById(R.id.tv_book_name);
            tvPubInfo = (TextView) itemView.findViewById(R.id.tv_publisher_info);
            tvState = (TextView) itemView.findViewById(R.id.tv_state);
            itemView.setOnClickListener(this);
        }

        public void setCover(String url) {
            if (TextUtils.isEmpty(url)) {
                return;
            }
            sdvCover.setImageURI(url);
        }

        public void setBookName(String bookName) {
            if (TextUtils.isEmpty(bookName)) {
                return;
            }
            tvBookName.setText(bookName);
        }

        public void setPubInfo(String author, String translator, String pubInfo, String pubDate) {
            if (TextUtils.isEmpty(author + translator + pubInfo + pubDate)) {
                return;
            }
            tvPubInfo.setText(author + " 著 / " + translator + " 译 / " + pubInfo + " / " + pubDate);
        }

        public void setState(boolean state) {
            Drawable drawable = null;
            if (state) {
                drawable = itemView.getResources().getDrawable(R.mipmap.has_add_icon);
                tvState.setText("已存在");
            } else {
                if (checkedData.get(position)) {
                    drawable = itemView.getResources().getDrawable(R.mipmap.book_check);
                } else {
                    drawable = itemView.getResources().getDrawable(R.mipmap.book_uncheck);
                }
                tvState.setText("");
            }
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            tvState.setCompoundDrawables(null, drawable, null, null);
        }

        @Override
        public void onClick(View view) {
            if (view == itemView) {
                if (!data.get(position).isContain()) {
                    checkedData.put(position, !checkedData.get(position));
                    if (getCheckedCount() == checkedData.size()) {
                        selAll = true;
                    } else {
                        selAll = false;
                    }
                    Drawable drawable = null;
                    if (checkedData.get(position)) {
                        drawable = itemView.getResources().getDrawable(R.mipmap.book_check);
                    } else {
                        drawable = itemView.getResources().getDrawable(R.mipmap.book_uncheck);
                    }
                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                    tvState.setCompoundDrawables(null, drawable, null, null);
                }
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(view, data.get(position), position);
                }
            }
        }
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, Book book, int position);
    }
}
