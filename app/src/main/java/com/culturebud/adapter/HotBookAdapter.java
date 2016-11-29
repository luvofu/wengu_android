package com.culturebud.adapter;

import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
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

/**
 * Created by XieWei on 2016/10/24.
 */

public class HotBookAdapter extends RecyclerView.Adapter<HotBookAdapter.HotBookViewHolder> {
    private static final String TAG = "HotBookAdapter";
    private List<Book> data;

    public HotBookAdapter() {
        data = new ArrayList<>();
    }

    public void clearData() {
        data.clear();
        notifyDataSetChanged();
    }

    public void addItems(List<Book> books) {
        if (books == null || books.size() < 0) {
            return;
        }
        int lastPosition = data.size() - 1;
        data.addAll(books);
        notifyItemRangeChanged(lastPosition, books.size());
        Log.e(TAG, "addItems()");
    }

    @Override
    public HotBookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HotBookViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.front_hot_book_item, parent, false));
    }

    @Override
    public void onBindViewHolder(HotBookViewHolder holder, int position) {
        Book book = data.get(position);
        holder.setPosition(position);
        holder.setBookCover(book.getCover());
        holder.setBookName(book.getTitle());
        holder.setHotRating(book.getRating());
        Log.e(TAG, "onBindViewHolder() --> " + book);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class HotBookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvBookName;
        private SimpleDraweeView sdvCover;
        private RatingBar rbRating;
        private int position;

        public HotBookViewHolder(View itemView) {
            super(itemView);
            tvBookName = (TextView) itemView.findViewById(R.id.tv_book_name);
            sdvCover = (SimpleDraweeView) itemView.findViewById(R.id.sdv_book_cover);
            rbRating = (RatingBar) itemView.findViewById(R.id.rb_rating);
            rbRating.setNumStars(5);
            rbRating.setMax(10);
            rbRating.setStepSize(0.1F);
            LayerDrawable ld = (LayerDrawable) rbRating.getProgressDrawable();
            ld.getDrawable(0).setColorFilter(itemView.getResources().getColor(R.color.light_font_black), PorterDuff.Mode.SRC_ATOP);
            ld.getDrawable(1).setColorFilter(itemView.getResources().getColor(R.color.yellow), PorterDuff.Mode.SRC_ATOP);
            ld.getDrawable(2).setColorFilter(itemView.getResources().getColor(R.color.orange), PorterDuff.Mode.SRC_ATOP);
            itemView.setOnClickListener(this);
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public void setBookName(CharSequence bookName) {
            tvBookName.setText(bookName);
        }

        public void setBookCover(String url) {
            if (TextUtils.isEmpty(url)) {
                return;
            }
            Uri uri = Uri.parse(url);
            sdvCover.setImageURI(uri);
        }

        public void setHotRating(float rating) {
            Log.d(TAG, "setHotRating --> " + rating);
            rbRating.setRating(rating / 2);

            Log.d(TAG, "rbRating.getRating() --> " + rbRating.getRating());
        }

        @Override
        public void onClick(View v) {
            if (v == itemView && itemClickListener != null) {
                itemClickListener.onItemClick(v, data.get(position));
            }
        }
    }

    private BooksAdapter.OnItemClickListener itemClickListener;

    public void setOnItemClickListener(BooksAdapter.OnItemClickListener listener) {
        itemClickListener = listener;
    }

    interface OnItemClickListener {
        void onItemClick(View v, Book book);
    }
}
