package com.culturebud.adapter;

import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.culturebud.R;
import com.culturebud.bean.Book;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by H on 2017/3/18.
 */

public class ImportBooksAdapter extends RecyclerView.Adapter<ImportBooksAdapter.BooksViewHolder> {
    private List<Book> data;


    public ImportBooksAdapter() {
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

    public void afterAddBook(long bookId) {
        boolean change = false;
        for (Book book : data) {
            if (book.getBookId() == bookId) {
                change = true;
                book.setContain(true);
            }
        }
        if (change) {
            notifyDataSetChanged();
        }

    }

    @Override
    public BooksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new BooksViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.import_books_item, parent, false));
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
        holder.setAddBookBtn(book.isContain());

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
        private Button addBook;

        public BooksViewHolder(View itemView) {
            super(itemView);
            sdvBookCover = (SimpleDraweeView) itemView.findViewById(R.id.sdv_book_sheet_cover);
            tvBookName = (TextView) itemView.findViewById(R.id.tv_book_name);
            tvPublisherInfo = (TextView) itemView.findViewById(R.id.tv_publisher_info);
            rbGoodRating = (RatingBar) itemView.findViewById(R.id.rb_good_rating);
            tvGoodNum = (TextView) itemView.findViewById(R.id.tv_good_num);
            addBook = (Button) itemView.findViewById(R.id.add_book);
            itemView.setOnClickListener(this);
            addBook.setOnClickListener(this);
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT_WATCH) {
                if (rbGoodRating.getProgressDrawable() != null && rbGoodRating.getProgressDrawable() instanceof
                        LayerDrawable) {
                    LayerDrawable ld = (LayerDrawable) rbGoodRating.getProgressDrawable();
                    ld.getDrawable(0).setColorFilter(itemView.getResources().getColor(R.color.light_font_black),
                            PorterDuff.Mode.SRC_ATOP);
                    ld.getDrawable(1).setColorFilter(itemView.getResources().getColor(R.color.yellow), PorterDuff
                            .Mode.SRC_ATOP);
                    ld.getDrawable(2).setColorFilter(itemView.getResources().getColor(R.color.orange), PorterDuff
                            .Mode.SRC_ATOP);
                }
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

        public void setAddBookBtn(boolean contain) {
            if (contain) {
                //  addBook.setBackground(itemView.getResources().getDrawable(R.drawable.import_book_bg));
                addBook.setBackgroundResource(R.drawable.import_book_bg2);
                addBook.setText("已在书架");
            } else {
                addBook.setBackgroundResource(R.drawable.import_book_bg);
                addBook.setText("加入书架");
            }
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
