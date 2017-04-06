package com.culturebud.adapter;

import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.culturebud.R;
import com.culturebud.bean.Book;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by XieWei on 2016/11/3.
 */

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BooksViewHolder> {
    private List<Book> data;

    public BooksAdapter() {
        data = new ArrayList<>();
    }

    public void clearData() {
        if (data.size() > 0) {
            data.clear();
            notifyDataSetChanged();
        }
    }

    public void addItems(List<Book> books) {
        if (books == null || books.size() <= 0) {
            return;
        }
        int position = data.size() - 1;
        data.addAll(books);
        notifyItemRangeChanged(position, books.size());
    }

    @Override
    public BooksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BooksViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.books_item, parent, false));
    }

    @Override
    public void onBindViewHolder(BooksViewHolder holder, int position) {
        Book book = data.get(position);
        holder.setPosition(position);
        holder.setBookCover(book.getCover());
        holder.setBookName(book.getTitle());
        holder.setPublisherInfo(book.getAuthor() + " 著 / "
                + book.getTranslator() + " 译 / "
                + book.getPublisher() + " / " + book.getPubDate());
        holder.setGoodRating(book.getRating());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class BooksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private int position;
        private SimpleDraweeView sdvBookCover;
        private TextView tvBookName, tvPublisherInfo, tvGoodNum;
        private RatingBar rbGoodRating;

        public BooksViewHolder(View itemView) {
            super(itemView);
            sdvBookCover = (SimpleDraweeView) itemView.findViewById(R.id.sdv_book_sheet_cover);
            tvBookName = (TextView) itemView.findViewById(R.id.tv_book_name);
            tvPublisherInfo = (TextView) itemView.findViewById(R.id.tv_publisher_info);
            rbGoodRating = (RatingBar) itemView.findViewById(R.id.rb_good_rating);
            tvGoodNum = (TextView) itemView.findViewById(R.id.tv_good_num);
            itemView.setOnClickListener(this);
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT_WATCH) {
                LayerDrawable ld = (LayerDrawable) rbGoodRating.getProgressDrawable();
                ld.getDrawable(0).setColorFilter(itemView.getResources().getColor(R.color.font_black_light), PorterDuff.Mode.SRC_ATOP);
                ld.getDrawable(1).setColorFilter(itemView.getResources().getColor(R.color.yellow), PorterDuff.Mode.SRC_ATOP);
                ld.getDrawable(2).setColorFilter(itemView.getResources().getColor(R.color.orange), PorterDuff.Mode.SRC_ATOP);
            }
        }

        public void setPosition(int position) {
            this.position = position;
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

        public void setPublisherInfo(String publisherInfo) {
            if (TextUtils.isEmpty(publisherInfo)) {
                return;
            }
            tvPublisherInfo.setText(publisherInfo);
        }

        public void setGoodRating(float rating) {
            rbGoodRating.setRating(rating / 2);
            tvGoodNum.setText(String.format(Locale.getDefault(), "%.1f", rating) + "分");
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(v, data.get(position));
            }
        }
    }

    private OnItemClickListener itemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        itemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, Book book);
    }
}
