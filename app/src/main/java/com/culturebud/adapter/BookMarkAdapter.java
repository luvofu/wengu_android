package com.culturebud.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.culturebud.R;
import com.culturebud.bean.BookMark;
import com.culturebud.util.WidgetUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by XieWei on 2017/3/18.
 */

public class BookMarkAdapter extends RecyclerView.Adapter<BookMarkAdapter.BookMarkViewHolder> {
    private List<BookMark> data;
    private static final int ITEM_TYPE_ADD = 0;
    private static final int ITEM_TYPE_BOOK_MARK = 1;

    public BookMarkAdapter() {
        data = new ArrayList<>();
    }

    public void clearData() {
        if (data.size() > 0) {
            data.clear();
            notifyDataSetChanged();
        }
    }

    public void addItems(List<BookMark> items) {
        if (items != null && items.size() > 0) {
            int position = data.size() - 1;
            data.addAll(items);
            notifyItemRangeChanged(position, items.size());
        }
    }

    public void addItem(BookMark item) {
        if (item != null) {
            data.add(0, item);
            notifyItemChanged(0);
        }
    }

    @Override
    public BookMarkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_BOOK_MARK) {
            return new BookMarkViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.book_mark_item, parent, false), viewType);
        } else {
            FrameLayout fl = (FrameLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.book_mark_item,
                    parent, false);
            fl.removeAllViews();

            ImageView ivAdd = new ImageView(parent.getContext());
            ivAdd.setImageResource(R.mipmap.ic_add_white);
            ivAdd.setBackgroundResource(R.drawable.bg_dashed);
            ivAdd.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(parent.getResources()
                    .getDimensionPixelSize(R.dimen.book_mark_item_cover_width),
                    parent.getResources().getDimensionPixelSize(R.dimen.book_mark_item_cover_height));
            params.gravity = Gravity.BOTTOM;
            ivAdd.setLayoutParams(params);
            fl.addView(ivAdd);
            return new BookMarkViewHolder(fl, viewType);
        }
    }

    @Override
    public void onBindViewHolder(BookMarkViewHolder holder, int position) {
        if (position == data.size()) {
            //TODO do nothing
        } else {
            BookMark item = data.get(position);
            holder.setBookCover(item.getCover());
            holder.setBookTitle(item.getName());
            holder.setReadPage(item.getPages());
            holder.setReadTime(item.getUpdatedTime());
            holder.setReadProgress(item.getPages(), item.getTotalPage());
            holder.setItem(item);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == data.size()) {
            return ITEM_TYPE_ADD;
        } else {
            return ITEM_TYPE_BOOK_MARK;
        }
    }

    @Override
    public int getItemCount() {
        return data.size() + 1;
    }

    class BookMarkViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private SimpleDraweeView sdvCover;
        private TextView tvReadPage, tvReadProgress, tvReadTime, tvBookTitle;
        private ProgressBar pbReadProgress;
        private int viewType;
        private BookMark item;

        public BookMarkViewHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
            if (viewType == ITEM_TYPE_ADD) {

            } else {
                sdvCover = WidgetUtil.obtainViewById(itemView, R.id.sdv_book_cover);
                tvReadPage = WidgetUtil.obtainViewById(itemView, R.id.tv_read_page);
                tvReadProgress = WidgetUtil.obtainViewById(itemView, R.id.tv_read_progress);
                tvReadTime = WidgetUtil.obtainViewById(itemView, R.id.tv_read_time);
                tvBookTitle = WidgetUtil.obtainViewById(itemView, R.id.tv_book_title);
                pbReadProgress = WidgetUtil.obtainViewById(itemView, R.id.pb_read_progress);
                pbReadProgress.setMax(100);
            }
            itemView.setOnClickListener(this);
        }

        public void setItem(BookMark item) {
            this.item = item;
        }

        public void setBookCover(String url) {
            if (!TextUtils.isEmpty(url)) {
                sdvCover.setImageURI(url);
            }
        }

        public void setReadPage(int page) {
            if (page > 0) {
                tvReadPage.setText(String.valueOf(page));
            }
        }

        public void setReadProgress(int pages, int totalPages) {
            if (totalPages > 0) {
                float progress = pages / Float.valueOf(totalPages);
                pbReadProgress.setProgress((int) (progress * 100));
                tvReadProgress.setText(String.format(Locale.getDefault(), "%.2f", progress * 100) + "%");
            }
        }

        public void setReadTime(long time) {
            tvReadTime.setText(sdf.format(new Date(time)));
        }

        public void setBookTitle(String title) {
            if (!TextUtils.isEmpty(title)) {
                tvBookTitle.setText(title);
            }
        }

        @Override
        public void onClick(View v) {
            if (v == itemView) {
                if (onBookMarkItemClickListener != null) {
                    onBookMarkItemClickListener.onBookMarkItemClick(v, item, viewType);
                }
            }
        }
    }

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm");

    private OnBookMarkItemClickListener onBookMarkItemClickListener;

    public OnBookMarkItemClickListener getOnBookMarkItemClickListener() {
        return onBookMarkItemClickListener;
    }

    public void setOnBookMarkItemClickListener(OnBookMarkItemClickListener onBookMarkItemClickListener) {
        this.onBookMarkItemClickListener = onBookMarkItemClickListener;
    }

    public interface OnBookMarkItemClickListener {
        void onBookMarkItemClick(View v, BookMark item, int type);
    }
}
