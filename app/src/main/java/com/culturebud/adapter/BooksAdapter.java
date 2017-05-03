package com.culturebud.adapter;

import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.culturebud.R;
import com.culturebud.bean.Book;
import com.culturebud.util.WidgetUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by XieWei on 2016/11/3.
 */

public class BooksAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Book> data;
    private int operaType;
    private List<Long> addedIds;

    public BooksAdapter(int operaType) {
        data = new ArrayList<>();
        this.operaType = operaType;
        if (operaType == 1) {
            addedIds = new ArrayList<>();
        }
    }

    public void addIds(List<Long> addedIds) {
        if (addedIds != null && !addedIds.isEmpty()) {
            this.addedIds.addAll(addedIds);
        }
    }

    public void addId(long bookId) {
        if (bookId > 0) {
            addedIds.add(bookId);
            notifyDataSetChanged();
        }
    }

    public void deleteItem(long id) {
        Book tmp = null;
        int idx = 0;
        for (Book b : data) {
            if (b.getBookId() == id) {
                tmp = b;
                break;
            }
            idx++;
        }
        if (tmp != null) {
            data.remove(tmp);
            notifyItemRemoved(idx);
        }
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (operaType == 0) {
            return new BooksViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.books_item, parent, false));
        } else {
            return new BooksAddToBookSheetViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.books_operas_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (operaType == 0) {
            BooksViewHolder holder1 = (BooksViewHolder) holder;
            Book book = data.get(position);
            holder1.setPosition(position);
            holder1.setBookCover(book.getCover());
            holder1.setBookName(book.getTitle());
            holder1.setPublisherInfo((TextUtils.isEmpty(book.getAuthor()) ? "" : (book.getAuthor() + " 著 / "))
                    + (TextUtils.isEmpty(book.getTranslator()) ? "" : (book.getTranslator() + " 译 / "))
                    + (TextUtils.isEmpty(book.getPublisher()) ? "" : (book.getPublisher() + " / "))
                    + (TextUtils.isEmpty(book.getPubDate()) ? "" : book.getPubDate()));
            holder1.setGoodRating(book.getRating());
        } else if (operaType == 1) {
            BooksAddToBookSheetViewHolder holder1 = (BooksAddToBookSheetViewHolder) holder;
            Book book = data.get(position);
            holder1.setPosition(position);
            holder1.setBookCover(book.getCover());
            holder1.setBookName(book.getTitle());
            holder1.setPublisherInfo((TextUtils.isEmpty(book.getAuthor()) ? "" : (book.getAuthor() + " 著 / "))
                    + (TextUtils.isEmpty(book.getTranslator()) ? "" : (book.getTranslator() + " 译 / "))
                    + (TextUtils.isEmpty(book.getPublisher()) ? "" : (book.getPublisher() + " / "))
                    + (TextUtils.isEmpty(book.getPubDate()) ? "" : book.getPubDate()));
            holder1.hasAdded(book.getBookId());
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class BooksAddToBookSheetViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private int position;
        private SimpleDraweeView sdvBookCover;
        private TextView tvBookName, tvPublisherInfo, tvAdd;

        public BooksAddToBookSheetViewHolder(View itemView) {
            super(itemView);
            sdvBookCover = WidgetUtil.obtainViewById(itemView, R.id.sdv_book_sheet_cover);
            tvBookName = WidgetUtil.obtainViewById(itemView, R.id.tv_book_name);
            tvPublisherInfo = WidgetUtil.obtainViewById(itemView, R.id.tv_publisher_info);
            tvAdd = WidgetUtil.obtainViewById(itemView, R.id.tv_add);
            tvAdd.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_add:
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(v, data.get(position), 1);
                    }
                    break;
                default:
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(v, data.get(position), 0);
                    }
                    break;
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

        public void hasAdded(long bookId) {
            if (addedIds.contains(bookId)) {
                tvAdd.setText("已存在");
                tvAdd.setEnabled(false);
                tvAdd.setBackgroundResource(R.drawable.gray_round_bg);
            } else {
                tvAdd.setText("添加");
                tvAdd.setEnabled(true);
                tvAdd.setBackgroundResource(R.drawable.blue_round_bg);
            }
        }
    }

    class BooksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private int position;
        private SimpleDraweeView sdvBookCover;
        private TextView tvBookName, tvPublisherInfo, tvGoodNum;
        private RatingBar rbGoodRating;
        private LinearLayout llItem;

        public BooksViewHolder(View itemView) {
            super(itemView);
            sdvBookCover = (SimpleDraweeView) itemView.findViewById(R.id.sdv_book_sheet_cover);
            tvBookName = (TextView) itemView.findViewById(R.id.tv_book_name);
            tvPublisherInfo = (TextView) itemView.findViewById(R.id.tv_publisher_info);
            rbGoodRating = (RatingBar) itemView.findViewById(R.id.rb_good_rating);
            tvGoodNum = (TextView) itemView.findViewById(R.id.tv_good_num);
            Button btnDel = WidgetUtil.obtainViewById(itemView, R.id.btn_delete);
            btnDel.setOnClickListener(this);
            itemView.setOnClickListener(this);
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT_WATCH) {
                LayerDrawable ld = (LayerDrawable) rbGoodRating.getProgressDrawable();
                ld.getDrawable(0).setColorFilter(itemView.getResources().getColor(R.color.font_black_light),
                        PorterDuff.Mode.SRC_ATOP);
                ld.getDrawable(1).setColorFilter(itemView.getResources().getColor(R.color.yellow), PorterDuff.Mode
                        .SRC_ATOP);
                ld.getDrawable(2).setColorFilter(itemView.getResources().getColor(R.color.orange), PorterDuff.Mode
                        .SRC_ATOP);
            }
            llItem = WidgetUtil.obtainViewById(itemView, R.id.ll_item);
            llItem.setOnClickListener(v -> {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(v, data.get(position), 0);
                }
            });
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
                if (v == itemView) {
                    itemClickListener.onItemClick(v, data.get(position), 0);
                } else if (v.getId() == R.id.btn_delete) {
                    itemClickListener.onItemClick(v, data.get(position), 1);
                }
            }
        }
    }

    private OnItemClickListener itemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        itemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, Book book, int operaType);
    }
}
