package com.culturebud.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.culturebud.R;
import com.culturebud.bean.Comment;
import com.culturebud.bean.CommentReply;
import com.culturebud.bean.MyRelatedComment;
import com.culturebud.adapter.CommentMyRelatedAdapter.MyRelatedViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by XieWei on 2016/11/20.
 */

public class CommentMyRelatedAdapter extends RecyclerView.Adapter<MyRelatedViewHolder> {
    private List<MyRelatedComment> data;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm", Locale.getDefault());

    public CommentMyRelatedAdapter() {
        data = new ArrayList<>();
    }

    public void clearData() {
        if (!data.isEmpty()) {
            data.clear();
            notifyDataSetChanged();
        }
    }

    public void addItems(List<MyRelatedComment> items) {
        if (items != null && !items.isEmpty()) {
            int position = data.size() - 1;
            data.addAll(items);
            notifyItemRangeChanged(position, items.size());
        }
    }

    @Override
    public MyRelatedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyRelatedViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_my_related_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MyRelatedViewHolder holder, int position) {
        MyRelatedComment item = data.get(position);
        holder.position = position;
        holder.setFace(item.getNewReply().getAvatar());
        holder.setReplierNick(item.getNewReply().getNickname());
        holder.setReplierContent(item.getNewReply().getContent());

        holder.setCreateTime(item.getNewReply().getCreatedTime());

        holder.setCommentContent(item.getComment().getNickname(), item.getComment().getContent());

        holder.setTitlle(item.getComment().getTitle());
        holder.setAuthor(item.getComment().getAuthor());

        holder.setReplies(item.getCommentReplyList());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyRelatedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private int position;
        private SimpleDraweeView sdvFace;
        private TextView tvReplierNick, tvReplierContent;

        private TextView tvCreateTime, tvReplyNum;

        private TextView tvCommentContent;

        private TextView tvTitle, tvAuthor;

        private RecyclerView rvReplies;

        public MyRelatedViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            sdvFace = (SimpleDraweeView) itemView.findViewById(R.id.sdv_face);
            tvReplierNick = (TextView) itemView.findViewById(R.id.tv_nick_name);
            tvReplierContent = (TextView) itemView.findViewById(R.id.tv_content);

            tvCreateTime = (TextView) itemView.findViewById(R.id.tv_create_time);
            tvReplyNum = (TextView) itemView.findViewById(R.id.tv_reply_num);

            tvCommentContent = (TextView) itemView.findViewById(R.id.tv_comment_content);

            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvAuthor = (TextView) itemView.findViewById(R.id.tv_author);

            rvReplies = (RecyclerView) itemView.findViewById(R.id.rv_replies);
            LinearLayoutManager llm = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.VERTICAL, false);
            rvReplies.setLayoutManager(llm);
            rvReplies.setAdapter(new CommentReplyAdapter());
            setListeners();
        }

        private void setListeners() {

        }

        public void setFace(String url) {
            if (TextUtils.isEmpty(url)) {
                return;
            }
            sdvFace.setImageURI(url);
        }

        public void setReplierNick(String nick) {
            if (TextUtils.isEmpty(nick)) {
                return;
            }
            tvReplierNick.setText(nick);
        }

        public void setCreateTime(long time) {
            tvCreateTime.setText(sdf.format(new Date(time)));
        }

        public void setReplyNum(long replyNum) {
            tvReplyNum.setText(String.valueOf(replyNum));
        }

        public void setReplierContent(String content) {
            if (TextUtils.isEmpty(content)) {
                return;
            }
            tvReplierContent.setText(content);
        }

        public void setCommentContent(String nick, String content) {
            if (TextUtils.isEmpty(nick) && TextUtils.isEmpty(content)) {
                return;
            }
            tvCommentContent.setText(nick + " : " + content);
        }

        public void setTitlle(String titlle) {
            if (TextUtils.isEmpty(titlle)) {
                return;
            }
            tvTitle.setText(titlle);
        }

        public void setAuthor(String author) {
            if (TextUtils.isEmpty(author)) {
                return;
            }
            tvAuthor.setText(author);
        }

        public void setReplies(List<CommentReply> replies) {
            ((CommentReplyAdapter) rvReplies.getAdapter()).clearData();
            ((CommentReplyAdapter) rvReplies.getAdapter()).addItems(replies);
        }

        @Override
        public void onClick(View view) {
            if (view == itemView && onItemClickLisntener != null) {
                onItemClickLisntener.onItemClick(position, view, data.get(position).getComment());
                return;
            }
        }
    }

    private OnItemClickListener onItemClickLisntener;

    public void setOnItemClickLisntener(OnItemClickListener onItemClickLisntener) {
        this.onItemClickLisntener = onItemClickLisntener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View v, Comment comment);
    }
}
