package com.culturebud.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.culturebud.CommonConst;
import com.culturebud.R;
import com.culturebud.bean.CheckedBook;
import com.culturebud.util.WidgetUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XieWei on 2017/3/30.
 */

public class MyCreatedBooksAdapter extends RecyclerView.Adapter<MyCreatedBooksAdapter.MyCreatedBooksViewHolder> {
    private List<CheckedBook> data;

    public MyCreatedBooksAdapter() {
        data = new ArrayList<>();
    }

    public void addItems(List<CheckedBook> items) {
        if (items != null && !items.isEmpty()) {
            int position = data.size() - 1;
            data.addAll(items);
            notifyItemRangeChanged(position, items.size());
        }
    }

    public void addItem(CheckedBook item) {
        if (item != null) {
            data.add(item);
            notifyItemInserted(data.size() - 1);
        }
    }

    public void clearData() {
        if (!data.isEmpty()) {
            data.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public MyCreatedBooksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyCreatedBooksViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_my_created_books, parent, false));
    }

    @Override
    public void onBindViewHolder(MyCreatedBooksViewHolder holder, int position) {
        CheckedBook item = data.get(position);
        holder.setCover(item.getCover());
        holder.setBookTitle(item.getTitle());
        holder.setBookAuthor(item.getAuthor());
        holder.setStatus(item.getCheckStatus());
        holder.setCheckInfo(item.getCheckInfo());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyCreatedBooksViewHolder extends RecyclerView.ViewHolder {
        private SimpleDraweeView sdvCover;
        private TextView tvTitle, tvAuthor, tvStatus, tvInfo;

        public MyCreatedBooksViewHolder(View itemView) {
            super(itemView);
            sdvCover = WidgetUtil.obtainViewById(itemView, R.id.sdv_book_cover);
            tvTitle = WidgetUtil.obtainViewById(itemView, R.id.tv_book_title);
            tvAuthor = WidgetUtil.obtainViewById(itemView, R.id.tv_book_author);
            tvStatus = WidgetUtil.obtainViewById(itemView, R.id.tv_status);
            tvInfo = WidgetUtil.obtainViewById(itemView, R.id.tv_check_info);
        }

        public void setCover(String url) {
            if (!TextUtils.isEmpty(url)) {
                sdvCover.setImageURI(url);
            }
        }

        public void setBookTitle(String title) {
            if (!TextUtils.isEmpty(title)) {
                tvTitle.setText(title);
            }
        }

        public void setBookAuthor(String author) {
            if (!TextUtils.isEmpty(author)) {
                tvAuthor.setText(author);
            }
        }

        public void setStatus(int status) {
            switch (status) {
                case CommonConst.BookVerifyStatus.STATUS_VERIFY:
                    tvStatus.setText("审核中");
                    break;
                case CommonConst.BookVerifyStatus.STATUS_NO_PASS:
                    tvStatus.setText("审核未通过");
                    break;
                case CommonConst.BookVerifyStatus.STATUS_PASS:
                    tvStatus.setText("审核通过");
                    break;
            }
        }

        public void setCheckInfo(String info) {
            if (TextUtils.isEmpty(info)) {
                tvInfo.setVisibility(View.GONE);
            } else {
                tvInfo.setText("未通过原因：" + info);
                tvInfo.setVisibility(View.VISIBLE);
            }
        }
    }
}
