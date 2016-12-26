package com.culturebud.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.culturebud.R;
import com.culturebud.bean.BookCommunity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by XieWei on 2016/11/6.
 */

public class BookCommunityAdapter extends RecyclerView.Adapter<BookCommunityAdapter.BookCommunityViewHolder> {
    private List<BookCommunity> data;

    public BookCommunityAdapter() {
        data = new ArrayList<>();
    }

    public void clearData() {
        if (!data.isEmpty()) {
            data.clear();
            notifyDataSetChanged();
        }
    }

    public void addItems(List<BookCommunity> communities) {
        if (communities != null && communities.size() > 0) {
            int position = data.size();
            data.addAll(communities);
            notifyItemRangeChanged(position, communities.size());
        }
    }

    @Override
    public BookCommunityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BookCommunityViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_community_item, parent, false));
    }

    @Override
    public void onBindViewHolder(BookCommunityViewHolder holder, int position) {
        BookCommunity item = data.get(position);
        holder.position = position;
        holder.setTitle(item.getTitle());
        holder.setAuthor(item.getAuthor());
        holder.setNum(item.getCommentNum());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class BookCommunityViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvTitle, tvAuthor, tvNum;
        private int position;

        public BookCommunityViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.et_title);
            tvAuthor = (TextView) itemView.findViewById(R.id.tv_author);
            tvNum = (TextView) itemView.findViewById(R.id.tv_comment_num);
            itemView.setOnClickListener(this);
        }

        public void setTitle(CharSequence title) {
            tvTitle.setText(title);
        }

        public void setAuthor(CharSequence author) {
            tvAuthor.setText(author);
        }

        public void setNum(int num) {
            String tmp = String.format(Locale.getDefault(), tvNum.getContext()
                    .getString(R.string.book_community_comment_num), num);
            tvNum.setText(tmp);
        }

        @Override
        public void onClick(View v) {
            if (v == itemView) {
                if (onItemCommunityListener != null) {
                    onItemCommunityListener.onItemCommunity(v, data.get(position));
                }
                return;
            }
        }
    }

    private OnItemCommunityListener onItemCommunityListener;

    public void setOnItemCommunityListener(OnItemCommunityListener onItemCommunityListener) {
        this.onItemCommunityListener = onItemCommunityListener;
    }

    public interface OnItemCommunityListener {
        void onItemCommunity(View v, BookCommunity community);
    }
}
