package com.culturebud.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.culturebud.R;
import com.culturebud.bean.BookSheet;
import com.culturebud.util.WidgetUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XieWei on 2016/10/25.
 */

public class BookSheetAdapter extends RecyclerView.Adapter<BookSheetAdapter.HotSheetViewHolder> {
    private List<BookSheet> data;
    private boolean isHorizontal;

    public BookSheetAdapter() {
        data = new ArrayList<>();
    }

    public void setHorizontal(boolean isHorizontal) {
        this.isHorizontal = isHorizontal;
    }

    public void clearData() {
        data.clear();
        notifyDataSetChanged();
    }

    public void addItems(List<BookSheet> bookSheets) {
        if (bookSheets == null || bookSheets.size() < 0) {
            return;
        }
        int position = data.size() - 1;
        data.addAll(bookSheets);
        notifyItemRangeChanged(position, bookSheets.size());
    }

    @Override
    public HotSheetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (isHorizontal) {
            return new HotSheetViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.front_hot_sheet_item_h, parent, false));
        } else {
            return new HotSheetViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.front_hot_sheet_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(HotSheetViewHolder holder, int position) {
        BookSheet sheet = data.get(position);
        holder.position = position;
        holder.setCover(sheet.getCover());
        holder.setColNum(sheet.getCollectionNum());
        holder.setName(sheet.getName());
        holder.setNickName(sheet.getNickname());
        holder.setDesc(sheet.getDescription());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class HotSheetViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvColNum, tvNickName, tvName, tvDesc;
        private SimpleDraweeView sdvCover;
        private int position;

        public HotSheetViewHolder(View itemView) {
            super(itemView);
            tvColNum = (TextView) itemView.findViewById(R.id.tv_col_num);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvNickName = (TextView) itemView.findViewById(R.id.tv_nick_name);
            if (isHorizontal) {
                tvDesc = WidgetUtil.obtainViewById(itemView, R.id.tv_desc);
            }
            sdvCover = (SimpleDraweeView) itemView.findViewById(R.id.sdv_book_sheet_cover);
            itemView.setOnClickListener(this);
        }

        public void setDesc(String desc) {
            if (TextUtils.isEmpty(desc)) {
                return;
            }
            if (isHorizontal) {
                tvDesc.setText(desc);
            }
        }

        public void setColNum(long num) {
            tvColNum.setText(String.valueOf(num));
        }

        public void setName(CharSequence name) {
            tvName.setText(name);
        }

        public void setNickName(CharSequence nickName) {
            tvNickName.setText(nickName);
        }

        public void setCover(String url) {
            if (TextUtils.isEmpty(url)) {
                return;
            }
            Uri uri = Uri.parse(url);
            sdvCover.setImageURI(uri);
        }

        @Override
        public void onClick(View v) {
            if (v == itemView && listener != null) {
                listener.onItemClick(position, v, data.get(position));
            }
        }
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View v, BookSheet sheet);
    }
}
