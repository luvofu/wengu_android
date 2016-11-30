package com.culturebud.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.culturebud.R;
import com.culturebud.bean.BookSheetDetail;
import com.culturebud.bean.SheetBook;
import com.culturebud.widget.TagFlowLayout;
import com.facebook.drawee.view.SimpleDraweeView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.culturebud.adapter.BookSheetDetailAdapter.OnItemListener.OPERA_TYPE_ADD;
import static com.culturebud.adapter.BookSheetDetailAdapter.OnItemListener.OPERA_TYPE_ITEM;

/**
 * Created by XieWei on 2016/11/8.
 */

public class BookSheetDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private BookSheetDetail data;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_BOOK = 1;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());
    private Context context;

    public BookSheetDetailAdapter(Context context) {
        this.context = context;
    }

    public void setData(BookSheetDetail data) {
        if (data != null) {
            this.data = data;
            notifyDataSetChanged();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case TYPE_HEADER:
                holder = new BookSheetDetailViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.book_sheet_detail_header, parent, false));
                break;
            case TYPE_BOOK:
                holder = new SheetBooksViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.book_sheet_detail_books_item, parent, false));
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {
            BookSheetDetailViewHolder bsHolder = (BookSheetDetailViewHolder) holder;
            bsHolder.setBookName(data.getName());
            bsHolder.setCover(data.getCover());
            bsHolder.setCreateTime(data.getCreatedTime());
            bsHolder.setColNum(data.getCollectionNum());
            bsHolder.setCollect(data.isCollect());
            bsHolder.setDesc(data.getDescription());
            bsHolder.setNick(data.getNickname());
            bsHolder.setBookNum(data.getBookNum());
            String tagStr = data.getTag();
            if (!TextUtils.isEmpty(tagStr)) {
                String[] tags = tagStr.split("\\|");
                bsHolder.setTags(tags);
            }
        } else {
            SheetBook item = data.getSheetBookList().get(position - 1);
            SheetBooksViewHolder sbHolder = (SheetBooksViewHolder) holder;
            sbHolder.position = position;
            sbHolder.setCover(item.getCover());
            sbHolder.setBookName(item.getTitle());
            sbHolder.setAuthor(item.getAuthor());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else {
            return TYPE_BOOK;
        }
    }

    @Override
    public int getItemCount() {
        if (data != null) {
            List<SheetBook> books = data.getSheetBookList();
            int count = 1;
            if (books != null && books.size() > 0) {
                count += books.size();
            }
            return count;
        }
        return 0;
    }

    class BookSheetDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private SimpleDraweeView sdvCover;
        private TextView tvTitle, tvNick, tvCreateTime, tvColNum;
        private ImageView ivShare;
        private TextView tvDesc, tvBookNum;
        private TagFlowLayout flTags;

        public BookSheetDetailViewHolder(View itemView) {
            super(itemView);
            sdvCover = (SimpleDraweeView) itemView.findViewById(R.id.sdv_book_cover);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_book_name);
            tvNick = (TextView) itemView.findViewById(R.id.tv_nick);
            tvCreateTime = (TextView) itemView.findViewById(R.id.tv_create_time);
            tvColNum = (TextView) itemView.findViewById(R.id.tv_collect);
            ivShare = (ImageView) itemView.findViewById(R.id.iv_share);
            tvDesc = (TextView) itemView.findViewById(R.id.tv_desc);
            tvBookNum = (TextView) itemView.findViewById(R.id.tv_book_num);
            flTags = (TagFlowLayout) itemView.findViewById(R.id.fl_tags);

            setListener();
        }

        private void setListener() {
            tvColNum.setOnClickListener(this);
            ivShare.setOnClickListener(this);
        }

        public void setTags(String[] tags) {
            if (tags == null || tags.length <= 0) {
                return;
            }
//            List<String> tmp = Arrays.asList(tags);
            BookSheetTagsAdapter tagsAdapter = new BookSheetTagsAdapter(tags);
            flTags.setAdapter(tagsAdapter);
//            tagsAdapter.setData(tmp);
//            flTags.requestLayout();
        }

        public void setCover(String url) {
            sdvCover.setImageURI(url);
        }

        public void setBookName(String title) {
            if (TextUtils.isEmpty(title)) {
                return;
            }
            tvTitle.setText(title);
        }

        public void setNick(String nick) {
            if (TextUtils.isEmpty(nick)) {
                return;
            }
            tvNick.setText(nick);
        }


        public void setCreateTime(long time) {
            Date date = new Date(time);
            tvCreateTime.setText(sdf.format(date));
        }

        public void setColNum(long num) {
            tvColNum.setText("" + num);
        }

        public void setCollect(boolean isCollected) {
            Drawable drawable = null;
            if (isCollected) {
                drawable = context.getResources().getDrawable(R.mipmap.collect_icon_checked);
            } else {
                drawable = context.getResources().getDrawable(R.mipmap.collect_icon_default);
            }
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            tvColNum.setCompoundDrawables(drawable, null, null, null);
        }

        public void setDesc(String desc) {
            if (TextUtils.isEmpty(desc)) {
                return;
            }
            tvDesc.setText(desc);
        }

        public void setBookNum(long num) {
            tvBookNum.setText(String.format(Locale.getDefault(),
                    tvBookNum.getContext().getString(R.string.book_num), num));
        }

        @Override
        public void onClick(View v) {
            if (onHeaderClickListener != null) {
                switch (v.getId()) {
                    case R.id.tv_collect:
                        onHeaderClickListener.onHeaderClick(v, 0, data);
                        break;
                    case R.id.iv_share:
                        onHeaderClickListener.onHeaderClick(v, 1, data);
                        break;
                }
            }
        }
    }

    private OnHeaderClickListener onHeaderClickListener;

    public void setOnHeaderClickListener(OnHeaderClickListener onHeaderClickListener) {
        this.onHeaderClickListener = onHeaderClickListener;
    }

    public interface OnHeaderClickListener {
        void onHeaderClick(View v, int type, BookSheetDetail detail);
    }

    class SheetBooksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private SimpleDraweeView sdvCover;
        private TextView tvTitle, tvAuthor;
        private ImageView ivAdd;
        private int position;

        public SheetBooksViewHolder(View itemView) {
            super(itemView);
            sdvCover = (SimpleDraweeView) itemView.findViewById(R.id.sdv_book_cover);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_book_name);
            tvAuthor = (TextView) itemView.findViewById(R.id.tv_author);
            ivAdd = (ImageView) itemView.findViewById(R.id.iv_add);
            ivAdd.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        public void setCover(String url) {
            if (TextUtils.isEmpty(url)) {
                return;
            }
            sdvCover.setImageURI(url);
        }

        public void setBookName(String title) {
            if (TextUtils.isEmpty(title)) {
                return;
            }
            tvTitle.setText(title);
        }

        public void setAuthor(String author) {
            if (TextUtils.isEmpty(author)) {
                return;
            }
            tvAuthor.setText(author);
        }

        @Override
        public void onClick(View view) {
            if (onItemListener != null) {
                if (view == itemView) {
                    onItemListener.onItemOpera(view, position, OPERA_TYPE_ITEM,
                            data.getSheetBookList().get(position - 1));
                } else if (view == ivAdd) {
                    onItemListener.onItemOpera(view, position, OPERA_TYPE_ADD,
                            data.getSheetBookList().get(position - 1));
                }
            }
        }
    }

    private OnItemListener onItemListener;

    public void setOnItemListener(OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    public interface OnItemListener {
        public static final int OPERA_TYPE_ITEM = 0;
        public static final int OPERA_TYPE_ADD = 1;
        void onItemOpera(View v, int position, int operaType, SheetBook sheetBook);
    }
}
