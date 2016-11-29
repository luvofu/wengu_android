package com.culturebud.adapter;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.culturebud.BaseApp;
import com.culturebud.CommonConst.CommentOperaType;
import com.culturebud.R;
import com.culturebud.bean.Comment;
import com.facebook.drawee.view.SimpleDraweeView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by XieWei on 2016/11/10.
 */

public class BookCommunityCommentAdapter extends RecyclerView.Adapter<BookCommunityCommentAdapter.BookCommunityCommentViewHolder> {
    private List<Comment> data;
    private static SimpleDateFormat sdf = new SimpleDateFormat("MM-dd kk:mm", Locale.getDefault());

    public BookCommunityCommentAdapter() {
        data = new ArrayList<>();
    }

    public void addItems(List<Comment> comments) {
        if (comments == null || comments.isEmpty()) {
            return;
        }
        int position = data.size() - 1;
        data.addAll(comments);
        notifyItemRangeChanged(position, comments.size());
    }

    public void clearData() {
        if (data.isEmpty()) {
            return;
        }
        data.clear();
        notifyDataSetChanged();
    }

    @Override
    public BookCommunityCommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BookCommunityCommentViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_community_comment_item, parent, false));
    }

    @Override
    public void onBindViewHolder(BookCommunityCommentViewHolder holder, int position) {
        Comment item = data.get(position);
        holder.position = position;
        holder.setFace(item.getAvatar());
        holder.setNick(item.getNickname());
        holder.setContent(item.getContent());
        holder.setCreateTime(item.getCreatedTime());
        holder.setGoodNum(item.getGoodNum());
        holder.setGood(item.isGood());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void onThumbUp(boolean res, long commentId, int position) {
        if (data.isEmpty()) {
            return;
        }
        if (position >= 0 && position < data.size()) {
            Comment item = data.get(position);
            if (item.getCommentId() == commentId) {
                item.setGood(res);
                if (res) {
                    item.setGoodNum(item.getGoodNum() + 1);
                } else {
                    item.setGoodNum(item.getGoodNum() - 1);
                }
                notifyItemChanged(position);
                return;
            }
        }
        for (int i = 0; i < data.size(); i++) {
            Comment item = data.get(i);
            if (item.getCommentId() == commentId) {
                item.setGood(res);
                if (res) {
                    item.setGoodNum(item.getGoodNum() + 1);
                } else {
                    item.setGoodNum(item.getGoodNum() - 1);
                }
                notifyItemChanged(i);
                return;
            }
        }
    }

    class BookCommunityCommentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private SimpleDraweeView sdvFace;
        private TextView tvNick, tvContent;
        private TextView tvCreateTime, tvGoodNum, tvReplyNum;
        private ImageView ivShare;
        private int position;

        public BookCommunityCommentViewHolder(View itemView) {
            super(itemView);
            sdvFace = (SimpleDraweeView) itemView.findViewById(R.id.sdv_face);
            tvNick = (TextView) itemView.findViewById(R.id.tv_nick_name);
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
            tvCreateTime = (TextView) itemView.findViewById(R.id.tv_create_time);
            tvGoodNum = (TextView) itemView.findViewById(R.id.tv_good_num);
            tvReplyNum = (TextView) itemView.findViewById(R.id.tv_reply_num);
            ivShare = (ImageView) itemView.findViewById(R.id.iv_share);
            setListeners();
        }

        private void setListeners() {
            itemView.setOnClickListener(this);
            tvGoodNum.setOnClickListener(this);
            tvReplyNum.setOnClickListener(this);
            ivShare.setOnClickListener(this);
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public void setFace(String url) {
            if (TextUtils.isEmpty(url)) {
                return;
            }
            Uri uri = Uri.parse(url);
            sdvFace.setImageURI(uri);
        }

        public void setNick(CharSequence nick) {
            if (TextUtils.isEmpty(nick)) {
                return;
            }
            tvNick.setText(nick);
        }

        public void setContent(CharSequence content) {
            if (TextUtils.isEmpty(content)) {
                return;
            }
            tvContent.setText(content);
        }

        public void setCreateTime(long time) {
            String createTime = sdf.format(new Date(time));
            tvCreateTime.setText(createTime);
        }

        public void setGoodNum(long goodNum) {
            tvGoodNum.setText(String.valueOf(goodNum));
        }

        public void setReplyNum(long replyNum) {
            tvReplyNum.setText(String.valueOf(replyNum));
        }

        public void setGood(boolean isGood) {
            Drawable drawable = null;
            if (isGood) {
                drawable = BaseApp.getInstance().getResources().getDrawable(R.mipmap.good_true);
            } else {
                drawable = BaseApp.getInstance().getResources().getDrawable(R.mipmap.good_false);
            }
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            tvGoodNum.setCompoundDrawables(drawable, null, null, null);
        }

        @Override
        public void onClick(View v) {
            if (v == itemView && onItemClickListener != null) {
                onItemClickListener.onItemClick(position, v, data.get(position));
                return;
            }
            switch (v.getId()) {
                case R.id.tv_good_num:
                    if (onOperaClickListener != null) {
                        onOperaClickListener.onOperaClick(position, v, CommentOperaType.TYPE_THUMB_UP, data.get(position));
                    }
                    break;
                case R.id.tv_reply_num:
                    if (onOperaClickListener != null) {
                        onOperaClickListener.onOperaClick(position, v, CommentOperaType.TYPE_REPLY, data.get(position));
                    }
                    break;
                case R.id.iv_share:
                    if (onOperaClickListener != null) {
                        onOperaClickListener.onOperaClick(position, v, CommentOperaType.TYPE_SHARE, data.get(position));
                    }
                    break;
            }
        }
    }

    private OnOperaClickListener onOperaClickListener;

    public void setOnOperaClickListener(OnOperaClickListener onOperaClickListener) {
        this.onOperaClickListener = onOperaClickListener;
    }

    public interface OnOperaClickListener {
        void onOperaClick(int position, View v, int type, Comment comment);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View v, Comment comment);
    }
}
