package com.culturebud.adapter;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import com.culturebud.BaseApp;
import com.culturebud.CommonConst.CommentOperaType;
import com.culturebud.R;
import com.culturebud.bean.Comment;
import com.culturebud.bean.CommentReply;
import com.facebook.drawee.view.SimpleDraweeView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by XieWei on 2016/11/9.
 */

public class CommentDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Comment comment;
    private List<CommentReply> replies;
    private static final int TYPE_COMENT = 0;
    private static final int TYPE_COMENT_ME = 1;
    private static final int TYPE_REPLY = 2;
    private static final int TYPE_REPLY_REPLY = 3;

    public CommentDetailAdapter() {
        replies = new ArrayList<>();
    }

    public void setComment(Comment comment) {
        this.comment = comment;
        notifyItemChanged(0);
    }

    public Comment getComment() {
        return comment;
    }

    public CommentReply getItem(int position) {
        if (position > 0) {
            return replies.get(position - 1);
        }
        return null;
    }

    public void setGood(boolean good) {
        if (comment != null) {
            comment.setGood(good);
            if (good) {
                comment.setGoodNum(comment.getGoodNum() + 1);
            } else {
                comment.setGoodNum(comment.getGoodNum() - 1);
            }
            notifyItemChanged(0);
        }
    }

    public void addReplies(List<CommentReply> replies) {
        if (replies == null || replies.size() <= 0) {
            return;
        }
        int position = replies.size() - 1;
        this.replies.addAll(replies);
        notifyItemRangeChanged(position, replies.size());
    }

    public void addReply(CommentReply reply) {
        if (reply == null) {
            return;
        }
        replies.add(0, reply);
        notifyItemRangeChanged(1, 2);
    }

    public void delReply(long replyId) {
        int count = replies.size();
        int position = -1;
        for (int i = 0; i < count; i++) {
            if (replies.get(i).getReplyId() == replyId) {
                position = i;
                break;
            }
        }
        if (position != -1) {
            replies.remove(position);
            notifyItemRemoved(position + 1);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case TYPE_COMENT:
                holder = new CommentDetailViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.front_hot_comment_item, parent, false));
                break;
            case TYPE_COMENT_ME:
                holder = new CommentDetailViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.front_hot_comment_item, parent, false));
                ((CommentDetailViewHolder) holder).inflateMoreOpera(R.layout.more_operas);
                break;
            case TYPE_REPLY:
                holder = new CommentDetailReplyViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.comment_reply_item, parent, false));
                break;
            case TYPE_REPLY_REPLY:
                holder = new ReplyReplyViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.comment_reply_reply_item, parent, false));
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {
            CommentDetailViewHolder cdHolder = (CommentDetailViewHolder) holder;
            cdHolder.setFace(comment.getAvatar());
            cdHolder.setNick(comment.getNickname());
            cdHolder.setContent(comment.getContent());
            cdHolder.setTitle(comment.getTitle());
            cdHolder.setAuthor(comment.getAuthor());
            cdHolder.setCreateTime(comment.getCreatedTime());
            cdHolder.setGoodNum(comment.getGoodNum());
            cdHolder.setGood(comment.isGood());
            cdHolder.setReplyNum(comment.getReplyNum());
        } else {
            CommentReply reply = replies.get(position - 1);
            CommentDetailReplyViewHolder cdrHolder = (CommentDetailReplyViewHolder) holder;
            if (position == 1) {
                cdrHolder.showTop();
            } else {
                cdrHolder.hideTop();
            }
            cdrHolder.setCommentReply(reply);
            cdrHolder.setFace(reply.getAvatar());
            cdrHolder.setNick(reply.getNickname());
            cdrHolder.setCreateTime(reply.getCreatedTime());
            cdrHolder.setContent(reply.getContent());
            if (reply.getReplyType() == 1) {
                ReplyReplyViewHolder rrHolder = (ReplyReplyViewHolder) cdrHolder;
                rrHolder.setReplyContent(reply.getReplyObjNickname(), reply.getReplyObjContent());
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            if (BaseApp.getInstance().isMe(comment.getUserId())) {
                return TYPE_COMENT_ME;
            }
            return TYPE_COMENT;
        } else {
            CommentReply reply = replies.get(position - 1);
            if (reply.getReplyType() == 0) {
                return TYPE_REPLY;
            } else {
                return TYPE_REPLY_REPLY;
            }
        }
    }

    @Override
    public int getItemCount() {
        if (comment != null) {
            int count = 1;
            count += replies.size();
            return count;
        }
        return 0;
    }

    class CommentDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private SimpleDraweeView sdvFace;
        private TextView tvNick, tvContent;
        private TextView tvTitle, tvAuthor;
        private TextView tvCreateTime, tvGoodNum, tvReplyNum;
        private ImageView ivShare, ivDelete;
        private ViewStub vsMoreOperas;

        public CommentDetailViewHolder(View itemView) {
            super(itemView);
            vsMoreOperas = (ViewStub) itemView.findViewById(R.id.vs_delete);
            sdvFace = (SimpleDraweeView) itemView.findViewById(R.id.sdv_face);
//            ViewGroup.LayoutParams params = sdvFace.getLayoutParams();
//            int width = itemView.getResources().getDimensionPixelSize(R.dimen.comment_detail_face_size);
//            params.width = width;
//            params.height = width;
//            sdvFace.setLayoutParams(params);

            tvNick = (TextView) itemView.findViewById(R.id.tv_nick_name);
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvAuthor = (TextView) itemView.findViewById(R.id.tv_author);
            tvCreateTime = (TextView) itemView.findViewById(R.id.tv_create_time);
            tvGoodNum = (TextView) itemView.findViewById(R.id.tv_good_num);
            tvReplyNum = (TextView) itemView.findViewById(R.id.tv_reply_num);
            ivShare = (ImageView) itemView.findViewById(R.id.iv_share);
            setListeners();
        }

        public void inflateMoreOpera(@LayoutRes int resId) {
            vsMoreOperas.setLayoutResource(resId);
            View view = vsMoreOperas.inflate();
            ivDelete = (ImageView) view.findViewById(R.id.iv_more_opera);
            ivDelete.setOnClickListener(this);
        }

        private void setListeners() {
            tvGoodNum.setOnClickListener(this);
            tvReplyNum.setOnClickListener(this);
            ivShare.setOnClickListener(this);
            itemView.setOnClickListener(this);
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
                onItemClickListener.onItemClick(v, 0, comment, null);
                return;
            }
            if (onCommentOperaListener != null) {
                switch (v.getId()) {
                    case R.id.tv_good_num:
                        onCommentOperaListener.onCommentOpera(v, CommentOperaType.TYPE_THUMB_UP, comment);
                        break;
                    case R.id.tv_reply_num:
                        onCommentOperaListener.onCommentOpera(v, CommentOperaType.TYPE_REPLY, comment);
                        break;
                    case R.id.iv_share:
                        onCommentOperaListener.onCommentOpera(v, CommentOperaType.TYPE_SHARE, comment);
                        break;
                    case R.id.iv_more_opera://弹出删除对话框
                        //onCommentOperaListener.onCommentOpera(v, CommentOperaType.TYPE_DELETE, comment);
                        break;
                }
            }
        }
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position, Comment comment, CommentReply reply);
    }

    private OnCommentOperaListener onCommentOperaListener;

    public void setOnThumbListener(OnCommentOperaListener onCommentOperaListener) {
        this.onCommentOperaListener = onCommentOperaListener;
    }

    public interface OnCommentOperaListener {
        void onCommentOpera(View v, int type, Comment comment);
    }

    class CommentDetailReplyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private SimpleDraweeView sdvFace;
        private TextView tvNick, tvCreateTime, tvContent;
        private View top;
        private CommentReply reply;

        public CommentDetailReplyViewHolder(View itemView) {
            super(itemView);
            sdvFace = (SimpleDraweeView) itemView.findViewById(R.id.sdv_face);
            tvNick = (TextView) itemView.findViewById(R.id.tv_nick);
            tvCreateTime = (TextView) itemView.findViewById(R.id.tv_create_time);
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
            top = itemView.findViewById(R.id.v_top);
            itemView.setOnClickListener(this);
        }

        public void setCommentReply(CommentReply reply) {
            this.reply = reply;
        }

        public void showTop() {
            top.setVisibility(View.VISIBLE);
        }

        public void hideTop() {
            top.setVisibility(View.GONE);
        }

        public void setFace(String url) {
            if (TextUtils.isEmpty(url)) {
                return;
            }
            sdvFace.setImageURI(url);
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

        public void setContent(String content) {
            if (TextUtils.isEmpty(content)) {
                return;
            }
            tvContent.setText(content);
        }

        @Override
        public void onClick(View v) {
            if (v == itemView && onItemClickListener != null) {
                onItemClickListener.onItemClick(v, replies.indexOf(reply) + 1, comment, reply);
                return;
            }
        }
    }

    class ReplyReplyViewHolder extends CommentDetailReplyViewHolder {
        private TextView tvReplyContent;

        public ReplyReplyViewHolder(View itemView) {
            super(itemView);
            tvReplyContent = (TextView) itemView.findViewById(R.id.tv_reply_content);
        }

        public void setReplyContent(String nick, String content) {
            SpannableString ss = new SpannableString(nick + "：" + content);
            ss.setSpan(new ForegroundColorSpan(BaseApp.getInstance()
                            .getResources().getColor(R.color.front_hot_font)),
                    0, nick.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvReplyContent.setText(ss);
        }
    }

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm", Locale.getDefault());
}
