package com.culturebud.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.culturebud.R;
import com.culturebud.adapter.BookSheetsAdapter.BookSheetsViewHolder;
import com.culturebud.bean.BookSheet;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XieWei on 2016/11/18.
 */

public class BookSheetsAdapter extends RecyclerView.Adapter<BookSheetsViewHolder> {
    private List<BookSheet> data;

    public BookSheetsAdapter() {
        data = new ArrayList<>();
    }

    public void addItems(List<BookSheet> items) {
        if (items == null || items.isEmpty()) {
            return;
        }
        int position = data.size() - 1;
        data.addAll(items);
        notifyItemChanged(position, items.size());
    }

    public void clearData() {
        if (!data.isEmpty()) {
            data.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public BookSheetsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BookSheetsViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_sheets_item, parent, false));
    }

    @Override
    public void onBindViewHolder(BookSheetsViewHolder holder, int position) {
        BookSheet item = data.get(position);
        holder.setCover(item.getCover());
        holder.setTitle(item.getName());
        holder.setNick(item.getNickname());
        holder.setVolumes(item.getBookNum() + "册");
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class BookSheetsViewHolder extends RecyclerView.ViewHolder {
        private SimpleDraweeView sdvCover;
        private TextView tvTitle, tvCount, tvNick;

        public BookSheetsViewHolder(View itemView) {
            super(itemView);
            sdvCover = (SimpleDraweeView) itemView.findViewById(R.id.sdv_bs_cover);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_bs_title);
            tvCount = (TextView) itemView.findViewById(R.id.tv_book_volumes);
            tvNick = (TextView) itemView.findViewById(R.id.tv_nick);
        }

        public void setCover(String url) {
            if (TextUtils.isEmpty(url)) {
                return;
            }
            sdvCover.setImageURI(url);
        }

        public void setTitle(String title) {
            if (TextUtils.isEmpty(title)) {
                return;
            }
            tvTitle.setText(title);
        }

        public void setVolumes(String volumes) {
            if (TextUtils.isEmpty(volumes)) {
                return;
            }
            tvCount.setText(volumes);
        }

        public void setNick(String nick) {
            if (TextUtils.isEmpty(nick)) {
                return;
            }
            tvNick.setText(nick);
        }
    }
}
