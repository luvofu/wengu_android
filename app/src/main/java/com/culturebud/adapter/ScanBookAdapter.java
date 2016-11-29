package com.culturebud.adapter;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.culturebud.R;
import com.culturebud.bean.Book;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by XieWei on 2016/11/26.
 */

public class ScanBookAdapter extends Adapter<ScanBookAdapter.ScanBookViewHolder> {
    private List<Book> data;

    public ScanBookAdapter() {
        data = new ArrayList<>();
    }

    public List<Book> getData() {
        return data;
    }

    public void addItem(Book book) {
        if (book != null) {
            data.add(0, book);
            notifyItemInserted(0);
        }
    }

    public boolean hasAdded(Book book) {
        return data.contains(book);
    }

    @Override
    public ScanBookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ScanBookViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.scan_book_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ScanBookViewHolder holder, int position) {
        Book item = data.get(position);
        holder.setBookName(item.getTitle());
        holder.setBookState(item.isContain());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ScanBookViewHolder extends ViewHolder {
        private TextView tvBookName, tvBookState;

        public ScanBookViewHolder(View itemView) {
            super(itemView);
            tvBookName = (TextView) itemView.findViewById(R.id.tv_book_name);
            tvBookState = (TextView) itemView.findViewById(R.id.tv_book_state);
        }

        public void setBookName(String bookName) {
            if (TextUtils.isEmpty(bookName)) {
                return;
            }
            tvBookName.setText(bookName);
        }

        public void setBookState(boolean state) {
            Drawable drawable = null;
            if (state) {
                tvBookState.setText("已在书架");
                drawable = itemView.getResources().getDrawable(R.mipmap.book_exits_icon);
            } else {
                tvBookState.setText("");
                drawable = itemView.getResources().getDrawable(R.mipmap.recognized_icon);
            }
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            tvBookState.setCompoundDrawables(null, null, drawable, null);
        }
    }
}
