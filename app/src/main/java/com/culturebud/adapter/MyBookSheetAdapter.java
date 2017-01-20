package com.culturebud.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.culturebud.R;
import com.culturebud.bean.BookSheet;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XieWei on 2016/12/2.
 */

public class MyBookSheetAdapter extends RecyclerView.Adapter<MyBookSheetAdapter.MyBookSheetViewHolder> {
    private List<BookSheet> data;

    public MyBookSheetAdapter() {
        data = new ArrayList<>();
    }

    public void clearData() {
        if (!data.isEmpty()) {
            data.clear();
            notifyDataSetChanged();
        }
    }

    public void addItems(List<BookSheet> items) {
        if (items != null && !items.isEmpty()) {
            int position = data.size() - 1;
            data.addAll(items);
            notifyItemRangeChanged(position, items.size());
        }
    }

    public void addItem(BookSheet item) {
        if (item != null) {
            data.add(0, item);
            notifyItemInserted(0);
        }
    }

    @Override
    public MyBookSheetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyBookSheetViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_book_sheet_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MyBookSheetViewHolder holder, int position) {
        BookSheet item = data.get(position);
        holder.bookSheet = item;
        holder.setCover(item.getCover());
        holder.setName(item.getName());
        holder.setBookNum(item.getBookNum());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyBookSheetViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private SimpleDraweeView sdvCover;
        private TextView tvName, tvBookNum;
        private BookSheet bookSheet;

        public MyBookSheetViewHolder(View itemView) {
            super(itemView);
            sdvCover = (SimpleDraweeView) itemView.findViewById(R.id.sdv_sheet_cover);
            tvName = (TextView) itemView.findViewById(R.id.tv_sheet_name);
            tvBookNum = (TextView) itemView.findViewById(R.id.tv_book_num);
            itemView.setOnClickListener(this);
        }

        public void setCover(String url) {
            if (TextUtils.isEmpty(url)) {
                return;
            }
            sdvCover.setImageURI(url);
        }

        public void setName(String name) {
            if (TextUtils.isEmpty(name)) {
                return;
            }
            tvName.setText(name);
        }

        public void setBookNum(long bookNum) {
            tvBookNum.setText(bookNum + "册");
        }

        @Override
        public void onClick(View v) {
            if (v == itemView) {
                if (null != onItemClickListener) {
                    onItemClickListener.onItemClick(v, bookSheet);
                }
            }
        }
    }

    private OnItemClickListener onItemClickListener;

    public List<BookSheet> getData() {
        return data;
    }

    public void setData(List<BookSheet> data) {
        this.data = data;
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, BookSheet bookSheet);
    }
}
