package com.culturebud.adapter;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.RecyclerView.Adapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.culturebud.BaseApp;
import com.culturebud.R;
import com.culturebud.bean.Notebook;
import com.culturebud.bean.User;
import com.culturebud.util.WidgetUtil;
import com.culturebud.widget.SwipeMenuView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by XieWei on 2016/11/20.
 */

public class NotebookAdapter extends Adapter<NotebookAdapter.NotebookViewHolder> {
    private List<Notebook> data;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm", Locale.getDefault());
    private long userId;

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public NotebookAdapter() {
        data = new ArrayList<>();
    }

    public void clearData() {
        if (!data.isEmpty()) {
            data.clear();
            notifyDataSetChanged();
        }
    }

    public void addItems(List<Notebook> notebooks) {
        if (notebooks != null && !notebooks.isEmpty()) {
            int position = data.size() - 1;
            data.addAll(notebooks);
            notifyItemRangeChanged(position, notebooks.size());
        }
    }

    public void deleteItem(Notebook notebook) {
        if (notebook == null) {
            return;
        }
        int idx = data.indexOf(notebook);
        if (idx >= 0) {
            data.remove(notebook);
            notifyItemRemoved(idx);
        }
    }

    @Override
    public NotebookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NotebookViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notebooks_item, parent, false));
    }

    @Override
    public void onBindViewHolder(NotebookViewHolder holder, int position) {
        Notebook item = data.get(position);
        holder.position = position;
        holder.setBookCover(item.getCover());
        holder.setBookName(item.getTitle()/* + "." + item.getName()*/);
        holder.setBookNum(item.getNoteNum());
        holder.setCreateTime(item.getCreatedTime());
        holder.canDelete();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class NotebookViewHolder extends ViewHolder implements View.OnClickListener {
        private SimpleDraweeView sdvBookCover;
        private TextView tvBookName, tvNoteNum;
        private TextView tvCreateTime;
        private int position;
        private Button btnDel;
        private RelativeLayout rlItem;

        public NotebookViewHolder(View itemView) {
            super(itemView);
            sdvBookCover = (SimpleDraweeView) itemView.findViewById(R.id.sdv_book_sheet_cover);
            tvBookName = (TextView) itemView.findViewById(R.id.tv_book_name);
            tvNoteNum = (TextView) itemView.findViewById(R.id.tv_note_num);
            tvCreateTime = (TextView) itemView.findViewById(R.id.tv_create_time);
            btnDel = WidgetUtil.obtainViewById(itemView, R.id.btn_delete);
            btnDel.setOnClickListener(this);
            rlItem = WidgetUtil.obtainViewById(itemView, R.id.rl_item);
            rlItem.setOnClickListener(this);
        }

        public void canDelete() {
            User user = BaseApp.getInstance().getUser();
            if (user != null && user.getUserId() == userId) {
                btnDel.setVisibility(View.VISIBLE);
            } else {
                btnDel.setVisibility(View.GONE);
            }
        }

        public void setBookCover(String url) {
            if (TextUtils.isEmpty(url)) {
                return;
            }
            sdvBookCover.setImageURI(url);
        }

        public void setBookName(String name) {
            if (TextUtils.isEmpty(name)) {
                return;
            }
            tvBookName.setText(name);
        }

        public void setBookNum(long num) {
            tvNoteNum.setText(String.valueOf(num));
        }

        public void setCreateTime(long time) {
            tvCreateTime.setText(sdf.format(new Date(time)));
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null) {
                if (view == rlItem) {
                    onItemClickListener.onItemClick(position, view, data.get(position), 0);
                } else if (view == btnDel) {
                    onItemClickListener.onItemClick(position, view, data.get(position), 1);
                }
            }
        }
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View v, Notebook notebook, int operaType);
    }
}
