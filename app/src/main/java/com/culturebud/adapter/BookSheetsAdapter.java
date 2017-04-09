package com.culturebud.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.culturebud.BaseApp;
import com.culturebud.R;
import com.culturebud.adapter.BookSheetsAdapter.BookSheetsViewHolder;
import com.culturebud.bean.BookSheet;
import com.culturebud.bean.User;
import com.culturebud.util.WidgetUtil;
import com.culturebud.widget.SwipeMenuView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XieWei on 2016/11/18.
 */

public class BookSheetsAdapter extends RecyclerView.Adapter<BookSheetsViewHolder> {
    private List<BookSheet> data;
    private boolean canDel = true;

    public void disableDel() {
        canDel = false;
    }

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
        holder.position = position;
        holder.setCover(item.getCover());
        holder.setTitle(item.getName());
        holder.setNick(item.getNickname());
        holder.setVolumes(item.getBookNum() + "册");
        holder.showNick(item.getUserId());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public boolean deleteItem(BookSheet sheet) {
        if (sheet == null) {
            return false;
        } else {
            int position = data.indexOf(sheet);
            if (position == -1) {
                return false;
            }
            boolean res = data.remove(sheet);
            notifyItemRemoved(position);
            return res;
        }
    }
    class BookSheetsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private SimpleDraweeView sdvCover;
        private TextView tvTitle, tvCount, tvNick;
        private Button btnDel;
        private int position;

        public BookSheetsViewHolder(View itemView) {
            super(itemView);
            sdvCover = WidgetUtil.obtainViewById(itemView, R.id.sdv_bs_cover);
            tvTitle = WidgetUtil.obtainViewById(itemView, R.id.tv_bs_title);
            tvCount = WidgetUtil.obtainViewById(itemView, R.id.tv_book_volumes);
            tvNick = WidgetUtil.obtainViewById(itemView, R.id.tv_nick);
            btnDel = WidgetUtil.obtainViewById(itemView, R.id.btn_delete);
            itemView.setOnClickListener(this);
            btnDel.setOnClickListener(this);
            if (!canDel) {
                ((ViewGroup) itemView).removeView(btnDel);
            }
        }

        public void showNick(long userId) {
            User user = BaseApp.getInstance().getUser();
            if (user != null && user.getUserId() == userId) {
                tvNick.setVisibility(View.GONE);
            } else {
                tvNick.setVisibility(View.VISIBLE);
            }
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

        @Override
        public void onClick(View view) {
            if (view == itemView && onItemClickListener != null) {
                onItemClickListener.onItemClick(view, position, data.get(position));
                return;
            }
            if (view.getId() == R.id.btn_delete) {
                if (deleteListener != null) {
                    deleteListener.onItemDelete(position, data.get(position));
                }
            }
        }
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position, BookSheet bookSheet);
    }

    private OnItemDeleteListener deleteListener;

    public OnItemDeleteListener getDeleteListener() {
        return deleteListener;
    }

    public void setDeleteListener(OnItemDeleteListener deleteListener) {
        this.deleteListener = deleteListener;
    }

    public interface OnItemDeleteListener {
        void onItemDelete(int position, BookSheet bookSheet);
    }
}
