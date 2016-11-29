package com.culturebud.adapter;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.culturebud.BaseApp;
import com.culturebud.R;
import com.culturebud.bean.Comment;
import com.culturebud.bean.User;
import com.facebook.drawee.view.SimpleDraweeView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by XieWei on 2016/10/25.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private static final String TAG = "CommentAdapter";
    private List<Comment> data;
    private static SimpleDateFormat sdf = new SimpleDateFormat("MM-dd kk:mm", Locale.getDefault());
    private static final int TYPE_SELF = 0;
    private static final int TYPE_OTHER = 1;
    private boolean hasOpera;

    public CommentAdapter(boolean hasOpera) {
        data = new ArrayList<>();
        this.hasOpera = hasOpera;
    }

    public void clearData() {
        data.clear();
        notifyDataSetChanged();
    }

    public void addItems(List<Comment> comments) {
        if (comments == null || comments.size() < 0) {
            return;
        }
        int position = data.size() - 1;
        data.addAll(comments);
        notifyItemRangeChanged(position, comments.size());
    }

    public void onThumbUp(long commentId, boolean isGood) {
        for (int i = 0; i < data.size(); i++) {
            Comment comment = data.get(i);
            if (comment.getCommentId() == commentId) {
                comment.setGood(isGood);
                if (isGood) {
                    comment.setGoodNum(comment.getGoodNum() + 1);
                } else {
                    comment.setGoodNum(comment.getGoodNum() - 1);
                }
                notifyItemChanged(i);
                break;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        User user = BaseApp.getInstance().getUser();
        if (user == null) {
            return TYPE_OTHER;
        }
        Comment item = data.get(position);
        if (hasOpera && item.getUserId() == user.getUserId()) {
            return TYPE_SELF;
        }
        return TYPE_OTHER;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_SELF) {
            CommentViewHolder holder = new CommentViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.front_hot_comment_item, parent, false));
            holder.inflateOperaView(R.layout.more_operas);
            return holder;
        } else {
            return new CommentViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.front_hot_comment_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        Comment comment = data.get(position);
        holder.setPosition(position);
        holder.setFace(comment.getAvatar());
        holder.setNick(comment.getNickname());
        holder.setContent(comment.getContent());
        holder.setTitle(comment.getTitle());
        holder.setAuthor(comment.getAuthor());
        holder.setCreateTime(comment.getCreatedTime());
        holder.setGoodNum(comment.getGoodNum());
        holder.setReplyNum(comment.getReplyNum());
        holder.setGood(comment.isGood());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class CommentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private SimpleDraweeView sdvFace;
        private ViewStub vsOpera;
        private ImageView ivDelete;
        private TextView tvNick, tvContent;
        private TextView tvTitle, tvAuthor;
        private TextView tvCreateTime, tvGoodNum, tvReplyNum;
        private ImageView ivShare;
        private RelativeLayout rlInfo;
        private int position;

        public CommentViewHolder(View itemView) {
            super(itemView);
            sdvFace = (SimpleDraweeView) itemView.findViewById(R.id.sdv_face);
            vsOpera = (ViewStub) itemView.findViewById(R.id.vs_delete);
            tvNick = (TextView) itemView.findViewById(R.id.tv_nick_name);
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvAuthor = (TextView) itemView.findViewById(R.id.tv_author);
            tvCreateTime = (TextView) itemView.findViewById(R.id.tv_create_time);
            tvGoodNum = (TextView) itemView.findViewById(R.id.tv_good_num);
            tvReplyNum = (TextView) itemView.findViewById(R.id.tv_reply_num);
            ivShare = (ImageView) itemView.findViewById(R.id.iv_share);
            rlInfo = (RelativeLayout) itemView.findViewById(R.id.rl_info);
            setListeners();
        }

        public void inflateOperaView(@LayoutRes int resId) {
            vsOpera.setLayoutResource(resId);
            View view = vsOpera.inflate();
            ivDelete = (ImageView) view.findViewById(R.id.iv_more_opera);
            ivDelete.setOnClickListener(this);
        }

        private void setListeners() {
            itemView.setOnClickListener(this);
            tvGoodNum.setOnClickListener(this);
            tvReplyNum.setOnClickListener(this);
            ivShare.setOnClickListener(this);
            rlInfo.setOnClickListener(this);
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

        public void setTitle(CharSequence title) {
            if (TextUtils.isEmpty(title)) {
                return;
            }
            tvTitle.setText(title);
        }

        public void setAuthor(CharSequence author) {
            if (TextUtils.isEmpty(author)) {
                return;
            }
            tvAuthor.setText(author);
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
            Log.d(TAG, "setGood() --> " + isGood);
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
                onItemClickListener.onItemClick(v, position, data.get(position));
                return;
            }
            switch (v.getId()) {
                case R.id.tv_good_num:
                    if (onThumbUpClickListener != null) {
                        onThumbUpClickListener.onThumbUpClick(data.get(position).getCommentId());
                    }
                    break;
                case R.id.tv_reply_num: {//目前逻辑与 item click 一样，都是进详情
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, position, data.get(position));
                    }
                    break;
                }
                case R.id.rl_info: {
                    if (onBookCommunityClickListener != null) {
                        onBookCommunityClickListener.onBookCommunityClick(v, position, data.get(position));
                    }
                    break;
                }
                case R.id.iv_share:
                    if (onShareListener != null) {
                        onShareListener.onShare(v, position, data.get(position));
                    }
                    break;
            }
        }
    }

    public void setOnThumbUpClickListener(OnThumbUpClickListener listener) {
        onThumbUpClickListener = listener;
    }

    private OnThumbUpClickListener onThumbUpClickListener;

    public interface OnThumbUpClickListener {
        void onThumbUpClick(long commentId);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position, Comment comment);
    }

    private OnBookCommunityClickListener onBookCommunityClickListener;

    public void setOnBookCommunityClickListener(OnBookCommunityClickListener onBookCommunityClickListener) {
        this.onBookCommunityClickListener = onBookCommunityClickListener;
    }

    public interface OnBookCommunityClickListener {
        void onBookCommunityClick(View v, int position, Comment comment);
    }

    private OnShareListener onShareListener;

    public void setOnShareListener(OnShareListener onShareListener) {
        this.onShareListener = onShareListener;
    }

    public interface OnShareListener {
        void onShare(View v, int position, Comment comment);
    }
}
